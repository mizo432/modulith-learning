## 概要

バッチステップの開始・終了時にログを出力する共通リスナーを追加しました。
また、JobLauncherからJobOperatorへのリネームを行い、Spring Batchの標準的な命名に合わせました。

すべてのバッチステップにBatchStepListenerを適用することで、一貫したログ出力を実現します。

| ファイルパス                                                                                                           | 状態      | 修正概要                                   | 備考                            |
|------------------------------------------------------------------------------------------------------------------------|-----------|--------------------------------------------|---------------------------------|
| `backend/src/main/java/undecided/shared/config/BatchStepListener.java`                                                 | `created` | ステップ開始・終了ログ出力リスナー新規追加 | StepExecutionListener実装       |
| `backend/src/main/java/undecided/generic/addressReg/internal/batch/config/ChouAzaImportBatchConfig.java`               | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/addressReg/internal/batch/config/CityImportBatchConfig.java`                  | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/addressReg/internal/batch/config/PrefectureImportBatchConfig.java`            | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/bankReg/internal/batch/config/BankImportBatchConfig.java`                     | `updated` | BatchStepListenerを4つのステップに追加     | 特になし                        |
| `backend/src/main/java/undecided/generic/bankReg/internal/batch/config/BranchImportBatchConfig.java`                   | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/calendarReg/internal/batch/config/HolidayImportBatchConfig.java`              | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/largeRecordBatch/internal/batch/config/LargeRecordImportBatchConfig.java`     | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/sampleBatch/internal/batch/config/SampleItemImportBatchConfig.java`           | `updated` | BatchStepListenerをステップに追加          | 特になし                        |
| `backend/src/main/java/undecided/generic/addressReg/internal/batch/operator/ChouAzaImportJobOperator.java`             | `renamed` | ChouAzaImportJobLauncherからリネーム       | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/addressReg/internal/batch/operator/CityImportJobOperator.java`                | `renamed` | CityImportJobLauncherからリネーム          | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/addressReg/internal/batch/operator/PrefectureImportJobOperator.java`          | `renamed` | PrefectureImportJobLauncherからリネーム    | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/bankReg/internal/batch/operator/BankImportJobOperator.java`                   | `renamed` | BankImportJobLauncherからリネーム          | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/bankReg/internal/batch/operator/BranchImportJobOperator.java`                 | `renamed` | BranchImportJobLauncherからリネーム        | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/calendarReg/internal/batch/operator/HolidayImportJobOperator.java`            | `renamed` | HolidayImportJobLauncherからリネーム       | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/largeRecordBatch/internal/batch/operator/LargeRecordImportJobOperator.java`   | `renamed` | LargeRecordImportJobLauncherからリネーム   | launcher→operatorパッケージ変更 |
| `backend/src/main/java/undecided/generic/sampleBatch/internal/batch/operator/SampleItemImportJobOperator.java`         | `renamed` | SampleItemImportJobLauncherからリネーム    | launcher→operatorパッケージ変更 |
| `backend/src/test/java/undecided/generic/addressReg/internal/batch/config/ChouAzaImportBatchConfigTest.java`           | `updated` | JobLauncherからJobOperatorへの参照更新     | 特になし                        |
| `backend/src/test/java/undecided/generic/addressReg/internal/batch/config/CityImportBatchConfigTest.java`              | `updated` | JobLauncherからJobOperatorへの参照更新     | 特になし                        |
| `backend/src/test/java/undecided/generic/addressReg/internal/batch/config/PrefectureImportBatchConfigTest.java`        | `updated` | JobLauncherからJobOperatorへの参照更新     | 特になし                        |
| `backend/src/test/java/undecided/generic/calendarReg/internal/batch/config/HolidayImportBatchConfigTest.java`          | `updated` | JobLauncherからJobOperatorへの参照更新     | 特になし                        |
| `backend/src/test/java/undecided/generic/largeRecordBatch/internal/batch/config/LargeRecordImportBatchConfigTest.java` | `updated` | JobLauncherからJobOperatorへの参照更新     | 特になし                        |
| `backend/src/test/java/undecided/generic/sampleBatch/internal/batch/config/SampleItemImportBatchConfigTest.java`       | `updated` | JobLauncherからJobOperatorへの参照更新     | 特になし                        |
| `.agents/guidelines/java/coding-standards.guidelines.md`                                                               | `updated` | launcher→operatorへの記載更新              | 特になし                        |
| `.agents/skills/batch-design/SKILL.md`                                                                                 | `updated` | launcher→operatorへの記載更新              | 特になし                        |
| `.agents/skills/modulith-dev/SKILL.md`                                                                                 | `updated` | launcher→operatorへの記載更新              | 特になし                        |
| `docs/05 バッチガイド/バッチ設計ガイド.md`                                                                             | `updated` | launcher→operatorへの記載更新              | 特になし                        |

## 動作確認方法

- ブランチをチェックアウト
- 以下のコマンドでビルドとテストを実行

```bash
./gradlew :backend:compileJava
./gradlew :backend:compileTestJava
```

## 関連issueやプルリクエスト

closes: [#291](https://github.com/mizo432/modulith-learning/issues/291) — バッチステップ開始時にログを出力する
closes: [#292](https://github.com/mizo432/modulith-learning/issues/292) — バッチステップ終了時にログを出力する
closes: [#294](https://github.com/mizo432/modulith-learning/issues/294) —
JobLauncherからJobOperatorへのリネーム

## 懸念事項

- 特になし

## 参考ドキュメント

- [Spring Batch StepExecutionListener](https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/core/listener/StepExecutionListener.html)
