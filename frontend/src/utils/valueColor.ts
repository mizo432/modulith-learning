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
