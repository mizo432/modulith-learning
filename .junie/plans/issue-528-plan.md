# Issue #528 実装計画：スプリント容量表示（ベロシティ・割り当て可能ポイント）

## 決定済み事項（サブIssue）
- #654：ベロシティは営業日あたりSP（SP/営業日）。割り当て可能ポイント = ベロシティ × 対象スプリントの営業日数
- #655：データ不足時のチェック・フォールバックは設けない
- #656：ベロシティはチームごとに計算する
- 前提：#516（StoryPoint）、#525（スプリント割当）。オーバーアロケーションは禁止ではなく警告（#531 の範囲。本Issueでは容量表示上の超過表示まで）

## 設計判断（本計画で確定）
1. ベロシティ = チームの全完了スプリントについて「DONEアイテムのSP合計 ÷ 営業日数合計」（加重平均。スプリント規模差を吸収）。照会時点の現在データで計算するため、完了ポイントの変更は自動で反映される
2. 完了スプリントが無い、または営業日数合計が0の場合はベロシティ0（#655により特別扱いしない。除算回避のみ）
3. 営業日数 = スプリント期間（開始日〜終了日、両端含む）のうち土日でなく祝日（calendarReg の HolidayQuery）でない日数
4. 割り当て可能ポイント = ベロシティ × 対象スプリントの営業日数。小数点以下は四捨五入して整数で返す
5. 割り当て済みポイント = 対象スプリントに割り当て済みのバックログアイテムのSP合計（ステータス不問。未見積もり（SPなし）は0として扱う）
6. APIは1本にまとめる：GET /api/v1/sprints/{sprintId}/capacity → {sprintId, businessDays, velocityPerBusinessDay, assignablePoints, assignedPoints, overAllocated}。Issue本文の3API分割は実装提案であり、表示に必要な値は同一のため統合する
7. モジュール境界：SP集計は requirementBacklog の spi（SprintPointsQuery）が提供し、sprintExecution のクエリサービスが SprintFacade 相当（自モジュールの Sprint）と HolidayQuery と組み合わせて容量を計算する。他モジュールのテーブルは直接参照しない
8. 表示先：フロントエンドのスプリント一覧（SprintList）のスプリント詳細表示部に、割り当て可能ポイント・割り当て済みポイント・プログレスバー・超過時の警告文を表示する

## 段階
- 段階1：requirementBacklog spi に SprintPointsQuery（スプリント別の割り当て済みSP合計・完了SP合計）を追加
- 段階2：sprintExecution に営業日数カウンタと SprintCapacityQueryService を実装
- 段階3：容量API（GET /api/v1/sprints/{sprintId}/capacity）と medium テスト
- 段階4：フロントエンドの容量表示
- 段階5：全体検証とPR
