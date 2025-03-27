# システムアーキテクチャメトリクスダッシュボード

## 概要

このダッシュボードは、Spring Modulithを使用したモジュラーモノリスアプリケーションのアーキテクチャメトリクスを確認するためのものです。Spring Modulithは、モジュラーモノリスアプローチを採用したアプリケーションの開発をサポートするフレームワークで、このダッシュボードを通じてアプリケーションのモジュール構造や依存関係を可視化し、アーキテクチャの健全性を評価するための情報を提供します。

## アクセス方法

ダッシュボードには以下のURLでアクセスできます：

```
http://localhost:8080/dashboard/architecture
```

## 機能

ダッシュボードは以下の機能を提供します：

### 1. 概要タブ

システムの概要情報を表示します。Spring Modulithを使用したモジュラーモノリスアプリケーションの基本的な説明と、このダッシュボードの目的について説明しています。

### 2. 生データタブ

Spring Modulithのアクチュエーターエンドポイントから取得した生のJSONデータを表示します。これには以下の情報が含まれます：

- **Modulith Data**: アプリケーションのモジュール構造に関する情報
- **Modulith Applications Data**: アプリケーション内のモジュール間の依存関係に関する情報

## 技術的な詳細

このダッシュボードは以下のコンポーネントで構成されています：

1. **Spring Boot Actuator**: アプリケーションのメトリクスやヘルスチェックなどの情報を提供するエンドポイント
2. **Spring Modulith Actuator**: Spring Modulithの特定のメトリクスを提供するエンドポイント
3. **Thymeleafテンプレート**: ダッシュボードのUIを提供するテンプレート
4. **Bootstrap**: UIのスタイリングに使用されるCSSフレームワーク

## 設定

ダッシュボードを使用するには、以下の設定が必要です：

1. `application.yml`ファイルでSpring Boot ActuatorとSpring Modulith Actuatorのエンドポイントを有効にする：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,modulith,modulithApplications
  endpoint:
    health:
      show-details: always
```

2. RestTemplateのBeanを提供する設定クラスを作成する：

```java
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

3. ダッシュボードのコントローラーを作成する：

```java
@Controller
@RequestMapping("/dashboard/architecture")
public class ArchitectureMetricsDashboardController {

    private final RestTemplate restTemplate;

    @Autowired
    public ArchitectureMetricsDashboardController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String dashboard(Model model) {
        // アクチュエーターエンドポイントからモジュール情報を取得
        String modulithData = restTemplate.getForObject("http://localhost:8080/actuator/modulith", String.class);
        String modulithApplicationsData = restTemplate.getForObject("http://localhost:8080/actuator/modulithApplications", String.class);

        // モデルにデータを追加
        model.addAttribute("modulithData", modulithData);
        model.addAttribute("modulithApplicationsData", modulithApplicationsData);
        model.addAttribute("pageTitle", "システムアーキテクチャメトリクスダッシュボード");

        return "architecture-dashboard";
    }
}
```

## 今後の拡張

このダッシュボードは以下の機能で拡張することができます：

1. モジュール間の依存関係を視覚化するグラフの追加
2. モジュールの詳細情報（クラス数、メソッド数など）の表示
3. アーキテクチャの健全性を評価するメトリクスの追加
4. モジュール間の依存関係の変更を追跡する履歴機能の追加
