package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.AdminAccount;

/**
 * admin_account テーブルへのアクセスを担当する MyBatis マッパーインターフェース。
 *
 * 実際の SQL は対応する XML（AdminAccountMapper.xml）側に記述する。
 * ここでは、ログインID から 1件取得するメソッドだけ定義しておく。
 */
@Mapper
public interface AdminAccountMapper {

    /**
     * ログインID を指定して管理者ユーザーを1件取得する。
     *
     * @param loginId ログインID（admin_account.login_id）
     * @return 該当する管理者ユーザー 1件。存在しない場合は null を返す想定。
     */
	AdminAccount findByLoginId(@Param("loginId") String loginId);
}
