# DDDモデル候補

## 概要

本ファイルは、EF-03 帳票利用のDDD（Domain-Driven Design）モデル候補を定義します。

## サブドメイン候補

| 項番 | サブドメイン名 | 種類 | 説明 |
|------|---------------|------|------|
| 01 | 帳票配信 | コア | 帳票の配信管理 |
| 02 | 帳票プレビュー | コア | 帳票のプレビュー表示 |
| 03 | 帳票検索 | コア | 帳票の検索機能 |
| 04 | 承認・確認 | コア | 帳票の承認ワークフロー |

## アグリゲート候補

| 項番 | アグリゲート名 | 集約ルート | 説明 |
|------|---------------|-----------|------|
| 01 | FormDelivery | FormDelivery | 帳票配信の集約 |
| 02 | FormPreview | FormPreview | 帳票プレビューの集約 |
| 03 | FormSearch | FormSearch | 帳票検索の集約 |
| 04 | Approval | Approval | 承認・確認の集約 |

## エンティティ候補

| 項番 | エンティティ名 | アグリゲート | 説明 |
|------|---------------|-------------|------|
| 01 | FormDelivery | FormDelivery | 帳票配信 |
| 02 | FormPreview | FormPreview | 帳票プレビュー |
| 03 | FormSearch | FormSearch | 帳票検索 |
| 04 | Approval | Approval | 承認・確認 |

## 値オブジェクト候補

| 項番 | 値オブジェクト名 | 使用箇所 | 説明 |
|------|-----------------|---------|------|
| 01 | DeliveryId | FormDelivery | 配信識別子 |
| 02 | RecipientId | FormDelivery | 宛先識別子 |
| 03 | DeliveryType | FormDelivery | 配信種類 |
| 04 | DeliveryStatus | FormDelivery | 配信ステータス |
| 05 | PreviewId | FormPreview | プレビュー識別子 |
| 06 | CachePath | FormPreview | キャッシュパス |
| 07 | SearchId | FormSearch | 検索識別子 |
| 08 | SearchCondition | FormSearch | 検索条件 |
| 09 | ApprovalId | Approval | 承認識別子 |
| 10 | ApproverId | Approval | 承認者識別子 |
| 11 | ApprovalStatus | Approval | 承認ステータス |
| 12 | ApprovalComment | Approval | 承認コメント |

## ドメインサービス候補

| 項番 | サービス名 | 説明 |
|------|-----------|------|
| 01 | DeliveryService | 帳票配信のロジック |
| 02 | PreviewService | プレビュー生成ロジック |
| 03 | SearchService | 帳票検索ロジック |
| 04 | ApprovalWorkflowService | 承認ワークフロー管理 |

## ドメインイベント候補

| 項番 | イベント名 | 発生元 | 説明 |
|------|-----------|--------|------|
| 01 | FormDelivered | FormDelivery | 帳票が配信された |
| 02 | ApprovalCompleted | Approval | 承認が完了した |
