package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.AdminAccount;
import com.example.app.domain.UserAccount;
import com.example.app.service.AdminAccountService;
import com.example.app.service.UserAccountService;



/**
 * ログイン画面の表示だけを担当するコントローラ。
 *
 * - ユーザー用ログイン画面（/login/user）
 * - 管理者用ログイン画面（/login/admin）
 *
 * ★ポイント
 *  いまは「画面を表示するだけ」で、ログイン処理（認証）はまだ実装しません。
 *  次のステップで POST 処理や Service を追加していきます。
 */
@Controller
public class LoginController {

	
	// ユーザー認証用サービスを受け取るフィールド
	private final UserAccountService userAccountService;
	// 管理者認証用サービスを受け取るフィールド
	private final AdminAccountService adminAccountService;
	
	
    /**
     * コンストラクタ。
     * Spring が UserAccountService を自動で注入する。
     */
	public LoginController(UserAccountService userAccountService,
							AdminAccountService adminAccountService
							) {
		this.userAccountService = userAccountService;
		this.adminAccountService = adminAccountService;
	}
	
	
    /**
     * ユーザーログイン処理（フォームからの POST を受け取る）。
     *
     * URL: POST /login/user
     *
     * フロー:
     *  1. loginId / password を受け取る
     *  2. UserAccountService#authenticate で認証する
     *  3. 成功時:
     *      - セッションに「ログイン中ユーザー情報」を保存
     *      - ひとまずトップページ（/）にリダイレクト
     *        （将来 /user/home に変更してもよい）
     *  4. 失敗時:
     *      - /login/user?error へリダイレクトしてエラーメッセージ表示
     */
	@PostMapping("/login/user")
	public String loginUser(@RequestParam("loginId") String loginId,
							@RequestParam("password") String password,
							HttpSession session
							) {
		
		// 認証を実行（成功なら UserAccount、失敗なら null）
		UserAccount user = userAccountService.authenticate(loginId, password);
		
		if(user == null) {
			// 認証失敗 → ?error を付けてログイン画面に戻す
			return "redirect:/login/user?error";
		}
		
        // 認証成功 → セッションにログインユーザー情報を保存
        // "loginUser" という属性名で保持しておく（後でログインチェックに使える）
        session.setAttribute("loginUser", user);
        
        return "redirect:/user/home"; //user/homeへ
	}
	
	
    /**
     * 管理者ログイン処理（フォームからの POST を受け取る）。
     *
     * URL: POST /login/admin
     *
     * フロー:
     *  1. loginId / password を受け取る
     *  2. AdminAccountService#authenticate で認証する
     *  3. 成功時:
     *      - セッションに「ログイン中管理者情報」を保存（属性名: "loginAdmin"）
     *      - ひとまず管理者用単語一覧（/admin/words）へリダイレクト
     *  4. 失敗時:
     *      - /login/admin?error へリダイレクトしてエラーメッセージ表示
     */
	@PostMapping("/login/admin")
	public String loginAdmin(@RequestParam("loginId") String loginId,
							@RequestParam("password") String password,
							HttpSession session) {
		
		// 認証を実行（成功なら AdminAccount、失敗なら null）
		AdminAccount admin = adminAccountService.authenticate(loginId, password);
		
		if(admin == null) {
			return "redirect:/login/admin?error"; // 認証失敗 → ?error を付けて管理者ログイン画面に戻す
		}
		
        // 認証成功 → セッションにログイン管理者情報を保存
        // "loginAdmin" という属性名で保持しておく（後でログインチェックに使える）
        session.setAttribute("loginAdmin", admin);
        
        return "redirect:/admin/home"; // 認証成功後は管理者HOMEへリダイレクト
	}
	
	
	
	
	
    /**
     * ユーザー用ログイン画面の表示。
     *
     * URL: GET /login/user
     * 対応テンプレート: templates/login-user.html
     */
	@GetMapping("/login/user")
	public String showUserLoginForm() {
		return "login-user"; // 単にビュー名を返すだけ。Model には何も入れていない。
	}
	
	
	
    /**
     * 管理者用ログイン画面の表示。
     *
     * URL: GET /login/admin
     * 対応テンプレート: templates/login-admin.html
     */
	@GetMapping("/login/admin")
	public String showAdminLoginForm() {
		return "login-admin"; // 単にビュー名を返すだけ。Model には何も入れていない。
	}
	
	
	
    /**
     * ログアウト処理。
     *
     * - セッションを破棄して、ログイン情報などをすべて消す。
     * - 処理後はトップページ（/）へリダイレクトする。
     *
     * ★学習用の簡易実装:
     *   - GET /logout でログアウトしている。
     *   - 本番システムでは「POST でログアウトさせる」など、
     *     CSRF 対策を含めた設計が必要になる。
     */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
        // セッションを完全に破棄して、保持している属性をすべて消す。
        // これにより "loginUser" などのログイン情報も削除される。
		session.invalidate();
		
		return "redirect:/";// トップページへリダイレクト
	}
	
	
	
	
	
}
