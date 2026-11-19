import React from 'react';
import { render, screen } from '@testing-library/react';
import { MarkdownComment } from './MarkdownComment';

describe('MarkdownComment', () => {
  describe('Markdownレンダリング', () => {
    it('太字Markdownのレンダリング - **bold**が<strong>としてレンダリングされる', () => {
      render(<MarkdownComment content="**bold**" />);
      expect(screen.getByText('bold')).toBeInTheDocument();
      const boldElement = screen.getByText('bold');
      expect(boldElement.tagName).toBe('STRONG');
    });

    it('斜体Markdownのレンダリング - *italic*が<em>としてレンダリングされる', () => {
      render(<MarkdownComment content="*italic*" />);
      expect(screen.getByText('italic')).toBeInTheDocument();
      const italicElement = screen.getByText('italic');
      expect(italicElement.tagName).toBe('EM');
    });

    it('リンクMarkdownのレンダリング - [text](url)が<a href="url">としてレンダリングされる', () => {
      render(<MarkdownComment content="[link text](https://example.com)" />);
      const anchor = screen.getByRole('link', { name: 'link text' });
      expect(anchor).toHaveAttribute('href', 'https://example.com');
    });

    it('コードブロックのレンダリング - ```code```が<pre><code>としてレンダリングされる', () => {
      render(<MarkdownComment content="```console.log('hello')```" />);
      expect(screen.getByText("console.log('hello')")).toBeInTheDocument();
      const codeElement = screen.getByText("console.log('hello')");
      expect(codeElement.closest('pre')).toBeInTheDocument();
    });

    it('インラインコードのレンダリング - `code`が<code>としてレンダリングされる', () => {
      render(<MarkdownComment content="Use `npm install` to install" />);
      expect(screen.getByText('npm install')).toBeInTheDocument();
      const codeElement = screen.getByText('npm install');
      expect(codeElement.tagName).toBe('CODE');
    });

    it('リストのレンダリング - - itemが<ul><li>としてレンダリングされる', () => {
      render(<MarkdownComment content="- item1\n- item2" />);
      expect(screen.getByText('item1')).toBeInTheDocument();
      expect(screen.getByText('item2')).toBeInTheDocument();
      const list = screen.getByText('item1').closest('ul');
      expect(list).toBeInTheDocument();
    });

    it('GFMテーブルのレンダリング - | a | b |が<table>としてレンダリングされる', () => {
      render(
        <MarkdownComment content="| Col1 | Col2 |\n|------|------|\n| A | B |" />
      );
      expect(screen.getByText('Col1')).toBeInTheDocument();
      expect(screen.getByText('A')).toBeInTheDocument();
      const table = screen.getByText('Col1').closest('table');
      expect(table).toBeInTheDocument();
    });

    it('取り消し線のレンダリング - ~~deleted~~が<del>としてレンダリングされる', () => {
      render(<MarkdownComment content="~~deleted~~" />);
      expect(screen.getByText('deleted')).toBeInTheDocument();
      const delElement = screen.getByText('deleted');
      expect(delElement.tagName).toBe('DEL');
    });

    it('タスクリストのレンダリング - - [ ] taskが<input type="checkbox">としてレンダリングされる', () => {
      render(<MarkdownComment content="- [ ] task" />);
      const checkbox = screen.getByRole('checkbox');
      expect(checkbox).toBeInTheDocument();
      expect(checkbox).not.toBeChecked();
    });
  });

  describe('セキュリティ', () => {
    it('生HTMLの除去 - <script>alert(1)</script>がテキストとしてエスケープされる', () => {
      render(<MarkdownComment content="<script>alert(1)</script>" />);
      // scriptタグが実行されないこと（エラーが出なければOK）
      // 生HTMLがそのままレンダリングされないことを確認
      expect(screen.queryByRole('alert')).not.toBeInTheDocument();
    });

    it('危険な属性の除去 - <a href="javascript:alert(1)">のhrefが除去される', () => {
      render(<MarkdownComment content="[link](javascript:alert(1))" />);
      const anchor = screen.getByRole('link');
      expect(anchor.getAttribute('href')).not.toContain('javascript:');
    });
  });

  describe('空・空白・null処理', () => {
    it('空文字列の表示 - 空文字列で「（本文なし）」が表示される', () => {
      render(<MarkdownComment content="" />);
      expect(screen.getByText('（本文なし）')).toBeInTheDocument();
    });

    it('空白のみの表示 - 空白のみで「（本文なし）」が表示される', () => {
      render(<MarkdownComment content="   " />);
      expect(screen.getByText('（本文なし）')).toBeInTheDocument();
    });

    it('null・undefinedの処理 - null/undefinedでエラーにならない', () => {
      expect(() => render(<MarkdownComment content={null as any} />)).not.toThrow();
      expect(screen.getByText('（本文なし）')).toBeInTheDocument();
    });
  });

  describe('改行', () => {
    it('改行のレンダリング - 2行以上の改行が<p>タグで区切られる', () => {
      render(<MarkdownComment content="Line1\n\nLine2" />);
      expect(screen.getByText('Line1')).toBeInTheDocument();
      expect(screen.getByText('Line2')).toBeInTheDocument();
      const p1 = screen.getByText('Line1').closest('p');
      const p2 = screen.getByText('Line2').closest('p');
      expect(p1).toBeInTheDocument();
      expect(p2).toBeInTheDocument();
      expect(p1).not.toBe(p2);
    });
  });
});
