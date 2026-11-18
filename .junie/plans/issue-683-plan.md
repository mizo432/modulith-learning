# Issue #683 計画書 — ScrumTeam のメンバー従業員IDを設定する経路を新設

## 概要

`ScrumTeam.memberEmployeeIds` の配管（plumbing）は #498 で実装されたが、メンバーIDリストを設定する API 経路が存在しない。
そのため `ScrumTeamChanged` イベントの `memberEmployeeIds` は常に空リストとなり、requirementBacklog 側の `isTeamMember()` 判定が PO/SM のみに限定される。

本 Issue では、専用エンドポイントを新設し、コントローラ→サービス→エンティティ→イベント発行のフローを完結させる。

## 設計方針

1. **専用エンドポイント** `PATCH /api/v1/products/{productId}/scrum-teams/{scrumTeamId}/members` を新設し、`memberEmployeeIds` を受け付ける。
2. **UpdateScrumTeamCommandService** 経由で `ScrumTeam.setMemberEmployeeIds` を呼び、コントローラが `ScrumTeamChanged` を発行する（既存の PO/SM 専用エンドポイントのパターンに倣う）。
3. **サーバ側で現在の PO／SM の従業員IDを常時メンバーへ自動包含する（union）**。リクエストの `memberEmployeeIds` に PO/SM が含まれていなくても、保存時に union して必ず含める。
4. **ScrumTeamResponse** へ `memberEmployeeIds` を追加し、レスポンスで返す。
5. **#575 決定2** により正本は将来 Affiliation へ移るため、ScrumTeam 上の中間実装とする。

## 変更対象ファイル

### 新規作成

| ファイル | 説明 |
|---------|------|
| `backend/src/main/java/undecided/agiloom/productDiscovery/internal/presentation/UpdateScrumTeamMembersRequest.java` | リクエスト DTO（`memberEmployeeIds: List<SnowflakeId>`） |

### 修正

| ファイル | 説明 |
|---------|------|
| `backend/src/main/java/undecided/agiloom/productDiscovery/internal/presentation/ScrumTeamCommandController.java` | `PATCH /{scrumTeamId}/members` エンドポイント追加 |
| `backend/src/main/java/undecided/agiloom/productDiscovery/internal/presentation/ScrumTeamResponse.java` | `memberEmployeeIds` フィールド追加 |
| `backend/src/main/java/undecided/agiloom/productDiscovery/internal/presentation/ScrumTeamResponseConverter.java` | `memberEmployeeIds` の変換追加 |
| `backend/src/main/java/undecided/agiloom/productDiscovery/internal/command/UpdateScrumTeamCommandService.java` | `updateMembers()` メソッド追加（PO/SM の union 処理を含む） |
| `backend/src/main/java/undecided/agiloom/productDiscovery/spi/ScrumTeam.java` | `setMemberEmployeeIds()` は既存。union 処理はサービス層で行うため、エンティティ側の変更は不要 |

## 実装ステップ

### Step 1: UpdateScrumTeamMembersRequest の作成

- `UpdateScrumTeamMembersRequest` クラスを `internal/presentation` パッケージに作成
- フィールド: `List<SnowflakeId> memberEmployeeIds`（nullable; null の場合は空リストとして扱う）
- 既存の `AssignPoRequest` / `AssignSmRequest` のスタイルに倣う（`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`）

### Step 2: UpdateScrumTeamCommandService に updateMembers() を追加

- `updateMembers(SnowflakeId id, List<SnowflakeId> memberEmployeeIds)` メソッドを追加
- 処理フロー:
  1. `scrumTeamRepository.findById(id)` でエンティティ取得（NotFound なら `IllegalArgumentException`）
  2. リクエストの `memberEmployeeIds` に、現在の `poEmployeeId` と `smEmployeeId`（null でないもの）を union して結合
  3. `scrumTeam.setMemberEmployeeIds(unionedList)` を呼び出す
- `@Transactional` はクラスレベルで付与済み
- PO/SM の union 処理により、リクエストに PO/SM が含まれていなくても常にメンバーに含まれることを保証

### Step 3: ScrumTeamResponse に memberEmployeeIds を追加

- `ScrumTeamResponse` に `List<SnowflakeId> memberEmployeeIds` フィールドを追加
- `ScrumTeamResponseConverter.toScrumTeamResponse()` で `scrumTeam.getMemberEmployeeIds()` をマッピング

### Step 4: ScrumTeamCommandController に members エンドポイントを新設

- `PATCH /{scrumTeamId}/members` を追加
- リクエストボディ: `UpdateScrumTeamMembersRequest`（`@Valid`）
- 処理:
  1. `updateScrumTeamCommandService.updateMembers(snowflakeId, request.getMemberEmployeeIds())`
  2. `scrumTeamQueryService.findById(snowflakeId).orElseThrow()` で更新後エンティティ取得
  3. `eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam))` でイベント発行
  4. `ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam))` でレスポンス返却
- 既存の `assignPo` / `assignSm` メソッドのパターンに完全に倣う

## テスト戦略

サービスレイヤーのテストは Spring Boot に依存しない **small test** を基本とする。
既存の `UpdateScrumTeamCommandServiceTest`（`@ExtendWith(MockitoExtension.class)` + `@Tag("small")`）のスタイルに倣う。

### テストリスト

#### A. ScrumTeamTest（spi 層 — setMemberEmployeeIds）

1. setMemberEmployeeIds() に有効なIDリストを渡したとき、memberEmployeeIds が設定される
2. setMemberEmployeeIds() に null を渡したとき、IllegalArgumentException がスローされる
3. setMemberEmployeeIds() に空リストを渡したとき、memberEmployeeIds が空リストになる
4. setMemberEmployeeIds() を呼び出したとき、updatedAt が更新される
5. setMemberEmployeeIds() で渡したリストを変更しても、内部リストに影響しない（防御的コピー）

#### B. UpdateScrumTeamCommandServiceTest（command 層 — updateMembers）

6. updateMembers() に有効なIDリストを渡したとき、ScrumTeam の memberEmployeeIds が設定される
7. updateMembers() に渡したIDリストに PO の従業員IDが含まれていないとき、PO の従業員IDが自動的に含まれる
8. updateMembers() に渡したIDリストに SM の従業員IDが含まれていないとき、SM の従業員IDが自動的に含まれる
9. updateMembers() に渡したIDリストに PO と SM の両方が含まれていないとき、両方が自動的に含まれる
10. updateMembers() に渡したIDリストに PO と SM が既に含まれているとき、重複せずに1回ずつ含まれる
11. updateMembers() に空リストを渡して PO/SM が設定されているとき、PO と SM のみが含まれる
12. updateMembers() に空リストを渡して PO/SM がどちらも未設定のとき、memberEmployeeIds が空リストになる
13. updateMembers() で ScrumTeam が見つからないとき、IllegalArgumentException がスローされる

#### C. ScrumTeamResponseConverter テスト（presentation 層）

14. ScrumTeamResponseConverter.toScrumTeamResponse() を呼び出したとき、memberEmployeeIds が変換結果に含まれる
15. ScrumTeam の memberEmployeeIds が空リストのとき、レスポンスの memberEmployeeIds も空リストになる

#### D. ScrumTeamChanged テスト（spi 層 — 既存テストの補足）

16. ScrumTeamChanged.of() で memberEmployeeIds に PO/SM が含まれる ScrumTeam を渡したとき、イベントの memberEmployeeIds に PO/SM が含まれる
（※ `ScrumTeamChangedMemberIdsTest` に既存のテストがあるが、union 処理後の状態をカバーするテストを追加）

#### E. 統合テスト（medium — 必須ではないが推奨）

17. PATCH /api/v1/products/{productId}/scrum-teams/{scrumTeamId}/members に有効なリクエストを送信したとき、200 が返りレスポンスに memberEmployeeIds が含まれる
18. PATCH /api/v1/products/{productId}/scrum-teams/{scrumTeamId}/members に存在しない scrumTeamId を指定したとき、404 または 500 が返る
19. PATCH /api/v1/products/{productId}/scrum-teams/{scrumTeamId}/members に空リストを送信して PO/SM が設定されているとき、レスポンスの memberEmployeeIds に PO/SM が含まれる

## 非適用範囲

- frontend のチーム編集 UI（別 Issue で対応）
- 既存チームへの初期データ投入（本 Issue のスコープ外）
- Affiliation 領域への正本移行（#575 の今後の課題）

## 依存関係

- 依存する Issue: #498（memberEmployeeIds の配管実装済み）
- 関連する設計決定: #575 決定2（Affiliation への正本移行予定）
- 影響を受ける Issue: #620（Ready 遷移の権限拡張 — 本 Issue 完了後に機能する）
