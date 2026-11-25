package undecided.agiloom.requirementBacklog.internal;

/**
 * バックログアイテムの依存関係の要約（一覧・詳細レスポンス用）。
 *
 * @param dependencyCount 依存先の件数
 * @param dependenciesSatisfied 依存先がすべて DONE か（依存が無い場合も true）
 */
record BacklogItemDependencySummary(int dependencyCount, boolean dependenciesSatisfied) {}
