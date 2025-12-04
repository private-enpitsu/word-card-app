package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
		

	
	
	
	
	
	//	●クイズ用
	
	
    /**
     * クイズ用: ランダムに1件の単語を取得する。
     * ORDER BY RAND() LIMIT 1 を使う簡易版。
     *
     * @return ランダムに選ばれた Word 1件
     */
	Word selectRandomWord();
	
	
    /**
     * クイズ用: 正解とは別の誤答候補（日本語）をランダムに複数取得する。
     * - id で指定されたレコード以外から
     * - japanese カラムだけを取り出す
     * - LIMIT で指定件数分だけランダム取得
     *
     * @param id     正解の Word の id（この id の行は除外する）
     * @param limit  欲しい誤答候補の件数（例: 3）
     * @return 誤答候補の日本語リスト
     */
	List<String> selectRandomWrongAnswers(@Param("id") Long id,
										@Param("limit") int limit
										);
	
	
	
	
	
	
	//	●ページネーション用
	
	
    /**
     * 検索付きのページネーション用: キーワード条件での件数を取得する。
     * keyword が null または空文字の場合は、全件数を返す。
     */
	int countByKeyword(@Param("keyword") String keyword);
	
    /**
     * 検索付きのページネーション用:
     * キーワード条件＋offset/limit でデータを取得する。
     * @param keyword 検索キーワード（null/空なら条件なし）
     * @param offset  先頭から何件スキップするか（0, 10, 20, ...）
     * @param limit   1ページあたりの件数
     */
	List<Word> findPageByKeyword(@Param("keyword") String keyword,
								@Param("offset") int offset,
								@Param("limit") int limit			
								);
	
	
	
	
	
	
	
    /**
     * ページネーション用。全件数を取得する。
     * @return word テーブルの全レコード件数
     */
	int countAll();
	
    /**
     * ページネーション用: offset/limit でデータを取得する。
     * @param offset 先頭から何件スキップするか（0, 10, 20, ...）
     * @param limit  取得件数（1ページあたりの件数）
     * @return 指定範囲の Word リスト
     */
	List<Word> findPage(
			@Param("offset") int offset,
			@Param("limit") int limit			
			);
	
	
	
	
	

	
	
	
	
}
