-- notifications テーブルに複合インデックスを追加
-- findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc() のクエリ性能向上
CREATE INDEX idx_notifications_recipient_read_created
    ON undecided_nt.notifications(recipient_id, is_read, created_at DESC);
