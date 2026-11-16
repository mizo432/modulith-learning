# modulith-learning の開発ワークフローに takt を使用する

## ステータス

承認済み（2026-11-16 にモデル選定を変更）

## 日付

2026-11-16

## コンテキスト

Muse・Orca・takt の階層構成による開発環境を MacBook Air で試作している。構成の原則は「制御面は Orca 一本化」であり、製造層（実際にエージェントが作業する層）はプロジェクトごとに **Orca orchestration か takt 工場のどちらか**を選ぶことになっている。

試作プロジェクトである modulith-learning について、製造層をどちらにするか決める必要があった。takt はこの Mac に導入済みである（`/opt/homebrew/bin/takt`、本体のソースも `~/IdeaProjects/takt` にある）。

モデル選定については、当初、試作全体の方針（OpenCode ＋ LM Studio によるローカル実行）に合わせて takt でもローカル LLM を使うと決めた。しかしその後、オーナーから「ローカルで使うことが目的ではない」との判断があり、takt のモデルは Junie Lite に変更した（2026-11-16）。takt には Junie CLI を呼ぶ `junie-lite` プロバイダが組み込み済みであることを、takt のソースで確認している。

## 決定

**modulith-learning の開発ワークフロー（製造層）には takt を使用し、takt のモデルには Junie Lite を使用する。**

- 制御面は Orca 一本化の原則を維持し、takt はその下の製造層として使う
- takt では、組み込みの `junie-lite` プロバイダ経由で Junie CLI を使用する。Junie Lite は JetBrains のクラウド実行（現行は Qwen 3.6 27B）であり、ローカル LLM は使わない
- 前提として、Junie CLI のインストールと認証（JetBrains アカウント）が必要
- takt のワークフロー定義や具体的な運用手順は、試作の中で詰める。決まったものから AGENTS.md 等へ反映する
- 本決定は開発プロセス側のものであり、アプリケーション自体の技術スタック（Spring Boot、Spring Modulith 等）には影響しない

## 結果

### ポジティブな結果

- 製造層の方式がプロジェクトごとに固定され、Orca orchestration との二重運用にならない
- ワークフローを定義として残す形になり、作業の再現や引き継ぎがしやすくなる（想定）
- Junie Lite は無料枠で使え、この Mac のメモリ制約（16GB）を受けない。ローカル 14B 級モデルより実装品質が期待できる

### ネガティブな結果

- takt の学習・運用コストがかかる
- 試作段階のため、ワークフロー定義や手順はまだ未確定で、今後詰める作業が必要
- Junie Lite はクラウド実行のため、コードが JetBrains のクラウドを経由する
- takt の junie-lite プロバイダは構造化出力（outputSchema）・画像入力・MCP・allowedTools・maxTurns に未対応で、指定しても無視される。ワークフロー設計でこの制約を考慮する必要がある
- Junie Lite の中身のモデルは JetBrains 側で予告なく差し替えられ得る。再現性はモデル固定のローカル実行より低い

### 中立的な結果

- Orca 試作の他の要素（制御面、gate、フック）は本決定の影響を受けない
- OpenCode ＋ LM Studio のローカル構成自体は残る（Orca 側・IntelliJ Air 側の選択肢として併存）

## 代替案

### 代替案1：製造層に Orca orchestration を使う

図の構成では対になる選択肢である。今回は試作として takt 工場の方式を採用し、そちらの運用を検証することにしたため、modulith-learning では採用しない。

### 代替案2：takt でローカル LLM を使う（当初案）

当初はこちらを採用したが、「ローカルで使うことが目的ではない」というオーナーの判断により変更した。16GB のメモリ制約（OpenCode ＋ Qwen2.5-Coder-14B で /init がメモリ不足になった実績）も背景にある。

### 代替案3：takt で Junie Lite 以外のクラウドモデルを使う

Junie Lite は無料枠で足りる見込みであり、まずはこちらで運用する。

### 代替案4：どちらも使わず手作業でエージェントを操作する

再現性や引き継ぎの面で劣るため採用しない。

## 関連

- `AGENTS.md`（作業上の注意に takt と Junie Lite の使用を明記）
- Muse・Orca・takt 階層ダイアグラム（2026-11-16 にオーナーが共有した構成図）

## 見直しの条件

- 試作で takt が製造層として想定どおり機能しないことが判明した場合（Orca orchestration への切り替えを検討する）
- Junie Lite の制限（構造化出力・MCP 未対応、利用量の制限）がワークフローの障害になった場合