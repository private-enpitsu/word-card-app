package com.example.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.domain.UserAccount;
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
     * ログイン後ユーザー専用のクイズ画面。
     * URL: /user/quiz （GET）
     *
     * - セッションから "loginUser" を取得（未ログインなら /login/user へリダイレクト）
     * - ログイン前クイズと同じロジックで問題を生成
     * - テンプレートは user/quiz.html を使用
     */
    @GetMapping("/user/quiz")
    public String showUserQuiz(HttpSession session, Model model) {

        // ★ ログインチェック（UserHomeController と同じルール）
        UserAccount loginUser = (UserAccount) session.getAttribute("loginUser");
        if (loginUser == null) {
            // 未ログイン → ユーザー用ログイン画面へ
            return "redirect:/login/user";
        }

        // ログインユーザー情報は必要に応じて画面で表示できるよう Model にも積んでおく
        model.addAttribute("loginUser", loginUser);

        // ===== ここからクイズの問題生成ロジック（/quiz と同じ処理） =====

        // ランダムな1件を「問題」として取得
        Word questionWord = wordService.getRandomWordForQuiz();

        // 正解の日本語
        String correctJapanese = questionWord.getJapanese();

        // 誤答候補（3件）を取得
        List<String> wrongList = wordService.getWrongAnswersForQuiz(questionWord.getId(), 3);

        // 正解＋誤答候補をまとめて4択リストにする
        List<String> choices = new ArrayList<>();
        choices.add(correctJapanese);   // 正解
        choices.addAll(wrongList);      // 誤答候補を追加

        // 選択肢をシャッフルしてランダムな順番にする
        Collections.shuffle(choices);

        // 画面に渡す
        model.addAttribute("questionWord", questionWord);        // 問題の単語（英語など）
        model.addAttribute("choices", choices);                  // 選択肢のリスト
        model.addAttribute("correctJapanese", correctJapanese);  // 正解（hidden で保持する用）

        // ログイン後専用クイズ画面を表示
        return "user/quiz";  // src/main/resources/templates/user/quiz.html
    }

	
}
