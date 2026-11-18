import { valueToColor, valueToQualityCategory, qualityCategoryToLabel, qualityCategoryToTooltip } from '../valueColor';

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

describe('valueToQualityCategory', () => {
  it('Value 100 を渡したとき、ATTRACTIVE を返す', () => {
    expect(valueToQualityCategory(100)).toBe('ATTRACTIVE');
  });

  it('Value 76 を渡したとき、ATTRACTIVE を返す', () => {
    expect(valueToQualityCategory(76)).toBe('ATTRACTIVE');
  });

  it('Value 75 を渡したとき、MUST_BE を返す', () => {
    expect(valueToQualityCategory(75)).toBe('MUST_BE');
  });

  it('Value 51 を渡したとき、MUST_BE を返す', () => {
    expect(valueToQualityCategory(51)).toBe('MUST_BE');
  });

  it('Value 50 を渡したとき、ONE_DIMENSIONAL を返す', () => {
    expect(valueToQualityCategory(50)).toBe('ONE_DIMENSIONAL');
  });

  it('Value 26 を渡したとき、ONE_DIMENSIONAL を返す', () => {
    expect(valueToQualityCategory(26)).toBe('ONE_DIMENSIONAL');
  });

  it('Value 25 を渡したとき、INDIFFERENT を返す', () => {
    expect(valueToQualityCategory(25)).toBe('INDIFFERENT');
  });

  it('Value 0 を渡したとき、INDIFFERENT を返す', () => {
    expect(valueToQualityCategory(0)).toBe('INDIFFERENT');
  });

  it('Value null を渡したとき、null を返す', () => {
    expect(valueToQualityCategory(null)).toBeNull();
  });
});

describe('qualityCategoryToLabel', () => {
  it('ATTRACTIVE のラベルが「魅力的品質」である', () => {
    expect(qualityCategoryToLabel('ATTRACTIVE')).toBe('魅力的品質');
  });

  it('MUST_BE のラベルが「当たり前品質」である', () => {
    expect(qualityCategoryToLabel('MUST_BE')).toBe('当たり前品質');
  });

  it('ONE_DIMENSIONAL のラベルが「一元的品質」である', () => {
    expect(qualityCategoryToLabel('ONE_DIMENSIONAL')).toBe('一元的品質');
  });

  it('INDIFFERENT のラベルが「無関心品質」である', () => {
    expect(qualityCategoryToLabel('INDIFFERENT')).toBe('無関心品質');
  });
});

describe('qualityCategoryToTooltip', () => {
  it('ATTRACTIVE のツールチップに「76〜100」が含まれる', () => {
    expect(qualityCategoryToTooltip('ATTRACTIVE')).toContain('76〜100');
  });

  it('MUST_BE のツールチップに「51〜75」が含まれる', () => {
    expect(qualityCategoryToTooltip('MUST_BE')).toContain('51〜75');
  });

  it('ONE_DIMENSIONAL のツールチップに「26〜50」が含まれる', () => {
    expect(qualityCategoryToTooltip('ONE_DIMENSIONAL')).toContain('26〜50');
  });

  it('INDIFFERENT のツールチップに「0〜25」が含まれる', () => {
    expect(qualityCategoryToTooltip('INDIFFERENT')).toContain('0〜25');
  });
});
