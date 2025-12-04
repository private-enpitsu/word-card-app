package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * アプリのトップページ（メニュー）を表示するコントローラ。
 * URL: /
 *
 * - ログイン前クイズ画面へのリンク
 * - 管理者用単語一覧画面へのリンク
 * などの入口だけをまとめた、シンプルなメニュー画面を想定している。
 */

@Controller
public class HomeController {

	
    /**
     * トップページ（メニュー）を表示する。
     * URL: / （GET）
     *
     * @return 表示するテンプレートの論理名（index）
     */
	@GetMapping("/")
	public String showHome() {
		return "index"; //index.html を表示する
	}
	
	
}
