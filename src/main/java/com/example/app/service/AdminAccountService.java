package com.example.app.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.AdminAccount;
import com.example.app.mapper.AdminAccountMapper;

/**
 * admin_account テーブルに関する業務ロジックを担当するサービスクラス。
 *
 * 今回は「管理者ログイン認証（loginId + password のチェック）」だけ実装しておき、
 * 将来、管理者登録や変更機能を追加していく想定。
 */
@Service
@Transactional(readOnly = true) // 認証処理は参照系なので readOnly=true 読み取り専用
public class AdminAccountService {
	
	/** admin_account テーブルへアクセスするマッパー */
	private final AdminAccountMapper adminAccountMapper;
	
    /**
     * コンストラクタ。
     * Spring が AdminAccountMapper を自動注入する。
     */
	public AdminAccountService(AdminAccountMapper adminAccountMapper) {
		this.adminAccountMapper = adminAccountMapper;
	}
	
    /**
     * ログインID とパスワードを使って管理者認証を行う。
     *
     * - DB の password には「BCrypt でハッシュ化された文字列」が入っている前提。
     * - フォームから送信された生パスワードと、DB のハッシュを BCrypt.checkpw で比較する。
     *
     * @param loginId     ログインID
     * @param rawPassword フォームから送信された生のパスワード
     * @return 認証成功時: AdminAccount / 失敗時: null
     */
	public AdminAccount authenticate(String loginId, String rawPassword) {
		
		// ログインID から管理者ユーザーを取得
		AdminAccount admin = adminAccountMapper.findByLoginId(loginId);
		
		// ユーザーが存在しない場合は認証失敗
		if(admin == null) {
			return null;
		}
		
		// DB に保存されているハッシュ化済みパスワード
		String hashedPassword = admin.getPassword();
		
		// BCrypt でパスワードを照合
		boolean matches = BCrypt.checkpw(rawPassword, hashedPassword);
		
		if(!matches) {
			return null; // パスワード不一致 → 認証失敗
		}
		
		return admin; // ここまで来たら認証成功
	}
	
	

}
