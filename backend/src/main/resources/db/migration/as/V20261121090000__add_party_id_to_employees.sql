-- SimpleEmployeeにparty_idカラムを追加する（Issue #685: コメント通知のメール送信先の解決）

alter table undecided_as.employees
    add column party_id bigint;
