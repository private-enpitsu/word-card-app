// quiz-sound.js

// 画面の読み込みがすべて終わってから処理を実行する
document.addEventListener("DOMContentLoaded", function () {
  // ★ HTML の <audio> を取得
  const correctAudio = document.getElementById("correctSound");
  const wrongAudio   = document.getElementById("wrongSound");
  const mekuruAudio   = document.getElementById("mekuruSound");

  // ★ 正解音を鳴らす関数
  window.playCorrectSound = function () {
    if (!correctAudio) return;
    correctAudio.currentTime = 0;  // 毎回頭から再生する
    correctAudio.play();
  };

  // ★ 不正解音を鳴らす関数
  window.playWrongSound = function () {
    if (!wrongAudio) return;
    wrongAudio.currentTime = 0;    // 毎回頭から再生する
    wrongAudio.play();
  };
  
  // ★ めくる音を鳴らす関数
  window.playMekuruSound = function () {
    if (!mekuruAudio) return;
    mekuruAudio.currentTime = 0;    // 毎回頭から再生する
    mekuruAudio.play();
  };
  
  
});
