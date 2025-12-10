package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.domain.UserAccount;
import com.example.app.domain.Word;
import com.example.app.service.WordService;

import lombok.RequiredArgsConstructor;



/**
 * ユーザー向けの「英単語カード（単語暗記用）」画面を扱うコントローラ。
 *
 */
@Controller
@RequiredArgsConstructor
public class UserCardController {
	
	/** Word に関する処理を行うサービス。コンストラクタインジェクションで受け取る。 */
	private final WordService wordService;
	
	
	
	
    /**
     * 単語カード画面の初期表示。
     * URL: /user/cards （GET）
     *
     * - ランダムに1件の単語を取得
     * - cards.html テンプレートを表示
     */
	@GetMapping("/user/cards")
	public String showCards(HttpSession session,
							Model model
							) {
		
		
		// セッションから「ログイン中ユーザー情報」を取得
		UserAccount loginUser = (UserAccount) session.getAttribute("loginUser");
		
		// ログインしていない（セッションにユーザー情報がない）場合はログイン画面へ
		if(loginUser == null) {
			return "redirect:/login/user";
		}
		
		
		// ランダムな1件を「問題」として取得
		Word cardWord = wordService.getRandomWordForQuiz();
		
		
		// 画面に渡す
		model.addAttribute("cardWord", cardWord);
		model.addAttribute("loginUser", loginUser);
		
		return "user/cards";
	}
	
}
