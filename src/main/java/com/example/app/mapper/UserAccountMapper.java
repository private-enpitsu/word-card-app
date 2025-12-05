package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.UserAccount;

/**
 * user_account テーブルへのアクセスを担当する MyBatis マッパーインターフェース。
 *
 * 実際の SQL は対応する XML（UserAccountMapper.xml）側に記述する。
 * ここでは、ログインID から 1件取得するメソッドだけ定義しておく。
 */
@Mapper
public interface UserAccountMapper {

	
    /**
     * ログインID を指定してユーザーを1件取得する。
     *
     * @param loginId ログインID（user_account.login_id）
     * @return 該当するユーザー 1件。存在しない場合は null を返す想定。
     */
	UserAccount findByLoginId(@Param("loginId") String loginId);
}
