// quiz-sound.js

// 画面の読み込みがすべて終わってから処理を実行する
//document.addEventListener("DOMContentLoaded", function () {
//  // ★ HTML の <audio> を取得
//  const correctAudio = document.getElementById("correctSound");
//  const wrongAudio   = document.getElementById("wrongSound");
//  const mekuruAudio   = document.getElementById("mekuruSound");
//
//  // ★ 正解音を鳴らす関数
//  window.playCorrectSound = function () {
//    if (!correctAudio) return;
//    correctAudio.currentTime = 0;  // 毎回頭から再生する
//    correctAudio.play();
//  };
//
//  // ★ 不正解音を鳴らす関数
//  window.playWrongSound = function () {
//    if (!wrongAudio) return;
//    wrongAudio.currentTime = 0;    // 毎回頭から再生する
//    wrongAudio.play();
//  };
//  
//  // ★ めくる音を鳴らす関数
//  window.playMekuruSound = function () {
//    if (!mekuruAudio) return;
//    mekuruAudio.currentTime = 0;    // 毎回頭から再生する
//    mekuruAudio.play();
//  };
//
//  
//});


{

	// 画面の読み込みがすべて終わってから処理を実行する
	document.addEventListener("DOMContentLoaded", function() {
		// ★ HTML の <audio> を取得（フォールバック用としても使う）
		const correctAudio = document.getElementById("correctSound");
		const wrongAudio = document.getElementById("wrongSound");
		const mekuruAudio = document.getElementById("mekuruSound");

		// ===== Web Audio API 再生（同じ mp3 を別経路で鳴らす） =====
		// 目的：iPhoneでTTS後に効果音が小さくなり続ける現象を回避しやすくする
		const AudioCtx = window.AudioContext || window.webkitAudioContext;

		// Web Audio API が使えない環境では従来の <audio>.play() に戻す
		const canUseWebAudio = !!AudioCtx;

		let audioCtx = null;                 // AudioContext（初回のユーザー操作で作る）
		const bufferCache = new Map();       // key: 音源URL, value: Promise<AudioBuffer>

		// AudioContext を（必要な時に）生成して返す
		function getAudioContext() {
			if (!canUseWebAudio) return null;
			if (audioCtx) return audioCtx;
			audioCtx = new AudioCtx();
			return audioCtx;
		}

		// iOS対策：ユーザー操作の中で resume() される必要があるため、
		// playCorrectSound() など（クリックで呼ばれる想定）の先頭で必ず呼ぶ
		async function ensureContextResumed(ctx) {
			if (!ctx) return;
			if (ctx.state === "suspended") {
				try {
					await ctx.resume();
				} catch (e) {
					// resume できない場合はフォールバックへ
					console.warn("AudioContext resume failed:", e);
				}
			}
		}

		// <audio> 要素から実際の音源URLを取得（th:src でも currentSrc で取れる）
		function getAudioUrlFromElement(audioEl) {
			if (!audioEl) return null;
			// currentSrc が最優先（ブラウザが解決した最終URL）
			const url = audioEl.currentSrc || audioEl.src;
			return url || null;
		}

		// 音源を fetch→decode して AudioBuffer 化（1回だけ、以後キャッシュ）
		function loadBuffer(ctx, url) {
			if (!ctx || !url) return Promise.reject(new Error("ctx or url missing"));

			if (bufferCache.has(url)) {
				return bufferCache.get(url);
			}

			const p = fetch(url, { cache: "force-cache" })
				.then((res) => {
					if (!res.ok) throw new Error("fetch failed: " + res.status);
					return res.arrayBuffer();
				})
				.then((arr) => ctx.decodeAudioData(arr));

			bufferCache.set(url, p);
			return p;
		}

		// Web Audio API で再生（失敗したら false を返す）
		async function playWithWebAudio(audioEl) {
			if (!canUseWebAudio) return false;

			const ctx = getAudioContext();
			if (!ctx) return false;

			await ensureContextResumed(ctx);

			const url = getAudioUrlFromElement(audioEl);
			if (!url) return false;

			try {
				const buffer = await loadBuffer(ctx, url);

				// 再生のたびに Source を作る（AudioBufferSourceNode は使い捨て）
				const source = ctx.createBufferSource();
				source.buffer = buffer;

				// ここで GainNode を挟めば音量調整も可能（今は 1.0 のまま）
				// const gain = ctx.createGain();
				// gain.gain.value = 1.0;
				// source.connect(gain);
				// gain.connect(ctx.destination);

				source.connect(ctx.destination);
				source.start(0);

				return true;
			} catch (e) {
				console.warn("WebAudio play failed:", e);
				return false;
			}
		}

		// 従来の <audio> 再生（フォールバック）
		function playWithHtmlAudio(audioEl) {
			if (!audioEl) return;
			try {
				audioEl.currentTime = 0; // 毎回頭から
				const p = audioEl.play();
				// iOSでは play() が Promise を返す。失敗してもアプリを止めない
				if (p && typeof p.catch === "function") {
					p.catch((e) => console.warn("HTMLAudio play blocked/failed:", e));
				}
			} catch (e) {
				console.warn("HTMLAudio play error:", e);
			}
		}

		// 共通：まず Web Audio API を試し、ダメなら <audio> にフォールバック
		async function playSound(audioEl) {
			const ok = await playWithWebAudio(audioEl);
			if (!ok) playWithHtmlAudio(audioEl);
		}

		// ★ 正解音を鳴らす関数（名前は変更しない）
		window.playCorrectSound = function() {
			if (!correctAudio) return;
			// click などのユーザー操作から呼ばれる前提
			playSound(correctAudio);
		};

		// ★ 不正解音を鳴らす関数（名前は変更しない）
		window.playWrongSound = function() {
			if (!wrongAudio) return;
			playSound(wrongAudio);
		};

		// ★ めくる音を鳴らす関数（名前は変更しない）
		window.playMekuruSound = function() {
			if (!mekuruAudio) return;
			playSound(mekuruAudio);
		};
	});


}