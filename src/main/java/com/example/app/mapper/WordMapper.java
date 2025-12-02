package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Word;


/**
 * word テーブルへのアクセスを定義する MyBatis マッパーインターフェース。
 * 実際の SQL は XML マッパーファイル（WordMapper.xml）側に記述する。
 */

@Mapper
public interface WordMapper {
	
	
    /**
     * 主キー id を指定して 1件取得する。
     * @param id 取得したいレコードの主キー
     * @return 該当する Word。存在しない場合は null を返す想定。
     */
	Word findById(Long id);
	
	
    /**
     * 全件取得する。管理者用一覧のベースとして利用予定。
     * 件数が増えてきた場合は、別途ページネーション用メソッドを用意する。
     * @return すべての Word のリスト
     */
	List<Word> findAll();
	
	
    /**
     * 新規レコードを追加する。
     * @param word 追加したいデータ（id は null を想定）
     * @return 影響を受けた行数（通常は 1）
     */
	int insert(Word word);
	
	
    /**
     * 既存レコードを更新する。
     * @param word 更新内容（id で更新対象を特定する）
     * @return 影響を受けた行数（通常は 1）
     */
	int update(Word word);
	
	
    /**
     * 主キー id を指定して 1件削除する。
     * @param id 削除したいレコードの主キー
     * @return 影響を受けた行数（通常は 1）
     */
	int deleteById(Long id);
		

}
