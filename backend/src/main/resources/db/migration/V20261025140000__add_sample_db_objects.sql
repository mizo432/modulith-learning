-- V20261025140000__add_sample_db_objects.sql
-- DBオブジェクト（シーケンス、シノニム、ストアドプロシージャ）のサンプルを作成します。

-- サンプルシーケンスデモテーブルの作成
-- SampleSequenceエンティティ用のテーブルです。
CREATE TABLE IF NOT EXISTS undecided_as.sample_sequence_demo (
    id BIGINT PRIMARY KEY,
    sequence_name VARCHAR(50) NOT NULL,
    description VARCHAR(200)
);

-- テーブルへのコメント付与
COMMENT ON TABLE undecided_as.sample_sequence_demo IS 'シーケンスサンプル用のデモテーブル';
COMMENT ON COLUMN undecided_as.sample_sequence_demo.id IS 'シーケンスで生成されたID';
COMMENT ON COLUMN undecided_as.sample_sequence_demo.sequence_name IS 'シーケンス名';
COMMENT ON COLUMN undecided_as.sample_sequence_demo.description IS '説明';

-- サンプルシーケンスの作成
-- ID生成や通番管理に使用されるシーケンスのサンプルです。
CREATE SEQUENCE IF NOT EXISTS undecided_as.seq_sample_order
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    NO CYCLE
    CACHE 10;

-- シーケンスへのコメント付与
COMMENT ON SEQUENCE undecided_as.seq_sample_order IS 'サンプル注文番号用のシーケンス';

-- サンプルシーケンス2（パーティ用）
CREATE SEQUENCE IF NOT EXISTS undecided_as.seq_sample_party
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    NO CYCLE
    CACHE 10;

-- シーケンスへのコメント付与
COMMENT ON SEQUENCE undecided_as.seq_sample_party IS 'サンプルパーティID用のシーケンス';

-- サンプルシノニムの作成
-- 元のテーブル（parties）へのシノニムを作成します。
CREATE OR REPLACE SYNONYM undecided_as.syn_parties FOR undecided_as.parties;

-- シノニムへのコメント付与
COMMENT ON SYNONYM undecided_as.syn_parties IS 'partiesテーブルへのシノニム';

-- サンプルストアドプロシージャの作成
-- シーケンスから次の値を取得するプロシージャです。
CREATE OR REPLACE PROCEDURE undecided_as.proc_get_next_order_id(
    OUT p_order_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    p_order_id := nextval('undecided_as.seq_sample_order');
END;
$$;

-- プロシージャへのコメント付与
COMMENT ON PROCEDURE undecided_as.proc_get_next_order_id IS 'サンプル注文番号シーケンスから次の値を取得するプロシージャ';

-- サンプルストアドファンクションの作成
-- パーティタイプごとのカウントを返すファンクションです。
CREATE OR REPLACE FUNCTION undecided_as.func_count_parties_by_type(
    p_party_type VARCHAR(10)
)
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    v_count BIGINT;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM undecided_as.parties
    WHERE party_type = p_party_type;
    RETURN COALESCE(v_count, 0);
END;
$$;

-- ファンクションへのコメント付与
COMMENT ON FUNCTION undecided_as.func_count_parties_by_type IS 'パーティタイプごとのパーティ数を返すファンクション';
