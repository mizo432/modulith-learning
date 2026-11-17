import { valueToColor } from '../valueColor';

describe('valueToColor', () => {
  it('Value 0 で赤系の hsl を返す（hue=0）', () => {
    const color = valueToColor(0);
    expect(color).toMatch(/^hsl\(0,\s*\d+%,\s*\d+%\)$/);
  });

  it('Value 100 で緑系の hsl を返す（hue=120）', () => {
    const color = valueToColor(100);
    expect(color).toMatch(/^hsl\(120,\s*\d+%,\s*\d+%\)$/);
  });

  it('Value 50 で中間色の hsl を返す（hue=60）', () => {
    const color = valueToColor(50);
    expect(color).toMatch(/^hsl\(60,\s*\d+%,\s*\d+%\)$/);
  });
});
