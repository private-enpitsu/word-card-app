package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Word;
import com.example.app.mapper.WordMapper;

/**
 * word テーブルに関する業務ロジックを担当するサービスクラス。
 * 今は「全件取得」など、単純な処理だけを持たせている。
 */



@Service  // Spring 管理のサービスクラスとして登録
@Transactional(readOnly = true) // このクラスの public メソッドは全部「読取専用トランザクション」で動かすという「デフォルト設定」になります。書き込みメソッドは「例外扱い」にしたい。
public class WordService {
	
	/** word テーブルにアクセスするためのマッパー。コンストラクタインジェクションで受け取る。 */
	private final WordMapper wordMapper;
	
	
    /**
     * コンストラクタ。
     * Spring が自動で WordMapper を注入してくれる。
     */
	public WordService(WordMapper wordMapper) {
		this.wordMapper = wordMapper;
	}

	
    /**
     * word テーブルの全件を取得する。
     * 管理者画面やテスト用に利用する想定。
     *
     * @return すべての単語リスト
     */
	public List<Word> findAll(){
		return wordMapper.findAll(); // Mapper に処理を委譲するだけの薄いメソッド
	}
	
	
	
    /**
     * 主キー id で1件取得する。
     * @param id 取得したいレコードの主キー
     * @return 該当する Word。存在しない場合は null を返す想定。
     */
	public Word findById(Long id) {
		return wordMapper.findById(id);
	}
	
	
	
	
    /**
     * 全レコード件数を取得する（ページネーション用）。
     * @return word テーブルの全件数
     */
	public int countAll() {
		return wordMapper.countAll();
	}
	
    /**
     * 指定ページのデータを取得する（ページネーション用）。
     *
     * @param page 1 始まりのページ番号（1ページ目=1）
     * @param size 1ページあたりの件数
     * @return 指定ページに表示する Word のリスト
     */
	public List<Word> findPage(int page, int size){
		
		
		// ページ番号が 1 未満の場合は 1 に矯正する
		if(page < 1) {
			page = 1;
		}
		
		// 先頭から何件スキップするかを計算 (0, size, 2*size, ...)ページネーション何ページ目か
		int offset = (page - 1) * size;
		
		// Mapper に offset/limit 指定で取得を依頼
		return wordMapper.findPage(offset, size);
		
		
	}
	
	
	
    /**
     * 検索キーワード付きでの件数を取得する。
     * keyword が null/空白のみ の場合は全件数を返す。
     */
	public int countByKeyword(String keyword) {
		
		// null や 空白だけ → 条件なし（全件）
		if(keyword == null || keyword.isBlank()) {
			return wordMapper.countAll();
		}
		
		// 前後の空白をカットしたうえで検索に使う
		String trimmed = keyword.trim();
		return wordMapper.countByKeyword(trimmed);
		
	}
	
    /**
     * 検索キーワード付きで、指定ページのデータを取得する。
     *
     * @param keyword 検索キーワード（null/空は条件なし）
     * @param page    1 始まりのページ番号
     * @param size    1ページあたりの件数
     * @return 該当ページの Word 一覧
     */
	public List<Word> findPageByKeyword(String keyword, int page, int size){
	
		
		// クエリ文字の脆弱性対策
		// ページ番号が 1 未満なら 1 に矯正
		if(page < 1) {
			page = 1;
		}
		
		// 先頭から何件スキップするかを計算 (0, size, 2*size, ...)
		int offset = (page - 1) * size;
		
		
		// keyword が null/空白のみ → 通常のページ取得（条件なし）
		if(keyword == null || keyword.isBlank()) {
			return wordMapper.findPage(offset, size);
		}
		
		String trimmed = keyword.trim();
		return wordMapper.findPageByKeyword(trimmed, offset, size);
		
		
	}
	
	
	
	
	
	
	
    /**
     * 単語を新規登録する。
     * @param word  登録したい単語（id は null を想定）
     */
	@Transactional(readOnly = false) // 新規登録なのでクラスの readOnly=true を上書きして、書き込みトランザクションにする
    public void create(Word word) {
		wordMapper.insert(word);	// Mapper に INSERT を依頼するだけの薄いメソッド
	}
	
	
	
    /**
     * 単語を更新する。
     * @param word 更新内容（id で更新対象を特定する）
     */
	@Transactional(readOnly = false)	// 更新処理なので書き込みトランザクションにする
	public void update(Word word) {
		wordMapper.update(word);	// Mapper に UPDATE を依頼するだけの薄いサービスメソッド
	}
	
	
	
    /**
     * 単語を削除する。
     * @param id 削除したいレコードの主キー
     */
	@Transactional(readOnly = false) // 削除は書き込み系なので readOnly=false にする
	public void delete(Long id) {
		wordMapper.deleteById(id);	// Mapper に削除処理を依頼するだけの薄いサービスメソッド
	}
	
	
	
	
    // ==========================
    // クイズ用のメソッド
    // ==========================

    /**
     * クイズ用: ランダムに1件の単語を取得する。
     * Controller からはこのメソッドを呼ぶだけでよい。
     *
     * @return ランダムに選ばれた Word 1件
     */
	public Word getRandomWordForQuiz() {
		return wordMapper.selectRandomWord();
	}


    /**
     * クイズ用: 指定した単語(id)以外から、誤答候補の日本語を複数取得する。
     *
     * @param correctWordId 正解の Word の id
     * @param wrongCount    欲しい誤答候補の件数（例: 3）
     * @return 誤答候補の日本語リスト
     */
	public List<String> getWrongAnswersForQuiz(Long correctWordId, int worngCount){
		return wordMapper.selectRandomWrongAnswers(correctWordId, worngCount);
	}


}
