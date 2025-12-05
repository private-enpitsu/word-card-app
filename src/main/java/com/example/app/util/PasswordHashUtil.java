package com.example.app.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 学習用のパスワードハッシュ生成ユーティリティ。
 *
 * - main メソッドを実行して、BCrypt のハッシュ文字列をコンソールに出力する。
 * - その出力を user_account テーブルの password カラムにコピー＆ペーストして使う。
 *
 * 本番システムでは、ユーザー登録時にサービス層でハッシュ化するのが一般的だが、
 * ここでは「先にDBへ手動INSERTするための補助ツール」として用意している。
 */
public class PasswordHashUtil {

    public static void main(String[] args) {
        // ★ ここにハッシュ化したい生パスワードを書く（例: "pass123"）
        String rawPassword = "pass123";

        // BCrypt でハッシュ化（ランダムなソルトを含む）
        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        System.out.println("raw     : " + rawPassword);
        System.out.println("hashed  : " + hashed);
    }
	
}
