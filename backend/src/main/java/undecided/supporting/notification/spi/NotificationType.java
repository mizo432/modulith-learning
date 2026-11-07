package undecided.supporting.notification.spi;

/**
 * 通知の種類を表す列挙型です。
 * <p>
 * 各事業領域から共通して利用可能な通知タイプを定義します。
 * </p>
 */
public enum NotificationType {
    /**
     * メンション通知
     */
    MENTION,

    /**
     * 担当者変更通知
     */
    ASSIGNEE_CHANGE,

    /**
     * 期限通知
     */
    DUE_DATE,

    /**
     * 承認待ち通知
     */
    AWAITING_DECISION,

    /**
     * 承認結果通知
     */
    APPROVAL_RESULT,

    /**
     * ジョブ実行結果通知
     */
    JOB_EXECUTION_RESULT,

    /**
     * システム通知
     */
    SYSTEM,

    /**
     * カスタム通知（事業領域固有の通知）
     */
    CUSTOM
}
