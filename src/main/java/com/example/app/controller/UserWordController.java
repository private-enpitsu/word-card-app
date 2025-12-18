package com.example.app.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.UserAccount;
import com.example.app.domain.Word;
import com.example.app.service.WordService;

/**
 * ユーザー向けの「単語一覧（検索＋ページネーション）」画面を扱うコントローラ。
 *
 * 管理者用 AdminWordController の一覧表示とほぼ同じロジックだが、
 * - URL をユーザー向けに変更（例: /user/words）
 * - テンプレートを user/word-list.html に変更
 * - 編集・削除などの操作は行わない（閲覧専用）
 */
@Controller
public class UserWordController {
	
	
	/** 単語に関する処理を担当するサービス。 */
    private final WordService wordService;

    /**
     * コンストラクタ。
     * Spring が自動で WordService を注入してくれる。
     */
    public UserWordController(WordService wordService) {
        this.wordService = wordService;
    }

    /**
     * ユーザー用の単語一覧画面を表示する。
     * URL: /user/words （GET）
     *
     * - 検索キーワード（任意）
     * - ページ番号（1 始まり）
     * を受け取り、WordService 経由でページネーション検索を行う。
     *
     * 画面には以下の属性を渡す：
     * - wordList    : 表示する単語のリスト
     * - currentPage : 現在のページ番号
     * - totalPages  : 総ページ数
     * - totalCount  : 全件数（検索条件込み）
     * - keyword     : 検索キーワード（再表示用）
     */
    @GetMapping("/user/words")
    public String showUserWordList(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            HttpSession session,
            Model model) {

		// セッションから「ログイン中ユーザー情報」を取得
		UserAccount loginUser = (UserAccount) session.getAttribute("loginUser");
		
		// ログインしていない（セッションにユーザー情報がない）場合はログイン画面へ
		if(loginUser == null) {
			return "redirect:/login/user";
		}
		
        // 1ページあたりの表示件数（管理者用と合わせて 10 件／ページ）
        int pageSize = 10;

        // キーワード条件での総件数を取得（keyword が null/空なら全件数）
        int totalCount = wordService.countByKeyword(keyword);

        // 総ページ数を計算（0件のときは 1 ページとして扱う）
        int totalPages;
        if (totalCount == 0) {
            totalPages = 1;
            page = 1;
        } else {
            // 切り上げ割り算で総ページ数を計算
            totalPages = (totalCount + pageSize - 1) / pageSize;

            // page が範囲外なら補正（1〜totalPages の範囲に収める）
            if (page < 1) {
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }
        }

        // このページに表示するデータを取得
        List<Word> wordList = wordService.findPageByKeyword(keyword, page, pageSize);

        // テンプレートに渡す
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("wordList", wordList);     // 一覧データ
        model.addAttribute("currentPage", page);      // 現在ページ
        model.addAttribute("totalPages", totalPages); // 総ページ数
        model.addAttribute("totalCount", totalCount); // 全件数
        model.addAttribute("keyword", keyword);       // 検索キーワード（フォーム再表示用）

        // ユーザー用テンプレートを表示
        return "user/word-list";  // templates/user/word-list.html
    }

}
