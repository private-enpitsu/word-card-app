package com.example.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.app.domain.Word;
import com.example.app.service.WordService;


/**
 * 開発・動作確認用のテストコントローラ。
 * /words-test にアクセスしたときに、DB から全件取得してコンソールに表示し、
 * ブラウザには簡単なメッセージだけ返す。
 *
 * 本番の画面（クイズや一覧）は、別コントローラで改めて作成する想定。
 */


@Controller
public class WordTestController {
	
	/** Word に関する処理を行うサービス。コンストラクタインジェクションで受け取る。 */
	private final WordService wordService;
	
	
    /**
     * コンストラクタ。
     * Spring が自動で WordService のインスタンスを注入する。
     */
	public WordTestController(WordService wordService) {
		this.wordService = wordService;
	}
	
	
	
    /**
     * 動作確認用エンドポイント。
     * ブラウザで /words-test にアクセスすると実行される。
     *
     * - DB から Word の全件を取得
     * - コンソール(System.out)に中身を出力
     * - ブラウザには件数だけ簡単に表示する
     *
     * @return 簡単なテキストメッセージ（HTML テンプレートはまだ使わない）
     */
	
	@GetMapping("/words-test")
	@ResponseBody	// 戻り値の文字列をそのまま HTTP レスポンスボディとして返す
	public String wordsTest() {
		
		// DB から全件取得
		List<Word> wordList = wordService.findAll();
		
		// 取得した内容をコンソールに出力して確認できるようにする
		System.out.println("=== word テーブルの内容 ===");
		for(Word w : wordList) {
			
			// Word#toString() は Lombok の @Data により自動生成されている
			System.out.println(w);
		}
		System.out.println("=== 件数" + wordList.size() +  " ===");
		
		// ブラウザには、簡単な確認用メッセージだけ返す
		return "word テーブルから" + wordList.size() + "件取得しました。コンソールを確認してください。";
		
	}
	

}
