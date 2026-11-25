package undecided.jobmanagement.jobexecution.spi.jobexecution;

/**
 * ジョブが手動起動されたことを表すイベント（EVT-JB-007）。
 *
 * @param executionId 実行ID（冪等性キー）
 * @param jobCode ジョブコード
 * @param actorEmployeeId 起動した従業員のID
 */
public record JobManuallyStarted(String executionId, String jobCode, String actorEmployeeId) {}
