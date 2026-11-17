---
name: frontend-design
description: React 18、TypeScript、MUI v5、React Router v6に基づくフロントエンド設計ガイドライン。コンポーネント設計、ステート管理、ルーティング、API通信、型安全性の設計ルールを提供。
---

### Frontend Design Guide (`frontend-design`)

このスキルは、 **modulith-learning** リポジトリのフロントエンド（`frontend`
）において、アーキテクチャ設計、コンポーネント分割、ステート管理、ルーティング設計、API通信パターンを行う際に適用する設計ガイドラインです。

---

### 1. いつこのスキルを使用するか (When To Use)

- 新規ページやコンポーネントの設計・実装を行うとき
- 既存コンポーネントのリファクタリングや設計見直しを行うとき
- ステート管理戦略（ローカル/グローバル）を設計するとき
- ルーティング構造やナビゲーションフローを設計するとき
- API通信パターンやエラーハンドリング戦略を設計するとき
- 型定義（インターフェース、型エイリアス）の設計を行うとき
- アクセシビリティやパフォーマンス最適化を検討するとき

---

### 2. アーキテクチャ設計原則 (Architecture Design Principles)

#### 2.1 技術スタック

| カテゴリ               | テクノロジー                                          |
|:-----------------------|:------------------------------------------------------|
| **フレームワーク**     | React 18                                              |
| **言語**               | TypeScript 4.9（strictモード有効）                    |
| **UIコンポーネント**   | MUI v5（@mui/material, @mui/icons-material）+ Emotion |
| **ルーティング**       | React Router v6                                       |
| **HTTPクライアント**   | axios                                                 |
| **ビルドツール**       | Create React App (react-scripts 5.0.1)                |
| **テスト**             | Jest / React Testing Library                          |
| **パフォーマンス計測** | web-vitals                                            |

#### 2.2 コンポーネント指向設計

- **単一責任の原則**: 各コンポーネントは一つの明確な責務を持つ。
- **再利用性**: 共通機能はカスタムフックやプレゼンテーションコンポーネントとして抽出。
- **コンポジション**: 継承ではなく、コンポーネントの組み合わせで複雑なUIを構築。
- **Props駆動**: コンポーネントはPropsによって動作が決定される純粋関数として設計。

#### 2.3 型安全性第一

- **TypeScript strictモード**: 厳格な型チェックを常に有効化。
- **any型禁止**: `any`型の使用を厳禁し、代わりに`unknown`型を使用する。
- **unknown型の活用**: 型が不明な場合は`unknown`型を使用し、型ガードで安全に処理する。
- **明示的な型注釈**: 型推論に過度に依存せず、Propsや戻り値には明示的な型を付与。
- **インターフェース優先**: 外部APIやコンポーネント境界では`interface`を、型エイリアスは内部で使用する。
- **constアサーション**: リテラル型を保持する必要がある場合は`as const`を使用する。

#### 2.4 デジタル庁デザインシステム参照

フロントエンドの設計・実装では、**デジタル庁デザインシステムβ版** ([design.digital.go.jp](https://design.digital.go.jp)) を参考として参照します。デジタル庁デザインシステムは「誰一人取り残されない、人に優しいデジタル化を。」をミッションとし、アクセシビリティファーストを標榜するデザインガイドラインです。

**参照する主な原則**:

- **アクセシビリティファースト**: WCAG 2.2 AA基準およびJIS X 8341-3:2016に準拠した設計を基本とする。
- **カラーコントラスト**: テキストと背景のコントラスト比は少なくとも 4.5:1 以上を確保する。
- **タイポグラフィ**: 原則として 16px 未満の文字サイズを使用しない。行間やブロック幅も学習障害や読字障害を考慮して設計する。
- **色覚多様性への配慮**: カラーユニバーサルデザイン推奨配色セットを踏まえた色使いを行う。
- **キーボード操作**: すべてのインタラクティブ要素をキーボードのみで操作可能にする。
- **スクリーンリーダー対応**: 適切なARIA属性を使用して、スクリーンリーダーとの互換性を確保する。

**参考リソース**:

- デジタル庁デザインシステムβ版: https://design.digital.go.jp
- コードスニペット（React版）: https://github.com/digital-go-jp/design-system-example-components-react
- デザイントークン（Tailwind CSSプラグイン）: `@digital-go-jp/tailwind-theme-plugin`
- ウェブアクセシビリティ導入ガイドブック: デジタル庁が公開するアクセシビリティ基礎ガイド

**適用方針**:

- MUI v5をベースとしつつ、デジタル庁デザインシステムのアクセシビリティ基準を優先して適用する。
- テーマ設定（`theme.ts`）では、デジタル庁デザインシステムのカラーパレットやタイポグラフィの考え方を参照する。
- コンポーネント設計では、デジタル庁デザインシステムのUIパターンを参考にするが、MUIコンポーネントを優先使用する。
- アクセシビリティチェックリストは、デジタル庁デザインシステムのガイドラインを基準に評価する。

---

### 3. ディレクトリ構成規則 (Directory Structure Rules)

#### 3.1 標準的なディレクトリ構成

```
frontend/src/
├── components/       # 再利用可能なUIコンポーネント
├── pages/            # ルートに対応するページコンポーネント
│   └── <domain>/     # ドメインごとに分離（例: projects/, users/）
├── services/         # API通信サービス
├── contexts/         # React Context
├── types/            # 型定義（任意、必要に応じて）
├── utils/            # ユーティリティ関数（任意、必要に応じて）
├── App.tsx           # ルートコンポーネント
├── index.tsx         # エントリポイント
└── theme.ts          # MUIテーマ設定
```

#### 3.2 機能別分離の原則

- **ページコンポーネント**: `src/pages` 配下に配置し、ドメインごとにサブディレクトリを設ける。
- **共有コンポーネント**: `src/components` 配下に配置し、複数ページで再利用されるUI部品を収容。
- **サービス層**: `src/services` 配下にAPI通信を一元管理するサービスファイルを配置。
- **コンテキスト**: `src/contexts` 配下にグローバルステートを管理するContextを配置。

---

### 4. コンポーネント設計 (Component Design)

#### 4.1 コンポーネント分類

| 種類                                 | 説明                                                                            |
|:-------------------------------------|:--------------------------------------------------------------------------------|
| **プレゼンテーションコンポーネント** | UI表示に特化。Propsでデータを受け取り、状態を持たない。                         |
| **コンテナコンポーネント**           | データ取得やステート管理を担当。プレゼンテーションコンポーネントにPropsを渡す。 |
| **カスタムフック**                   | ロジックを抽出して再利用可能なフックとして提供する。                            |

#### 4.2 コンポーネント設計ルール

- **Propsの型定義**: 全てのPropsに明確な型定義を設け、デフォルト値を設定。
- **React.FCの使用**: React.FCは使用せず、明示的なProps型定義を使用する（型推論の正確性とエラーメッセージの明確化のため）。
- **副作用の管理**: `useEffect` で副作用を管理し、クリーンアップ関数を適切に実装。
- **ロジックの抽出**: 複雑なロジックはカスタムフックとして抽出し、コンポーネントはUIに集中させる。
- **コンポーネントサイズ**: 一つのコンポーネントが大きくなりすぎた場合は、子コンポーネントに分割。
- **早期リターン**: 条件分岐を浅く保つため、早期リターンを積極的に使用する。

#### 4.3 命名規則

| 要素             | 規則                                          | 例                                |
|:-----------------|:----------------------------------------------|:----------------------------------|
| コンポーネント名 | PascalCase                                    | `CustomerList`, `OrderForm`       |
| 関数名・変数名   | camelCase                                     | `fetchCustomers`, `orderList`     |
| 定数             | UPPER_SNAKE_CASE                              | `MAX_RETRY_COUNT`, `API_BASE_URL` |
| ファイル名       | コンポーネントはPascalCase、その他はcamelCase | `Header.tsx`, `api.ts`            |

---

### 5. ステート管理設計 (State Management Design)

#### 5.1 ステート管理の階層

| スコープ           | 手法                   | 使用場面                           |
|:-------------------|:-----------------------|:-----------------------------------|
| **ローカル**       | `useState`             | コンポーネント内の一時的な状態     |
| **複雑なローカル** | `useReducer`           | 複数の状態が連動する複雑なロジック |
| **グローバル**     | Context API            | 認証情報、テーマ、グローバル設定   |
| **サーバー状態**   | axios + カスタムフック | APIデータ取得・キャッシュ          |

#### 5.2 ステート管理ルール

- **ローカルステート優先**: 可能な限りローカルステートで管理し、グローバルステートは最小限に抑える。
- **Contextの適切な使用**: 認証情報やテーマなど、本当にグローバルな状態のみContextで管理。
- **カスタムフックでの抽象化**: API通信や複雑なステートロジックはカスタムフックでカプセル化。
- **不変性の尊重**: ステートの更新は常に新しいオブジェクトを返すようにし、直接変更しない。

#### 5.3 カスタムフック設計ガイドライン

カスタムフックはロジックを再利用可能にするための重要なパターンです。以下のガイドラインに従ってください。

**命名規則**:
- `use`で始める（例: `useAuth`, `useFetch`, `useLocalStorage`）
- フックの目的を明確に示す名前をつける

**設計原則**:
- **単一責任**: 一つのフックは一つの明確な責務を持つ
- **Props駆動**: フックの動作は引数によって決定される純粋関数として設計
- **エラーハンドリング**: エラー状態を適切に返し、呼び出し側で処理可能にする
- **ローディング状態**: 非同期操作にはローディング状態を含める

**コード例**:
```typescript
// ✅ Good: カスタムフックの実装例
import {useState, useEffect} from 'react';
import api from './services/api';

interface UseFetchResult<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

function useFetch<T>(url: string): UseFetchResult<T> {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const controller = new AbortController();

    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await api.get<T>(url, {signal: controller.signal});
        setData(response.data);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(err);
        } else {
          setError(new Error('不明なエラーが発生しました'));
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();

    return () => {
      controller.abort();
    };
  }, [url]);

  return {data, loading, error};
}

export default useFetch;
```

---

### 6. ルーティング設計 (Routing Design)

#### 6.1 React Router v6の設計パターン

- **宣言的ルーティング**: `<Routes>` と `<Route>` でルーティングを宣言的に定義。
- **ネストされたルート**: 親子関係のあるページはネストされたルートで表現。
- **動的ルート**: パスパラメータを使用して、動的なリソースにアクセス。
- **ガードルート**: 認証が必要なページは保護されたルートで囲む。

#### 6.2 ルーティング設計ルール

- **ページ単位のコンポーネント**: 各ルートは独立したページコンポーネントに対応。
- **404ハンドリング**: 存在しないパスへのアクセスはNotFoundページで統一。
- **ナビゲーションの一貫性**: サイドメニューやタブナビゲーションでルーティングを一元管理。
- **クエリパラメータ**: 検索条件やページネーションはクエリパラメータで管理。

---

### 7. API通信設計 (API Communication Design)

#### 7.1 axiosラッパー設計

- **一元管理**: `src/services/api.ts` でaxiosインスタンスを一元管理。
- **インターセプター**: リクエスト/レスポンスインターセプターで認証トークンの付与やエラーハンドリングを統一。
- **ベースURL設定**: 環境変数 (`REACT_APP_API_URL`) でベースURLを管理。

#### 7.2 サービス層設計

- **ドメイン別サービス**: `userService.ts`, `projectService.ts` など、ドメインごとにサービスファイルを分離。
- **型安全なAPI呼び出し**: リクエスト・レスポンスの型を明確に定義。
- **エラーハンドリング**: 一貫したエラー処理パターンを採用し、ユーザーに適切なフィードバックを提供。

#### 7.3 ローディング・エラー状態の管理

- **ローディング表示**: データ取得中はローディングインジケーターを表示。
- **エラー表示**: エラー発生時はユーザーに明確なエラーメッセージを表示。
- **リトライ機能**: 一時的なネットワークエラー時はリトライオプションを提供。
- **型安全なエラーハンドリング**: catch句では`any`型ではなく`unknown`型を使用し、型ガードで安全にエラー情報を抽出する。

**コード例**:
```typescript
// ❌ Bad: any型の使用
try {
  await api.post('/users', userData);
} catch (err: any) {
  setError(err.response?.data?.message || 'Error');
}

// ✅ Good: unknown型と型ガードの使用
try {
  await api.post('/users', userData);
} catch (err: unknown) {
  if (err instanceof Error) {
    setError(err.message);
  } else {
    setError('不明なエラーが発生しました');
  }
}
```

---

### 8. テーマとスタイリング設計 (Theme & Styling Design)

#### 8.1 MUIテーマ設計

- **テーマ集約**: `frontend/src/theme.ts` にテーマ設定を一元化。
- **環境別設定**: `.env.development`, `.env.staging`, `.env.production` で環境別の色や設定を上書き。
- **カスタムテーマ**: MUIのデフォルトテーマを拡張し、プロジェクト固有のデザインシステムを構築。
- **環境変数**: `REACT_APP_PRIMARY_COLOR`, `REACT_APP_SECONDARY_COLOR`, `REACT_APP_BACKGROUND_COLOR`
  でテーマカラーを環境別に設定。

#### 8.2 デジタル庁デザインシステムに基づくスタイリング

デジタル庁デザインシステムの基本デザインの原則を参照して、テーマとスタイリングを設計します。

**カラー設計**:

- **カラーパレット**: デジタル庁デザインシステムのプリミティブカラー(10色)とSolid Grayを参考にしたカラースキームを構築する。
- **コントラスト比**: テキストと背景のコントラスト比は少なくとも **4.5:1** 以上を確保する(JIS X 8341-3:2016準拠)。
- **色覚多様性**: カラーユニバーサルデザイン推奨配色セットを踏まえた色使いを行い、色だけに依存しない情報伝達を確保する。
- **意味付けの色**: 状態や意味を示す色は、色覚多様性を考慮して複数の視覚的区別手段(アイコン、テキスト、パターン)を併用する。

**タイポグラフィ設計**:

- **最小文字サイズ**: 原則として **16px** 未満の文字サイズを使用しない。
- **行間**: 学習障害や読字障害を考慮し、十分な行間(行高)を確保する。
- **ブロック幅**: 読みやすさを考慮し、テキストブロックの幅を適切に制限する(推奨: 45-75文字/行)。
- **フォントの選択**: 日本語フォントは読みやすさを優先し、システムフォントスタックを適切に設定する。

**レイアウトと余白**:

- **一貫した余白**: 要素間の余白を一貫したスケールで管理し、情報伝達を明確にする。
- **レスポンシブ設計**: デバイスやブラウザの制約を考慮した柔軟なレイアウトを設計する。

#### 8.3 スタイリングルール

- **MUIコンポーネント優先**: 標準のMUIコンポーネントを可能な限り使用。
- **Emotionによるカスタマイズ**: MUIの `sx` プロパティや `styled` APIでカスタムスタイルを適用。
- **CSSファイルの最小化**: `index.css` はグローバルリセットのみとし、コンポーネント単位でスタイルを管理。
- **デジタル庁基準の適用**: MUIのデフォルトスタイルがデジタル庁デザインシステムのアクセシビリティ基準を満たさない場合は、カスタムスタイルで上書きして基準に適合させる。

---

### 9. テスト設計 (Testing Design)

#### 9.1 テスト戦略

- **メインテスト手法**: フロントエンドのテストは **Jest** と **React Testing Library** で記述。
- **テストファイル**: コンポーネントと同じディレクトリに `*.test.tsx` を配置。
- **状態のカバー**: loading / error / empty / success の各状態をテストケースとして定義。
- **インタラクションテスト**: `@testing-library/user-event` でユーザー操作の検証を行う。
- **アクセシビリティテスト**: `@testing-library/jest-dom` を使用し、アクセシビリティ基準のテストを含む。

#### 9.2 テスト設計ルール

- **コンポーネント隔離**: 各テストはコンポーネントを隔離してテスト。
- **モックデータ**: 現実的なモックデータを使用して、様々なエッジケースをカバー。
- **スナップショットテスト**: 必要に応じてスナップショットテストを使用して、UIの意図しない変更を検出。
- **ユーザー視点のテスト**: 実装詳細ではなく、ユーザーがどのようにコンポーネントと相互作用するかをテストする。
- **テストのAAAパターン**: Arrange（準備）, Act（実行）, Assert（検証）の3段階でテストを構成する。

**コード例**:
```typescript
// ✅ Good: テストの実装例
import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Login from './Login';

describe('Login', () => {
  it('should display error message on failed login', async () => {
    // Arrange
    const user = userEvent.setup();
    render(<Login />);

    // Act
    await user.type(screen.getByLabelText(/username/i), 'test');
    await user.type(screen.getByLabelText(/password/i), 'wrong');
    await user.click(screen.getByRole('button', {name: /sign in/i}));

    // Assert
    expect(screen.getByRole('alert')).toBeInTheDocument();
    expect(screen.getByText(/authentication failed/i)).toBeInTheDocument();
  });
});
```

---

### 10. パフォーマンス設計 (Performance Design)

#### 10.1 リレンダリング最適化

- **useMemo/useCallback**: 計算コストの高い処理や関数の参照安定化に使用。
- **React.memo**: Propsが変更されない場合は再レンダリングしないように最適化。
- **不要な依存の排除**: `useEffect` の依存配列を最小限に抑える。
- **最適化のバランス**: 過剰な最適化はコードの可読性を損なうため、実際のパフォーマンス測定に基づいて適用する。

**コード例**:
```typescript
// ✅ Good: useMemo/useCallbackの適切な使用例
import {useMemo, useCallback} from 'react';

interface ProductListProps {
  products: Product[];
  onProductClick: (id: number) => void;
}

const ProductList: React.FC<ProductListProps> = ({products, onProductClick}) => {
  // 計算コストの高い処理をメモ化
  const sortedProducts = useMemo(
    () => products.sort((a, b) => a.price - b.price),
    [products]
  );

  // 関数の参照を安定化
  const handleProductClick = useCallback(
    (id: number) => {
      onProductClick(id);
    },
    [onProductClick]
  );

  return (
    <ul>
      {sortedProducts.map(product => (
        <li key={product.id} onClick={() => handleProductClick(product.id)}>
          {product.name}
        </li>
      ))}
    </ul>
  );
};

export default React.memo(ProductList);
```

#### 10.2 コードスプリット

- **動的インポート**: `React.lazy` と `Suspense` でページ単位でコードスプリットを実施。
- **ルートの分割**: 各ページを別チャンクに分割し、初期ロード時間を短縮。

**コード例**:
```typescript
// ✅ Good: コードスプリットの実装例
import {lazy, Suspense} from 'react';
import {Route, Routes} from 'react-router-dom';

const Dashboard = lazy(() => import('./pages/Dashboard'));
const UserList = lazy(() => import('./pages/users/UserList'));

const AppRoutes = () => (
  <Suspense fallback={<LoadingSpinner />}>
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/users" element={<UserList />} />
    </Routes>
  </Suspense>
);
```

#### 10.3 パフォーマンス計測

- **web-vitals**: Core Web Vitalsを計測し、パフォーマンス指標をモニタリング。
- **Lighthouse**: 定期的なパフォーマンス評価を実施。
- **React DevTools Profiler**: コンポーネントのレンダリングパフォーマンスを分析する。

---

### 11. セキュリティ設計 (Security Design)

#### 11.1 クライアントサイドセキュリティ

- **XSS対策**: ユーザー入力を直接 `dangerouslySetInnerHTML` で使用しない。
- **入力サニタイズ**: ユーザー入力は適切にバリデーション・サニタイズ。
- **機密情報の保護**: APIキーやトークンはクライアント側に露出しないよう設計。

#### 11.2 認証・認可

- **トークン管理**: 認証トークンは安全に保存し、リクエスト時に自動付与。
- **セッション管理**: トークン期限切れ時の再認証フローを設計。
- **権限ベースの表示制御**: ユーザーの権限に基づいてUI要素の表示・非表示を制御。

---

### 12. アクセシビリティ設計 (Accessibility Design)

デジタル庁デザインシステムのアクセシビリティガイドラインを基準に、WCAG 2.2 AAおよびJIS X 8341-3:2016に準拠した設計を行います。

#### 12.1 アクセシビリティファースト原則

デジタル庁デザインシステムは「アクセシビリティファースト」を標榜し、行政サービスは替えがきかないため、すべてのユーザーがサービスを利用できることを最優先します。

- **WCAG 2.2準拠**: 国際的なウェブアクセシビリティ基準WCAG 2.2の達成基準AAレベルに準拠する。
- **JIS X 8341-3:2016準拠**: 日本のウェブアクセシビリティ規格に準拠する。
- **WAI-ARIA 1.2準拠**: インタラクティブなUIコンポーネントのアクセシビリティを確保する。
- **最新仕様の追跡**: WCAG 3.0、WAI-ARIA 1.3、HTML Standard、CSS Living Standardなどの最新動向を注視し、アクセシビリティに寄与する技術を積極的に取り入れる。

#### 12.2 具体的なアクセシビリティ基準

**視覚的アクセシビリティ**:

- **カラーコントラスト**: テキストと背景のコントラスト比は **4.5:1** 以上(標準テキスト)、**3:1** 以上(大きなテキスト)を確保する。
- **色覚多様性**: 色だけに依存しない情報伝達を確保する。カラーユニバーサルデザイン推奨配色セットを参照する。
- **文字サイズ**: 原則として **16px** 未満の文字サイズを使用しない。
- **ズーム対応**: ブラウザのズーム機能(200%まで)に対応し、情報が切り詰められないようにする。

**キーボード操作**:

- **タブフォーカス**: すべてのインタラクティブ要素をキーボードのTabキーでアクセス可能にする。
- **フォーカス表示**: フォーカスインジケーターを明確に表示し、フォーカス順序が論理的であることを確保する。
- **キーボードショートカット**: 主要な操作にキーボードショートカットを提供する(任意)。

**スクリーンリーダー対応**:

- **ARIA属性**: 適切なARIA属性(`aria-label`, `aria-labelledby`, `aria-describedby`など)を使用して、スクリーンリーダーでの意味のある読み上げを確保する。
- **代替テキスト**: 画像、アイコン、装飾的な要素に適切な代替テキストまたは空のalt属性を設定する。
- **ライブリージョン**: 動的に更新されるコンテンツには`aria-live`属性を使用して、スクリーンリーダーに通知する。

**フォームアクセシビリティ**:

- **ラベルの関連付け**: すべてのフォームコントロールに明確なラベルを関連付ける。
- **エラーメッセージ**: 入力エラーは明確なテキストメッセージで通知し、`aria-invalid`や`aria-describedby`で関連付ける。
- **必須項目の表示**: 必須項目を明確に示し、視覚的表現とテキストの両方で通知する。

**コード例**:
```typescript
// ✅ Good: フォームアクセシビリティの実装例
import {TextField, FormHelperText} from '@mui/material';

const MyForm: React.FC = () => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');

  return (
    <TextField
      label="メールアドレス"
      value={email}
      onChange={(e) => setEmail(e.target.value)}
      error={!!error}
      helperText={error}
      aria-describedby={error ? 'email-error' : undefined}
      aria-invalid={!!error}
    />
    {error && (
      <FormHelperText id="email-error" error>
        {error}
      </FormHelperText>
    )}
  );
};
```

#### 12.3 アクセシビリティチェックリスト

デジタル庁デザインシステムのガイドラインに基づいたチェックリスト:

- [ ] 画像に代替テキスト（alt属性）が設定されている
- [ ] フォームコントロールにラベルが関連されている
- [ ] フォーカス順序が論理的である
- [ ] キーボードのみで操作可能である
- [ ] カラーコントラストがWCAG基準(4.5:1以上)を満たしている
- [ ] 文字サイズが16px以上である
- [ ] ARIA属性が適切に使用されている
- [ ] スクリーンリーダーで意味のある読み上げが可能である
- [ ] 動的コンテンツの更新がスクリーンリーダーに通知される
- [ ] ズーム(200%)時に情報が切り詰められない
- [ ] 色だけに依存しない情報伝達が行われている
- [ ] フォーカスインジケーターが明確に表示されている

---

### 13. 設計チェックリスト (Design Checklist)

設計時・実装時に以下の項目を確認してください。

#### 基本項目

- [ ] コンポーネントの責務が明確で単一である
- [ ] Propsの型定義が明確でデフォルト値が設定されている
- [ ] `any`型を使用していない
- [ ] 副作用の管理が適切でクリーンアップ関数が実装されている
- [ ] ステート管理のスコープが適切（ローカル/グローバル）
- [ ] ルーティング構造が一貫している
- [ ] API通信がサービス層で一元管理されている
- [ ] エラーハンドリングとローディング状態が適切に処理されている
- [ ] テストがloading/error/empty/successをカバーしている
- [ ] パフォーマンス最適化（useMemo, useCallback, React.memo）が適切に適用されている
- [ ] セキュリティ対策（XSS, 入力サニタイズ, トークン管理）が講じられている
- [ ] テーマ設定が `theme.ts` で一元管理されている
- [ ] 環境別設定が `.env.*` で適切に分離されている

#### デジタル庁デザインシステム基準

デジタル庁デザインシステムのアクセシビリティガイドラインに基づくチェック項目:

- [ ] カラーコントラスト比が4.5:1以上(標準テキスト)、3:1以上(大きなテキスト)を満たしている
- [ ] 文字サイズが16px以上である
- [ ] 色だけに依存しない情報伝達が行われている(色覚多様性対応)
- [ ] すべてのインタラクティブ要素がキーボード操作可能である
- [ ] フォーカス順序が論理的で、フォーカスインジケーターが明確に表示されている
- [ ] ARIA属性が適切に使用され、スクリーンリーダーで意味のある読み上げが可能である
- [ ] 画像やアイコンに適切な代替テキストが設定されている
- [ ] フォームコントロールにラベルが関連付けられている
- [ ] 動的コンテンツの更新がスクリーンリーダーに通知される(aria-live使用)
- [ ] ブラウザのズーム(200%)時に情報が切り詰められない
- [ ] 行間とブロック幅が読みやすさを考慮して設定されている
- [ ] デジタル庁デザインシステムの最新ガイドライン([design.digital.go.jp](https://design.digital.go.jp))を参照している
