package com.example.app.controller;

import java.util.Locale;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.UserAccount;
import com.example.app.domain.Word;
import com.example.app.service.WordService;

import lombok.RequiredArgsConstructor;

/**
 * ログイン後ユーザー向けの「日本語 → 英語を入力するクイズ」画面を担当するコントローラ。
 *
 * URL:
 *   - GET  /user/input-quiz : 問題の初期表示（ランダム1問）
 *   - POST /user/input-quiz : 入力された英単語の判定結果を表示（同じテンプレート）
 *
 * テンプレート:
 *   - src/main/resources/templates/user/input-quiz.html
 */
@Controller
@RequiredArgsConstructor
public class UserInputQuizController {

    /** 単語取得などの処理を行うサービス。コンストラクタインジェクションで受け取る。 */
    private final WordService wordService;

    /**
     * クイズ画面の初期表示。
     * - ログインチェック
     * - ランダムに1件の単語を取得
     * - questionWord（日本語出題用）と wordId（判定用）を Model に積む
     *
     * @param session HTTPセッション（ログインユーザー確認用）
     * @param model   画面に値を渡すためのモデル
     * @return user/input-quiz テンプレート名
     */
    @GetMapping("/user/input-quiz")
    public String showInputQuiz(HttpSession session, Model model) {

        // セッションからログイン中ユーザーを取得
        UserAccount loginUser = (UserAccount) session.getAttribute("loginUser");
        if (loginUser == null) {
            // 未ログインならユーザー用ログイン画面へリダイレクト
            return "redirect:/login/user";
        }
        model.addAttribute("loginUser", loginUser);

        // ランダムに1件の単語を取得（問題として使用）
        Word questionWord = wordService.getRandomWordForQuiz();

        // 万一、単語が1件も登録されていない場合の簡易ガード
        if (questionWord == null) {
            // このメッセージはテンプレート側で th:if などで表示する想定
            model.addAttribute("errorMessage", "問題に使用できる英単語が登録されていません。");
            return "user/input-quiz";
        }

        // 画面で出題に使う情報を Model に積む
        // - questionWord.japanese を日本語の出題文として利用
        // - wordId は POST 時の判定用（hidden で送信）
        model.addAttribute("questionWord", questionWord);
        model.addAttribute("wordId", questionWord.getId());

        // 入力欄の初期値（null のままでもよいが、テンプレ側で扱いやすいように空文字を渡しておく）
        model.addAttribute("userAnswer", "");

        return "user/input-quiz";
    }

    /**
     * ユーザーが入力した英単語の答え合わせを行う。
     *
     * 仕様:
     * - 空文字の場合：「入力してください」というメッセージを表示（判定は行わない）
     * - 前後の空白（半角/全角）は取り除いてから比較する
     * - 大文字小文字は区別しない（case-insensitive）
     * - 単語の途中に含まれるスペースはそのまま比較に使う（例: "New York" など）
     *
     * @param session HTTPセッション（ログインユーザー確認用）
     * @param model   画面に値を渡すためのモデル
     * @param wordId  出題に使用した Word のID（hiddenから送信される想定）
     * @param answer  ユーザーの入力値（テキストボックスから送信）
     * @return user/input-quiz テンプレート名
     */
    @PostMapping("/user/input-quiz")
    public String checkInputQuiz(
            HttpSession session,
            Model model,
            @RequestParam("wordId") Long wordId,
            @RequestParam("answer") String answer) {

        // セッションからログイン中ユーザーを取得
        UserAccount loginUser = (UserAccount) session.getAttribute("loginUser");
        if (loginUser == null) {
            // 未ログインならユーザー用ログイン画面へリダイレクト
            return "redirect:/login/user";
        }
        model.addAttribute("loginUser", loginUser);

        // 判定対象となる正解の単語を取得
        Word questionWord = wordService.findById(wordId);

        // 万一、id に対応するレコードが見つからなかった場合
        if (questionWord == null) {
            // システム的な異常に近いので、簡易メッセージ＋新規問題出題への誘導
            model.addAttribute("errorMessage", "問題の単語が取得できませんでした。もう一度お試しください。");
            return "redirect:/user/input-quiz";
        }

        // 画面側で再表示するために、問題の日本語と wordId を積む
        model.addAttribute("questionWord", questionWord);
        model.addAttribute("wordId", questionWord.getId());

        // ユーザー入力値は、後で再表示できるようにそのまま Model にも保持しておく
        model.addAttribute("userAnswer", answer);

        // ==== 入力チェック（空文字かどうか） ====
        String normalizedInput = normalizeForCompare(answer);

        if (normalizedInput.isEmpty()) {
            // 入力が実質的に空（空文字 or 空白のみ）の場合は、まず入力を促す
            model.addAttribute("errorMessage", "英単語を入力してください。");
            // 正解・不正解の判定結果はまだ出していないので resultMessage は設定しない
            return "user/input-quiz";
        }

        // ==== 正解の英単語も同様に正規化して比較 ====
        String normalizedCorrect = normalizeForCompare(questionWord.getEnglish());

        boolean isCorrect = normalizedInput.equals(normalizedCorrect);

        if (isCorrect) {
            // 正解の場合のメッセージ
            model.addAttribute("resultMessage", "正解です！");
            model.addAttribute("isCorrect", true);
        } else {
            // 不正解の場合のメッセージ（正解の英単語も表示）
            String message = "不正解です。" + " - - " + "答え：" + questionWord.getEnglish();
            model.addAttribute("resultMessage", message);
            model.addAttribute("isCorrect", false);
        }

        // ※「次の問題へ」ボタンはテンプレート側で
        //    <a th:href="@{/user/input-quiz}">次の問題へ</a>
        // などとして GET に飛ばす想定。
        return "user/input-quiz";
    }

    /**
     * 英単語のユーザー入力と正解を比較するための正規化関数。
     *
     * - null の場合は空文字に変換
     * - 全角スペース（\u3000）を半角スペースに変換
     * - 前後の空白（半角/全角）を除去
     * - 大文字小文字を区別しない比較のために小文字化
     *
     * 単語の途中に含まれるスペースはそのまま保持する。
     *
     * @param src 元の文字列（ユーザー入力 or 正解の英単語）
     * @return 比較用に正規化された文字列
     */
    private String normalizeForCompare(String src) {
        if (src == null) {
            return "";
        }

        // 全角スペースを半角スペースに変換
        String replaced = src.replace('\u3000', ' ');

        // 前後の空白（半角スペース、タブ、改行など）を除去
        String trimmed = replaced.trim();

        // 大文字小文字を区別せずに比較するため、小文字へ変換
        return trimmed.toLowerCase(Locale.ROOT);
    }
}
