package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.domain.AdminAccount;

/**
 * ログイン済み管理者専用の HOME 画面を担当するコントローラ。
 *
 * - URL: GET /admin/home
 * - ログインしていない場合は /login/admin へリダイレクトする。
 */
@Controller
public class AdminHomeController {
	
	
    /**
     * 管理者HOME画面の表示。
     * ログイン済みでない場合はログイン画面へ戻す。
     */
	@GetMapping("/admin/home")
	public String showAdminHome(HttpSession session, Model model) {
		
		// セッションから「ログイン中管理者情報」を取得
		AdminAccount loginAdmin = (AdminAccount) session.getAttribute("loginAdmin");
		
		// ログインしていない場合は管理者ログイン画面へリダイレクト
		if(loginAdmin == null) {
			return "redirect:/login/admin";
		}
		
		// 画面で使いやすいように Model にも積んでおく
		model.addAttribute("loginAdmin", loginAdmin);
		
		return "admin/home"; //admin/home.html を表示
	}
	

}
