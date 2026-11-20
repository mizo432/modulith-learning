import React from 'react';
import {render} from '@testing-library/react';

// axios の ESM 構文が Jest でパースできないため、api.ts 経由のインポートをモック
jest.mock('./services/api', () => ({
  __esModule: true,
  default: {
    interceptors: {
      request: {use: jest.fn()},
      response: {use: jest.fn()},
    },
  },
}));

import App from './App';

describe('App', () => {
  it('should pass initially', () => {
    expect(true).toBe(true);
  });

  // Issue #701 スモークテスト
  describe('App 描画スモークテスト', () => {
    it('App を render() したとき、例外をスローせずに描画が完了する', () => {
      expect(() => {
        render(<App />);
      }).not.toThrow();
    });

    it('未認証状態で App を描画したとき、ログイン画面のタイトルが表示される', () => {
      const {container} = render(<App />);
      // Login ページにリダイレクトされるため、ログイン関連の要素が存在することを確認
      const body = container.querySelector('body') || container;
      expect(body.textContent).toMatch(/login|ログイン|sign.?in/i);
    });

    it('App を描画したとき、AuthProvider が正しく提供され、useAuth() が例外をスローしない', () => {
      expect(() => {
        const {container} = render(<App />);
        // AppContent 内の useAuth() が例外をスローせず、描画が完了することを確認
        expect(container).toBeTruthy();
      }).not.toThrow();
    });
  });
});
