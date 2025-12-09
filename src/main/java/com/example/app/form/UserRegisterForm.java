package com.example.app.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ユーザー登録フォーム用の入力値保持クラス。
 *
 * - Controller でこのクラスを受け取り、Service 用の UserAccount に詰め替える。
 * - Validation アノテーションで、簡易な入力チェックを行う。
 */
public class UserRegisterForm {
	


    /** ログインID（メールアドレスを想定） */
    @NotBlank(message = "ログインIDを入力してください。")
    @Email(message = "メールアドレスの形式で入力してください。")
    @Size(max = 100, message = "ログインIDは100文字以内で入力してください。")
    private String loginId;

    /** パスワード（生パスワード） */
    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 6, max = 50, message = "パスワードは6〜50文字で入力してください。")
    private String password;

    /** パスワード（確認用） */
    @NotBlank(message = "確認用パスワードを入力してください。")
    @Size(min = 6, max = 50, message = "確認用パスワードは6〜50文字で入力してください。")
    private String passwordConfirm;

    /** 画面表示用のユーザー名 */
    @NotBlank(message = "ユーザー名を入力してください。")
    @Size(max = 100, message = "ユーザー名は100文字以内で入力してください。")
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

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
