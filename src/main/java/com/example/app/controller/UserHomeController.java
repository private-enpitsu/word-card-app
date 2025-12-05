package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.domain.UserAccount;


/**
 * ログイン済みユーザー専用の HOME 画面を担当するコントローラ。
 *
 * - URL: GET /user/home
 * - ログインしていない場合は /login/user へリダイレクトする。
 */
@Controller
public class UserHomeController {
	
	
    /**
     * ユーザーHOME画面の表示。
     * ログイン済みでない場合はログイン画面へ戻す。
     */
	@GetMapping("/user/home")
	public String showUserHome(HttpSession session, Model model) {
		
		// セッションから「ログイン中ユーザー情報」を取得
		UserAccount loginUser = (UserAccount) session.getAttribute("loginUser");
		
		// ログインしていない（セッションにユーザー情報がない）場合はログイン画面へ
		if(loginUser == null) {
			return "redirect:/login/user";
		}
		
		// 画面で使いやすいように Model にも積んでおく
		model.addAttribute("loginUser", loginUser);
		
		return "user/home"; //user/home.html を表示
	}
	
	
	
	
	
	
	
}
