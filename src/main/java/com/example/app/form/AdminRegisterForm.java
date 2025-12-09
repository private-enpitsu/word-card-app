package com.example.app.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 管理者登録フォーム用の入力値保持クラス。
 *
 * - Controller でこのクラスを受け取り、Service 用の AdminAccount に詰め替える。
 * - Validation アノテーションで、簡易な入力チェックを行う。
 */
public class AdminRegisterForm {

    /** ログインID（管理者用。メールアドレスや任意IDを想定） */
    @NotBlank(message = "ログインIDを入力してください。")
    @Size(max = 100, message = "ログインIDは100文字以内で入力してください。")
    private String loginId;

    /** パスワード（生パスワード） */
    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 6, max = 50, message = "パスワードは6〜50文字で入力してください。")
    private String password;

    
    /** パスワード（確認用）★追加 */
    @NotBlank(message = "確認用パスワードを入力してください。")
    @Size(min = 6, max = 50, message = "確認用パスワードは6〜50文字で入力してください。")
    private String passwordConfirm;
    
    /** 画面表示用の管理者名 */
    @NotBlank(message = "管理者名を入力してください。")
    @Size(max = 100, message = "管理者名は100文字以内で入力してください。")
    private String name;

    // ==========================
    // getter / setter
    // ==========================

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    /** ★追加 */
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    /** ★追加 */
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
