# Issue #711 計画: RestTemplate の非推奨化対応（RestClient への移行）

## 対象（実測済み: RestTemplate の使用は accountMgmt のみ）

- `backend/src/main/java/undecided/generic/accountMgmt/internal/config/AccountManagementConfig.java`（RestTemplate Bean 定義）
- `backend/src/main/java/undecided/generic/accountMgmt/internal/user/UserQueryImpl.java`（GET `getForEntity`）
- `backend/src/main/java/undecided/generic/accountMgmt/internal/user/UserCommandImpl.java`（GET `getForEntity`、POST `postForEntity` ×2）
- `backend/src/test/java/undecided/generic/accountMgmt/internal/user/UserCommandImplTest.java`（`@Mock RestTemplate`）

## 方針

- 現行にリトライ設定はない（実測）。新しい振る舞い（リトライ・タイムアウト等）は追加せず、振る舞いを変えない移行に徹する
- Bean は bankReg の先例に倣い名前付き `accountManagementRestClient` とし、注入側のフィールド名も同名にする（RestClient の Bean が複数あるため名前で解決させる）
- ベース URL は現行どおり各 Impl の定数（`http://localhost:8082`）のままとし、Bean に baseUrl は付けない
- 呼び出しは `retrieve()` に `onStatus`（エラーステータスを例外化しない no-op ハンドラ）を付け、`toEntity(...)` で ResponseEntity を得る形にする。現行コードは ResponseEntity のステータスで分岐しており（POST の非 2xx で固有メッセージの BusinessException、GET の非 2xx で false）、既定のエラーハンドラのままでは 4xx/5xx で例外が投げられ、この分岐と既存テストの期待メッセージが壊れるため
- テストは Mockito のモック検証から、MockRestServiceServer で実物の RestClient を検証する形に変える（最終的な振る舞いの表明にするため）

## テストリスト（small test）

UserCommandImplTest（書き換え）:

1. 初回ログイン状態が true のとき、/first-login-password へ POST してパスワードを変更することとなる
2. 初回ログイン状態が false のとき、/password へ POST してパスワードを変更することとなる
3. 新しいパスワードが 7 文字のとき、BusinessException（8文字以上）となる
4. 新しいパスワードが 65 文字のとき、BusinessException（64文字以下）となる
5. 新しいパスワードと確認用パスワードが一致しないとき、BusinessException（一致しません）となる
6. 初回ログイン時パスワード変更の POST が 400 を返すとき、BusinessException（初回ログイン時のパスワード変更に失敗しました）となる

UserQueryImplTest（新規）:

7. 初回ログイン状態が true を返すとき、needsPasswordChange は true となる
8. 初回ログイン状態が false を返すとき、needsPasswordChange は false となる
9. authorization サービスが 500 を返すとき、needsPasswordChange は false となる
10. authorization サービスへ接続できないとき、needsPasswordChange は false となる
11. ユーザー名で検索したとき、リポジトリの検索結果がそのまま返ることとなる

## 手順

1. write_tests: テストを RestClient 前提に書き換え・新規作成し、Red を実測する（現行コンストラクタは RestTemplate 前提のためコンパイルエラーが Red の証跡）
2. implement: AccountManagementConfig と Impl 2 件を RestClient 化する
3. 検証: 対象テストと accountMgmt の small test 全体を `--no-build-cache` で実測する
4. コミットを test: / feat: に分け、push して PR を作る（マージはオーナー判断）
