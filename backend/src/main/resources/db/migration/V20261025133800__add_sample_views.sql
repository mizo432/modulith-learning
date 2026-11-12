-- V20261025133800__add_sample_views.sql
-- ViewとMaterialized Viewのサンプルを作成します。

-- パーティサマリーViewの作成
-- partiesテーブルからパーティ情報を提供する読み取り専用Viewです。
CREATE OR REPLACE VIEW undecided_as.v_party_summary AS
SELECT 
    p.party_id,
    p.party_type,
    p.simple_mame
FROM undecided_as.parties p;

-- Viewへのコメント付与
COMMENT ON VIEW undecided_as.v_party_summary IS 'パーティのサマリー情報を提供するView';
COMMENT ON COLUMN undecided_as.v_party_summary.party_id IS 'パーティの一意な識別子';
COMMENT ON COLUMN undecided_as.v_party_summary.party_type IS 'パーティタイプ（PERSON, COMPANY など）';
COMMENT ON COLUMN undecided_as.v_party_summary.simple_mame IS 'パーティの名称';

-- パーティ集計Materialized Viewの作成
-- パーティタイプごとの集計結果を物理的に保存します。
CREATE MATERIALIZED VIEW undecided_as.mv_party_summary AS
SELECT 
    p.party_type,
    COUNT(*) AS party_count
FROM undecided_as.parties p
GROUP BY p.party_type;

-- Materialized View用のインデックス定義（パフォーマンス向上）
CREATE INDEX idx_mv_party_summary_type ON undecided_as.mv_party_summary (party_type);

-- Materialized Viewへのコメント付与
COMMENT ON MATERIALIZED VIEW undecided_as.mv_party_summary IS 'パーティタイプごとの集計結果を保存するMaterialized View';
COMMENT ON COLUMN undecided_as.mv_party_summary.party_type IS 'パーティタイプ（PERSON, COMPANY など）';
COMMENT ON COLUMN undecided_as.mv_party_summary.party_count IS 'パーティ数';
