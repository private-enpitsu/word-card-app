package com.example.app.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.AdminAccount;
import com.example.app.domain.UserAccount;
import com.example.app.form.AdminRegisterForm;
import com.example.app.form.UserRegisterForm;
import com.example.app.service.AdminAccountService;
import com.example.app.service.UserAccountService;

/**
 * 登録系（ユーザー登録 / 管理者登録）を担当するコントローラー。
 *
 * URL 例:
 *   - ユーザー登録:
 *       GET  /user/register  … フォーム表示
 *       POST /user/register  … 登録処理
 *   - 管理者登録:
 *       GET  /admin/register … フォーム表示
 *       POST /admin/register … 登録処理
 */

@Controller
public class RegisterController {
	
	
	
    // ==========================
    // フィールド（Service の注入）
    // ==========================
	
	/** ユーザー登録処理を担当するサービス */
	private final UserAccountService userAccountService;
	
	/** 管理者登録処理を担当するサービス */
	private final AdminAccountService adminAccountService;
	
	
	
	
    // ==========================
    // コンストラクタ
    // ==========================
    /**
     * コンストラクタ。
     * Spring が UserAccountService / AdminAccountService を自動注入する。
     */
	public RegisterController(UserAccountService userAccountService,
							AdminAccountService adminAccountService
							) {
        this.userAccountService = userAccountService;
        this.adminAccountService = adminAccountService;
		
	}
	
	
	
    // ==========================
    // ユーザー登録（/user/register）
    // ==========================
    /**
     * ユーザー登録フォームの表示（GET）。
     */
	@GetMapping("/user/register")
	public String showUserRegisterForm(@ModelAttribute("userRegisterForm") UserRegisterForm form) {
		
        // ★現時点では「フォームを表示するだけ」。
        //   バリデーションや完了メッセージなどは後で実装する。
        return "user/register"; //templates/user/register.html
	}
	
	
    /**
     * ユーザー登録フォームの送信処理（POST）。
     *
     * @param form 送信されたユーザー登録フォーム
     * @return 登録後に表示するビュー名（後で仕様に合わせて変更）
     */
	@PostMapping("/user/register")
	public String registerUser(@Valid
								@ModelAttribute("userRegisterForm") UserRegisterForm form,
								Errors errors
								) {
		
		// 1. アノテーションによるバリデーション結果の確認
		if(errors.hasErrors()) {
			return "user/register";// 入力エラーがある場合は、同じ画面を再表示
		}
		
		
		// 2. パスワードと確認用パスワードの一致チェック
		String password = form.getPassword();
		String passwordConfirm = form.getPasswordConfirm();
		
		
        if (password != null && !password.equals(passwordConfirm)) {
            // passwordConfirm フィールドにエラーを付与
            errors.rejectValue(
                    "passwordConfirm",
                    "UserRegisterForm.passwordConfirm.mismatch", // エラーコード（任意）
                    "パスワードが一致しません。");

            return "user/register";
        }
		
        
        // 3. フォーム → ドメイン(UserAccount) への詰め替え
        UserAccount user = new UserAccount();
        user.setLoginId(form.getLoginId());
        user.setPassword(form.getPassword()); // ★ 生パスワードのまま渡す（Service でハッシュ化）
        user.setName(form.getName());
        
        
     // 4. 登録処理（Service 内で BCrypt によるハッシュ化＆INSERT）
        userAccountService.register(user);
        
		return "redirect:/login-user"; // 5. 登録完了後はユーザーログイン画面へリダイレクト
	}
	
	
	
	
	
	
    // ==========================
    // 管理者登録（/admin/register）
    // ==========================

    /**
     * 管理者登録フォームの表示（GET）。
     */
    @GetMapping("/admin/register")
    public String showAdminRegisterForm(@ModelAttribute("adminRegisterForm") AdminRegisterForm form) {

        // ★現時点では「フォームを表示するだけ」。
        return "admin/register";  // ※ templates/admin/register.html を表示
    }

    /**
     * 管理者登録フォームの送信処理（POST）。
     *
     * @param form 送信された管理者登録フォーム
     * @return 登録後に表示するビュー名（後で仕様に合わせて変更）
     */
    @PostMapping("/admin/register")
    public String registerAdmin(@Valid 
    							@ModelAttribute("adminRegisterForm") AdminRegisterForm form,
					    		Errors errors
					    		) {

    	// 1. アノテーションによるバリデーション結果の確認
        if (errors.hasErrors()) {
            return "admin/register";
        }

        // 2. フォーム → ドメイン(AdminAccount) への詰め替え
        AdminAccount admin = new AdminAccount();
        admin.setLoginId(form.getLoginId());
        admin.setPassword(form.getPassword()); // ★ Service 側でハッシュ化する想定
        admin.setName(form.getName());

        // 3. 登録処理（★ここから先は推測になるため、Service 呼び出しはコメントに留めています）
        //
        // ★ AdminAccountService の中身が見えていないため、
        //   どのメソッドを呼ぶかを確定できません。
        //   例としては次のようなイメージになりますが、現時点ではコメントのままにしてあります。
        //
        // adminAccountService.register(admin);
        //
        // 実際に管理者登録も DB まで行いたい場合は、
        // AdminAccountService.java の内容を貼っていただければ、
        // それに合わせてここを仕上げます。

        // 4. とりあえずは、管理者登録フォームに戻る or ログイン画面へリダイレクトの仕様を決める必要がありますが、
        //    今回は「A-1: /login-admin へリダイレクト」という仕様でした。
        return "redirect:/login-admin";
    }
	
	

}
