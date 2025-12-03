package com.example.app.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Word;
import com.example.app.service.WordService;







@Controller
public class AdminWordController {

	
	private final WordService wordService; /** 単語に関する処理を担当するサービス。 */
	
	
    /**
     * コンストラクタ。
     * Spring が自動で WordService を注入してくれる。
     */
	public AdminWordController(WordService wordService) {
		this.wordService = wordService;
	}
	
	
	
    /**
     * 管理者用の単語一覧画面を表示する。
     * URL: /admin/words （GET）
     *
     * - DB から全件取得
     * - Model に "wordList" という名前で詰める
     * - Thymeleaf テンプレート "admin/word-list.html" を表示する
     *
     * @param model 画面に値を渡すための入れ物
     * @return 表示するテンプレートの論理名
     * 
     * ●ページネーション付きに編集
     */
	@GetMapping("/admin/words")
	public String showWordList(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			Model model) {
		
		
		// 1ページあたりの表示件数（必要に応じて変更可）
		int pageSize = 100;
		
		// 全件数を取得
		int totalCount = wordService.countAll();
		
		// 総ページ数を計算（0件のときは 1 ページとして扱う）
		int totalPages;
		
		if(totalCount == 0) {
			totalPages = 1;
			page = 1;
		} else {
			// 切り上げ割り算: (totalCount + pageSize - 1) / pageSize
            totalPages = (totalCount + pageSize - 1) / pageSize;
            
            // page が範囲外なら補正（1〜totalPages の範囲に収める）
            if (page < 1) {
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }
		}
		
		// このページに表示するデータを取得
		List<Word> wordList = wordService.findPage(page, pageSize);
		
        // テンプレートに渡す
        model.addAttribute("wordList", wordList);    // 一覧データ
        model.addAttribute("currentPage", page);     // 現在ページ
        model.addAttribute("totalPages", totalPages);// 総ページ数
        model.addAttribute("totalCount", totalCount);// 全件数
		
//		List<Word> wordList = wordService.findAll();	// DB から単語の全件を取得（現時点では絞り込みなし）
		
//		model.addAttribute("wordList", wordList);	// テンプレートに渡すため、Model に格納
		
		return "admin/word-list";	// src/main/resources/templates/admin/word-list.html を表示する
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    /**
     * 単語の新規登録画面を表示する。
     * URL: /admin/words/new （GET）
     *
     * - 空の Word インスタンスを用意して Model に詰める
     * - admin/word-form.html を表示する
     */
	@GetMapping("/admin/words/new")
	public String showCreateForm(Model model) {
		
		model.addAttribute("word", new Word());	// フォームの初期表示用に、空の Word オブジェクトを渡す
		
		return "admin/word-form";
	}
	
	
	
	
    /**
     * 単語の新規登録処理を行う。
     * URL: /admin/words （POST）
     *
     * - フォームから送信された Word を受け取る
     * - サービス経由で DB に INSERT する
     * - 処理後は一覧画面にリダイレクトする
     */
	@PostMapping("/admin/words")
	public String create(@Valid @ModelAttribute("word") Word word,
						Errors errors) {
		
		// バリデーションエラーがある場合は、登録せずフォーム再表示
		if(errors.hasErrors()) { // エラー情報は errors 経由でテンプレートに渡される
			return "admin/word-form";
		}
		
		wordService.create(word);	// エラーがなければ、サービスを通じて新規登録
		
		return "redirect:/admin/words";
	}
	
	
	
    /**
     * 単語の編集画面を表示する。
     * URL: /admin/words/{id}/edit （GET）
     *
     * - パスの {id} から対象レコードを取得
     * - Model に "word" として詰める
     * - 新規登録と同じ admin/word-form.html を使って表示する
     */
	@GetMapping("/admin/words/{id}/edit")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		
		Word word = wordService.findById(id);	// id から対象の Word を取得
		
        // 本来は null チェックして 404 やエラーページに飛ばす処理を入れることも多いが、
        // 今回は学習のため、存在する前提でシンプルに扱う。
		model.addAttribute("word", word);
		
		return "admin/word-form";	// 新規と同じフォームテンプレートを使い回す
	}
	
	
	
    /**
     * 単語の更新処理を行う。
     * URL: /admin/words/{id} （POST）
     *
     * - フォームから送信された Word を受け取り
     * - サービス経由で UPDATE
     * - 処理後は一覧画面にリダイレクト
     */
	@PostMapping("/admin/words/{id}")
	public String update(@PathVariable("id") Long id, //URL の パスの一部 {id} を引数に受け取るためのアノテーションです。
						@Valid @ModelAttribute("word") Word word,
						Errors errors) { //フォームから送信された値を詰め込んだ Word wordオブジェクトを引数("word")で受け取る
		
		word.setId(id);	// 念のため、パスの id をフォームの id に反映しておく（改ざん対策の意味もある）
		
        // バリデーションエラーがある場合は、更新せずフォーム再表示
        if (errors.hasErrors()) {
            return "admin/word-form";
        }
		
		wordService.update(word);	// 更新処理
		
		return "redirect:/admin/words";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    /**
     * 単語を削除する。
     * URL: /admin/words/{id}/delete （GET）
     *
     * - パスの {id} から削除対象を特定
     * - サービス経由で delete を実行
     * - 処理後は一覧画面にリダイレクト
     *
     * ※ 学習用にシンプルな GET 削除にしている。
     *    実運用では CSRF 対策などを考えて POST/DELETE + 確認画面にすることが多い。
     */
	@GetMapping("/admin/words/{id}/delete")
	public String delete(@PathVariable("id") Long id) {
		
		wordService.delete(id);	// 指定された id のレコードを削除
		
		return "redirect:/admin/words";	// 削除後は一覧画面へ戻る
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
