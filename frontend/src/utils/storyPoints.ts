/**
 * StoryPoint の許容値と説明テキストの定数。
 *
 * 説明テキストは Issue #516 の計画書で定めた文言を使う。
 */

/** 許容される StoryPoint の値のリスト（小さい順） */
export const STORY_POINT_VALUES = [0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89];

/** StoryPoint 値ごとの説明テキスト（日本語） */
export const STORY_POINT_DESCRIPTIONS: Record<number, string> = {
  0: '見積もり不要',
  1: '小さなタスク',
  2: '小規模',
  3: 'やや小規模',
  5: '中規模',
  8: '中規模の機能',
  13: 'やや大規模',
  21: '大規模な機能',
  34: 'かなり大規模',
  55: '非常に大規模',
  89: '最大規模（分割を検討）',
};

/**
 * StoryPoint 値に対応する説明テキストを返す。
 * 対応する値がない場合は空文字列を返す。
 *
 * @param storyPoint - StoryPoint の値
 * @returns 説明テキスト
 */
export function getStoryPointDescription(storyPoint: number): string {
  return STORY_POINT_DESCRIPTIONS[storyPoint] ?? '';
}
