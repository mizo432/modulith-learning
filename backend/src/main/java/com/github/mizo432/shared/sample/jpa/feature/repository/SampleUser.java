package com.github.mizo432.shared.sample.jpa.feature.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * リポジトリバリエーションのサンプルで使用されるユーザーエンティティです。
 *
 * <p>{@code CrudRepository} を使用する基本的なCRUD操作の例として使用します。
 */
@Entity
@Table(name = "sample_users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleUser {

  @Id private SnowflakeId userId;

  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "active", nullable = false)
  private Boolean active;

  public SampleUser(SnowflakeId userId, String email, String name) {
    this.userId = userId;
    this.email = email;
    this.name = name;
    this.active = true;
  }
}
