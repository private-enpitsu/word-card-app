package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data					// 全フィールドの getter/setter, toString などを Lombok に生成させる
@NoArgsConstructor		// 引数なしコンストラクタ（MyBatis やフレームワーク用）を生成
@AllArgsConstructor		// 全フィールドを引数に取るコンストラクタを生成（テストなどで便利）
public class Word {
	
	
    /** 
     * 主キー。DB 側の BIGINT に対応させるので Long 型を使用する。
     * INSERT 前は null のままにしておき、自動採番で値が入る想定。
     */
	private Long id;
	
	
    /**
     * 英単語。DB の english カラム (VARCHAR(200) NOT NULL) に対応。
     */
	// 英単語は必須、かつ長さ制限を付ける
	@NotBlank(message = "英単語は必須です")
	@Size(max = 200, message = "英単語は200文字以内で入力してください")
	private String english;
	
	
    /**
     * 日本語訳。DB の japanese カラム (VARCHAR(200) NOT NULL) に対応。
     */
	// 日本語も必須＋長さ制限
    @NotBlank(message = "日本語は必須です")
    @Size(max = 200, message = "日本語は200文字以内で入力してください")
	private String japanese;

}
