package com.example.app.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.UserAccount;
import com.example.app.mapper.UserAccountMapper;

/**
 * user_account テーブルに関する業務ロジックを担当するサービスクラス。
 *
 * 今回は「ログイン認証（loginId + password のチェック）」だけ実装しておき、
 * 将来、ユーザー登録や更新機能を追加していく想定。
 */
@Service
@Transactional(readOnly = true) // 認証処理は参照系なので readOnly=true 書き込み禁止
public class UserAccountService {

	
	/** user_account テーブルへアクセスするマッパー */
	private final UserAccountMapper userAccountMapper;
	
	
    /**
     * コンストラクタ。
     * Spring が UserAccountMapper を自動注入する。
     */
	public UserAccountService(UserAccountMapper userAccountMapper) {
		this.userAccountMapper = userAccountMapper;
	}
	
	
    /**
     * ログインID からユーザーを1件取得するヘルパーメソッド。
     *
     * @param loginId user_account.login_id に対応する値
     * @return 該当するユーザー、存在しない場合は null
     */
	public UserAccount findByLoginId(String loginId) {
		return userAccountMapper.findByLoginId(loginId);
	}
	
	
    /**
     * ログインID とパスワードを使って認証を行う。
     *
     * ★BCrypt によるハッシュ化を使った実装:
     *   - DB に保存されている password には「BCrypt でハッシュ化された文字列」が入っている前提。
     *   - フォームから送信された生のパスワードと、DB のハッシュ値を
     *     BCrypt#checkpw で比較する。
     *
     * @param loginId    ログインID
     * @param rawPassword フォームから送信された生のパスワード
     * @return 認証成功時: UserAccount / 失敗時: null
     */
	public UserAccount authenticate(String loginId, String rawPassword) {
		
		UserAccount user = userAccountMapper.findByLoginId(loginId);// ログインID からユーザーを取得
		
		
		// ユーザーが存在しない場合は認証失敗
		if(user == null) {
			return null;
		}
		
		
		// DB に保存されているハッシュ化済みパスワード
		String hashedPassword = user.getPassword();
		

        // ★ BCrypt によるパスワードの照合
        //   - rawPassword: フォームから送られてきた「生」のパスワード
        //   - hashedPassword: DB に保存されている「ハッシュ値」
		boolean matches = BCrypt.checkpw(rawPassword, hashedPassword);
		
		if(!matches) {
			return null;// パスワードが一致しなければ認証失敗
		}
		
		return user; // ここまで来たら認証成功
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
