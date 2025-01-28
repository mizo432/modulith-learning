目次
---

# Jpaとは

# Entity

```java

@Table(name = "SomeEntity")
@Entity
@Data
class AnyEntity {

  @Id
  private Lomg anyEntityId;

  private String code;

}
```

## 主キーの扱い

## カラム

## アソシエート

### 1:1

### 1:n

### n:m

## アノテーション

### Entity

### Table

### Embeddable

### Embedded

### Enum

### Id

### Inheritance

### MappedSuperclass

## 実装例

### Entity

```plantuml
@startuml
class IEntity
class BusinessEntity
class SystemEntity
IEntity <|. SystemEntity
IEntity <|.. BusinessEntity
class Ppt
BusinessEntity <|-- Ppt 
class Role
BusinessEntity <|-- Role 
class Role
BusinessEntity <|-- Description 
class Description
BusinessEntity <|-- Mi 
class Mi
BusinessEntity <|-- MiDetail 
class MiDetail

@enduml
```

### Ppt PartyPlaceThing

```plantuml
@startuml

abstract class Party

enum PartyType

Party --> PartyType
class Person
class Organization
Party <|-- Person
Party <|-- Organization
class OrganizationUnit

OrganizationUnit "0..*" -- "1" Organization

@enduml
```

### Repository

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
