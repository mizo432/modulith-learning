# Redis設計ガイド

本ガイドでは、Spring Boot における Redis キャッシングの設計・実装におけるベストプラクティスを定義します。

## 1. 概要

Redis をキャッシュ層として利用することで、データベースへの負荷を軽減し、API のレスポンス時間を高速化できます。しかし、エンタープライズレベルの運用環境では、デフォルトの設定のまま利用するとメモリ枯渇、キャッシュ暴走、シリアライゼーションエラーなどの問題が発生します。

以下の点に注意して設計・実装します。

- **シリアライゼーション**: JDK デフォルトのシリアライゼーションは使用せず、JSON シリアライザを採用
- **TTL（Time-To-Live）**: 無制限の TTL は設定せず、すべてのキャッシュに有効期限を設定
- **キャッシュ暴走防止**: 大量の同時アクセスによるデータベースへの負荷集中を防止
- **コネクションプーリング**: Lettuce の接続プールを適切に調整し、高負荷環境でも安定して動作

## 2. Redis キャッシュ構成の基本パターン

Redis キャッシュマネージャーは、`@EnableCaching` アノテーションを付与した構成クラスで定義し、一元的に管理します。

### 2.1 推奨構成

```java
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(30))  // デフォルトの TTL を設定
        .disableCachingNullValues()        // null 値のキャッシュを無効化
        .serializeKeysWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer()));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(config)
        .build();
  }
}
```

### 2.2 キャッシュ名別の TTL 設定

キャッシュの特性に応じて、キャッシュ名ごとに異なる TTL を設定できます。

```java
@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
  RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(Duration.ofMinutes(30))
      .disableCachingNullValues()
      .serializeKeysWith(RedisSerializationContext.SerializationPair
          .fromSerializer(new StringRedisSerializer()))
      .serializeValuesWith(RedisSerializationContext.SerializationPair
          .fromSerializer(new GenericJackson2JsonRedisSerializer()));

  RedisCacheConfiguration shortTtlConfig = defaultConfig
      .entryTtl(Duration.ofMinutes(5));  // 短命キャッシュ

  RedisCacheConfiguration longTtlConfig = defaultConfig
      .entryTtl(Duration.ofHours(2));    // 長命キャッシュ

  return RedisCacheManager.builder(connectionFactory)
      .cacheDefaults(defaultConfig)
      .withCacheConfiguration("accounts", shortTtlConfig)
      .withCacheConfiguration("masterData", longTtlConfig)
      .build();
}
```

| キャッシュ名     | 推奨TTL      | 用途                           |
|:-----------------|:-------------|:-------------------------------|
| `accounts`       | 5 分         | 頻繁に更新される取引データ     |
| `masterData`     | 2 時間       | rarely 更新されるマスターデータ |
| `session`        | 30 分        | ユーザーセッション情報         |
| `searchResults`  | 10 分        | 検索結果キャッシュ             |

## 3. シリアライゼーションの設計

### 3.1 デフォルトシリアライゼーションの問題点

Spring Data Redis の設定でシリアライザを指定しない場合、デフォルトで `JdkSerializationRedisSerializer` が使用されます。

| 問題点           | 説明                                                         |
|:-----------------|:-------------------------------------------------------------|
| ペイロード肥大   | バイナリメタデータにより、オブジェクトサイズが 3〜5 倍に膨張 |
| バージョン管理   | クラス構造の変更で `InvalidClassException` が発生            |
| 可読性           | バイナリ形式のため、Redis 直接操作での確認が困難             |
| セキュリティ     | 不正なオブジェクトのデシリアライゼーションリスク             |

### 3.2 推奨シリアライザ

| シリアライザ                         | 用途         | 推奨度 |
|:-------------------------------------|:-------------|:-------|
| `GenericJackson2JsonRedisSerializer` | 値（バリュー）| ⭐⭐⭐  |
| `StringRedisSerializer`              | キー         | ⭐⭐⭐  |

**理由**:
- JSON 形式は人間が読める形式で、Redis 直接操作でのデバッグが容易
- クラス構造の変更に対する耐性が高い
- ペイロードサイズが小さい

## 4. キャッシュ暴走（Thundering Herd）の防止

### 4.1 問題の概要

キャッシュキーの有効期限が切れた際、多数の同時リクエストがすべてキャッシュミスとなり、データベースに集中アクセスする現象を「キャッシュ暴走（Thundering Herd）」と呼びます。

```
キャッシュ有効期限切れ
    ↓
5,000 スレッドが同時にデータベースにアクセス
    ↓
データベース CPU 100% → 接続プール枯渇 → アプリケーション全体が停止
```

### 4.2 防止策

| 手法             | 説明                                                         |
|:-----------------|:-------------------------------------------------------------|
| 分散ロック       | Redisson や Spring Integration Redis Locks を使用            |
| キャッシュ再構築 | 1 つのスレッドのみがデータベースにアクセスし、他は待機       |
| TTL のランダム化 | キャッシュの TTL にランダムなオフセットを追加                |

### 4.3 TTL ランダム化の実装例

```java
@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
  RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(Duration.ofMinutes(30))
      .disableCachingNullValues()
      // TTL にランダムなオフセットを追加（±5分）
      .computePrefixWith(cacheName -> cacheName.getBytes())
      .serializeKeysWith(RedisSerializationContext.SerializationPair
          .fromSerializer(new StringRedisSerializer()))
      .serializeValuesWith(RedisSerializationContext.SerializationPair
          .fromSerializer(new GenericJackson2JsonRedisSerializer()));

  return RedisCacheManager.builder(connectionFactory)
      .cacheDefaults(config)
      .build();
}
```

## 5. キャッシュの無効化と TTL 設計

### 5.1 TTL の設定原則

- **無制限の TTL は設定しない**: すべてのキャッシュに有効期限を設定
- **ビジネス要件に応じた TTL**: データの更新頻度に応じて適切な TTL を設定
- **デフォルト TTL の設定**: キャッシュ名ごとに明示的に TTL を設定しない場合のデフォルト値を設定

### 5.2 キャッシュ無効化のパターン

| パターン         | 説明                                                         |
|:-----------------|:-------------------------------------------------------------|
| `@CacheEvict`    | 書き込み時に明示的にキャッシュを削除                         |
| `@CachePut`      | キャッシュを更新                                             |
| `@Cacheable`     | キャッシュが存在しない場合にのみデータベースにアクセス        |

### 5.3 実装例

```java
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Cacheable(value = "accounts", key = "#accountId")
  public AccountDto getAccount(String accountId) {
    return accountRepository.findByIdOrThrow(accountId);
  }

  @CacheEvict(value = "accounts", key = "#command.accountId()")
  public void updateAccountBalance(UpdateBalanceCommand command) {
    accountRepository.applyUpdate(command);
  }
}
```

### 5.4 Null 値のキャッシュ防止

`@Cacheable` で null を返す場合、デフォルトでは null 自体がキャッシュされます。これを防止するため、`disableCachingNullValues()` を設定します。

```java
RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
    .disableCachingNullValues();  // null 値のキャッシュを無効化
```

## 6. コネクションプーリングの設計

### 6.1 Lettuce vs Jedis

| 特性             | Lettuce（推奨）                          | Jedis                          |
|:-----------------|:-----------------------------------------|:-------------------------------|
| 接続モデル       | Netty ベースの非同期接続                 | ブロッキング接続               |
| スレッド安全性   | スレッドセーフ                           | スレッド非セーフ               |
| パフォーマンス   | 高並列環境で優位                         | 単純な環境で十分               |
| Spring Boot 統合 | デフォルトクライアント                   | 明示的な設定が必要             |

### 6.2 推奨設定

`application.yml` での推奨設定例です。

```yaml
spring:
  data:
    redis:
      host: redis.production.cluster.local
      port: 6379
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 32      # アクティブ接続の最大数
          max-idle: 16        # アイドル接続の最大数
          min-idle: 4         # アイドル接続の最小数
          max-wait: -1ms      # 接続待機時間（-1 は無限待機）
```

### 6.3 接続プールの調整ガイドライン

| 設定           | 推奨値 | 説明                               |
|:---------------|:-------|:-----------------------------------|
| `max-active`   | 32     | アプリケーションの並列処理数に応じて調整 |
| `max-idle`     | 16     | `max-active` の半分程度            |
| `min-idle`     | 4      | 最低限のアイドル接続数を維持        |
| `max-wait`     | -1ms   | 無限待機または適切なタイムアウトを設定 |

### 6.4 ネットワーク分断時の対応

大規模なネットワーク分断や Redis のフェイルオーバー時に、Lettuce の接続プールがハングアップしないよう、タイムアウト設定を適切に設定します。

```yaml
spring:
  data:
    redis:
      timeout: 2000ms  # 接続タイムアウトを設定
```

## 7. セキュリティ設計

### 7.1 認証の設定

本番環境では Redis への接続に認証を設定します。

```yaml
spring:
  data:
    redis:
      host: redis.production.cluster.local
      port: 6379
      password: ${REDIS_PASSWORD}  # 環境変数から取得
```

### 7.2 SSL/TLS 接続

```yaml
spring:
  data:
    redis:
      ssl:
        enabled: true
      host: redis.production.cluster.local
      port: 6380
```

## 8. テスト戦略

### 8.1 単体テスト

- キャッシュの有効・無効時の動作を確認
- TTL 設定の検証
- Null 値のキャッシュ防止の確認

### 8.2 統合テスト

- Testcontainers で Redis コンテナを起動し、実際の Redis 環境でテスト
- キャッシュ暴走防止の動作確認
- コネクションプーリングの動作確認

### 8.3 負荷テスト

- 高並列環境でのキャッシュ動作を確認
- コネクションプールの調整値の最適化

## 9. チェックリスト

Redis キャッシュを導入する際に確認するチェックリストです。

- [ ] シリアライザは `GenericJackson2JsonRedisSerializer` を使用しているか
- [ ] キーのシリアライザは `StringRedisSerializer` を使用しているか
- [ ] すべてのキャッシュに TTL が設定されているか
- [ ] `disableCachingNullValues()` が設定されているか
- [ ] キャッシュ暴走防止の対策（分散ロック or TTL ランダム化）を実施しているか
- [ ] Lettuce の接続プール設定が適切か
- [ ] 接続タイムアウトが設定されているか
- [ ] 本番環境では認証が設定されているか
- [ ] 本番環境では SSL/TLS が有効化されているか
- [ ] キャッシュ無効化のロジックが適切か

## 10. まとめ

Redis キャッシュを効果的に利用するためには、以下の点を遵守してください。

- **JDK シリアライゼーションは使用しない**: `GenericJackson2JsonRedisSerializer` を使用し、ペイロード肥大やバージョン管理の問題を防止
- **TTL は必ず設定する**: 無制限の TTL はメモリリークの原因となるため、すべてのキャッシュに有効期限を設定
- **キャッシュ暴走を防止する**: 分散ロックや TTL ランダム化により、データベースへの集中アクセスを防止
- **接続プールを適切に管理する**: Lettuce の接続プールを適切に調整し、高負荷環境でも安定して動作
