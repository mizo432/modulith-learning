package undecided.agiloom.sprintExecution.spi;

/**
 * @deprecated {@link undecided.supporting.notification.spi.NotificationType} を使用してください。
 * このクラスは後方互換性のために残されています。
 */
@Deprecated
public enum NotificationType {
    /** @deprecated {@link undecided.supporting.notification.spi.NotificationType#MENTION} を使用 */
    @Deprecated MENTION,
    /** @deprecated {@link undecided.supporting.notification.spi.NotificationType#ASSIGNEE_CHANGE} を使用 */
    @Deprecated ASSIGNEE_CHANGE,
    /** @deprecated {@link undecided.supporting.notification.spi.NotificationType#DUE_DATE} を使用 */
    @Deprecated DUE_DATE,
    /** @deprecated {@link undecided.supporting.notification.spi.NotificationType#AWAITING_DECISION} を使用 */
    @Deprecated AWAITING_DECISION
}
