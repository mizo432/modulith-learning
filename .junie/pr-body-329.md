## 概要

Issue #329 の一部として、Userエンティティの `toString()`
メソッドで機微な情報（氏名・パスワード・メール）がログに漏洩しないようマスク処理を実装しました。

## 変更内容

- **User.java**
    - `@ToString` Lombok アノテーションを削除
    - `firstName`、`lastName`、`password` フィールドに `@ToString.Exclude` を追加
    - カスタム `toString()` を実装し、機微情報をマスクする
        - 氏名: 最初の文字 + `*`（例: "太郎" → "太*"）
        - メール: 最初の文字 + `***@ドメイン`（例: "taro@example.com" → "t***@example.com"）
        - パスワード: 出力から除外
- **UserTest.java**
    - `toString()` のテストを追加（マスク処理、null 対応、空文字対応）

## 関連 Issue

Closes #329
