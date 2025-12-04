package com.example.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.domain.Word;
import com.example.app.service.WordService;



/**
 * ログイン前の4択クイズ画面を担当するコントローラ。
 *
 * URL:
 *   - GET  /quiz         : クイズ1問を表示
 *   - POST /quiz/answer  : 回答を受け取り、結果メッセージ＋次の問題を表示
 */


@Controller
public class QuizController {
	
	/** Word に関する処理を行うサービス。コンストラクタインジェクションで受け取る。 */
	private final WordService wordService;
	
	
    /**
     * コンストラクタ。
     * Spring が自動で WordService のインスタンスを注入する。
     */
	public QuizController(WordService wordService) {
		this.wordService = wordService;
	}
	
	
	
	
	
    /**
     * クイズ画面の初期表示。
     * URL: /quiz （GET）
     *
     * - ランダムに1件の単語を取得
     * - 正解の日本語＋誤答候補3件を混ぜて4択を作成
     * - quiz.html テンプレートを表示
     */
	@GetMapping("/quiz")
	public String showQuiz(Model model) {
		
		
		// ランダムな1件を「問題」として取得
		Word questionWord = wordService.getRandomWordForQuiz();
		
		
		// 正解の日本語
		String correctJapanese = questionWord.getJapanese();
		
		
		// 誤答候補（3件）を取得
		List<String> wrongList = wordService.getWrongAnswersForQuiz(questionWord.getId(), 3);
		
		
		// 正解＋誤答候補をまとめて4択リストにする
		List<String> choices = new ArrayList<>();
		choices.add(correctJapanese);	// 正解
		choices.addAll(wrongList);		// 誤答候補を追加
		
		
		// 選択肢をシャッフルしてランダムな順番にする
		Collections.shuffle(choices);
		
		
		// 画面に渡す
		model.addAttribute("questionWord",questionWord);		// 問題の単語（英語など）
		model.addAttribute("choices",choices);					// 選択肢のリスト
		model.addAttribute("correctJapanese",correctJapanese);	// 正解（hidden で保持する用）
		
		
		// 初回表示なので結果メッセージはなし（null のまま）
		return "quiz"; //quiz.html を表示
	}
	
	
	
	
	
    /**
     * クイズの回答を受け取り、結果を表示する。
     * URL: /quiz/answer （POST）
     *
     * - hidden で送られてきた「正解の日本語」と、ユーザーの選択を比較
     * - 正解/不正解のメッセージを作る
     * - ついでに次の問題も同時に表示する（/quiz と同じ処理を再利用）
     */
//	@PostMapping("/quiz/answer")
//	public String answerQuiz(@RequestParam("english") String english,			// 問題として出した英単語（表示用）
//							@RequestParam("correctJapanese") String correct,	// 正解の日本語（hiddenで送信されたもの）
//							@RequestParam("selectedChoice") String selected,	// ユーザーが選んだ回答
//							Model model
//							) {
//		
//		// 正解/不正解の判定
//		boolean correctFlag = correct.equals(selected);
//		
//		String resultMessage;
//		if(correctFlag) {
//			resultMessage = "正解！（" + english + " = " + correct +  "）";
//		} else {
//			resultMessage = "不正解！（" + english + " の正解は " + correct +  "）";
//		}
//		
//		
//		// まず結果メッセージを Model に詰める
//        model.addAttribute("lastResultMessage", resultMessage);
//
//        // ★ 次の問題も同時に表示したいので、GET /quiz と同じ処理をここで再利用する
//        //   （コード重複を避けるなら private メソッドに共通化しても良いが、学習用なのでここではシンプルに書く）
//
//        // ランダムな1件を「次の問題」として取得
//        Word questionWord = wordService.getRandomWordForQuiz();
//        String nextCorrectJapanese = questionWord.getJapanese();
//        List<String> wrongList = wordService.getWrongAnswersForQuiz(questionWord.getId(), 3);
//
//        List<String> choices = new ArrayList<>();
//        choices.add(nextCorrectJapanese);
//        choices.addAll(wrongList);
//        Collections.shuffle(choices);
//
//        model.addAttribute("questionWord", questionWord);
//        model.addAttribute("choices", choices);
//        model.addAttribute("correctJapanese", nextCorrectJapanese);
//
//        // 結果メッセージ＋次の問題をまとめて quiz.html に表示
//        return "quiz";
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
