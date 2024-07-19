目次
---

# Jpaとは

# Entity

## 主キーの扱い

## カラム

## アソシエート

### 1:1

### 1:n

### n:m

## アノテーション

### Entity

### Table

### Embedable

### Embeded

### Enum

### Id

## Immutable

## 実装例

```java

@Entity
@Table(name = "parties")
class Party {

  @Id
  private SnowflakeId id;
  @Embeded
  @Attribute(name = "simpleName")
  private SimpleName simpleName;

}

```

# Repository

## メソッド名

## 証跡

