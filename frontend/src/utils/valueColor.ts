/**
 * Value（0〜100）を HSL 色相に変換するユーティリティ。
 * 0 → 赤（hue=0°）、100 → 緑（hue=120°）の線形補間。
 */

const VALUE_MIN = 0;
const VALUE_MAX = 100;

/**
 * Value からテキスト用の濃い色（saturation=70%, lightness=45%）を返す。
 */
export function valueToColor(value: number): string {
  const clamped = Math.max(VALUE_MIN, Math.min(VALUE_MAX, value));
  const hue = (clamped / VALUE_MAX) * 120;
  return `hsl(${hue}, 70%, 45%)`;
}

/**
 * Value から背景用の薄い色（saturation=70%, lightness=92%）を返す。
 */
export function valueToBackgroundColor(value: number): string {
  const clamped = Math.max(VALUE_MIN, Math.min(VALUE_MAX, value));
  const hue = (clamped / VALUE_MAX) * 120;
  return `hsl(${hue}, 70%, 92%)`;
}

export { VALUE_MIN, VALUE_MAX };

/**
 * 品質カテゴリの型定義。
 */
export type QualityCategory = 'ATTRACTIVE' | 'MUST_BE' | 'ONE_DIMENSIONAL' | 'INDIFFERENT';

/**
 * 品質カテゴリごとの色定数（背景色・文字色）。
 * コントラスト比4.5:1以上を確保。黄バッジは濃色文字。
 */
export const QUALITY_CATEGORY_STYLES: Record<QualityCategory, { backgroundColor: string; color: string }> = {
  ATTRACTIVE: { backgroundColor: '#2e7d32', color: '#ffffff' },
  MUST_BE: { backgroundColor: '#fdd835', color: '#212121' },
  ONE_DIMENSIONAL: { backgroundColor: '#1976d2', color: '#ffffff' },
  INDIFFERENT: { backgroundColor: '#616161', color: '#ffffff' },
};

/**
 * Value（0〜100）から品質カテゴリを返す。
 * 76以上=ATTRACTIVE、51以上=MUST_BE、26以上=ONE_DIMENSIONAL、0以上=INDIFFERENT、null=null
 */
export function valueToQualityCategory(value: number | null): QualityCategory | null {
  if (value === null) {
    return null;
  }
  if (value >= 76) {
    return 'ATTRACTIVE';
  }
  if (value >= 51) {
    return 'MUST_BE';
  }
  if (value >= 26) {
    return 'ONE_DIMENSIONAL';
  }
  return 'INDIFFERENT';
}

/**
 * 品質カテゴリから日本語ラベルを返す。
 */
export function qualityCategoryToLabel(category: QualityCategory): string {
  const labelMap: Record<QualityCategory, string> = {
    ATTRACTIVE: '魅力的品質',
    MUST_BE: '当たり前品質',
    ONE_DIMENSIONAL: '一元的品質',
    INDIFFERENT: '無関心品質',
  };
  return labelMap[category];
}

/**
 * 品質カテゴリからツールチップテキスト（境界値）を返す。
 */
export function qualityCategoryToTooltip(category: QualityCategory): string {
  const tooltipMap: Record<QualityCategory, string> = {
    ATTRACTIVE: 'Value 76〜100',
    MUST_BE: 'Value 51〜75',
    ONE_DIMENSIONAL: 'Value 26〜50',
    INDIFFERENT: 'Value 0〜25',
  };
  return tooltipMap[category];
}
