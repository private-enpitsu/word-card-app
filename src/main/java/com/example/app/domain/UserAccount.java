package com.example.app.domain;



/**
 * アプリ利用ユーザー（user_account テーブル）の1行分を表すドメインクラス。
 *
 * - DB テーブル: user_account
 * - 主キー: id
 * - ログインID: login_id
 * - パスワード: password（将来ハッシュを格納する想定）
 */


public class UserAccount {
	
	/** ユーザーID（主キー, AUTO_INCREMENT） */
	private Long id;
	
	
	/** ログインID（メールアドレス等を想定） */
	private String loginId;
	
	
	/** パスワード（学習段階では平文 or 簡易ハッシュ。将来 BCrypt 等を想定） */
	private String password;
	
	
	/** 画面に表示するユーザー名 */
	private String name;
	
	
	/** レコード作成日時 */
	private java.time.LocalDateTime createdAt;
	
	
	/** レコード更新日時 */
	private java.time.LocalDateTime updatedAt;


	
	
    // ==========================
    // getter / setter
    // ==========================
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public java.time.LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(java.time.LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public java.time.LocalDateTime getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
}
