package com.github.mizo432.shared.sample.jpa.feature.dbobject;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SampleSynonymのテスト")
class SampleSynonymTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("有効な引数でSampleSynonymが正常に作成される")
    void shouldCreateSampleSynonymWithValidArgs() {
      // Arrange
      Long partyId = 1L;
      String partyType = "PERSON";
      String simpleName = "テストパーティ";

      // Act
      SampleSynonym sampleSynonym = new SampleSynonym(partyId, partyType, simpleName);

      // Assert
      assertThat(sampleSynonym).isNotNull();
      assertThat(sampleSynonym.getPartyId()).isEqualTo(partyId);
      assertThat(sampleSynonym.getPartyType()).isEqualTo(partyType);
      assertThat(sampleSynonym.getSimpleName()).isEqualTo(simpleName);
    }

    @Test
    @DisplayName("複数のSampleSynonymが異なる値で作成される")
    void shouldCreateMultipleSampleSynonyms() {
      // Act
      SampleSynonym synonym1 = new SampleSynonym(1L, "PERSON", "パーティA");
      SampleSynonym synonym2 = new SampleSynonym(2L, "COMPANY", "パーティB");

      // Assert
      assertThat(synonym1.getPartyId()).isNotEqualTo(synonym2.getPartyId());
      assertThat(synonym1.getPartyType()).isNotEqualTo(synonym2.getPartyType());
      assertThat(synonym1.getSimpleName()).isNotEqualTo(synonym2.getSimpleName());
    }
  }

  @Nested
  @DisplayName("アノテーションのテスト")
  class AnnotationTest {

    @Test
    @DisplayName("@Entityアノテーションが付与されている")
    void shouldHaveEntityAnnotation() {
      // Arrange & Act
      Entity entity = SampleSynonym.class.getAnnotation(Entity.class);

      // Assert
      assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("@Table.nameはsyn_partiesに設定されている")
    void shouldTableNameBeSet() {
      // Arrange & Act
      Table table = SampleSynonym.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("syn_parties");
      assertThat(table.schema()).isEqualTo("undecided_as");
    }

    @Test
    @DisplayName("@Immutableアノテーションが付与されている")
    void shouldHaveImmutableAnnotation() {
      // Arrange & Act
      Immutable immutable = SampleSynonym.class.getAnnotation(Immutable.class);

      // Assert
      assertThat(immutable).isNotNull();
    }
  }

  @Nested
  @DisplayName("ゲッターのテスト")
  class GetterTest {

    @Test
    @DisplayName("partyIdが取得できる")
    void shouldGetPartyId() {
      // Arrange
      Long partyId = 123L;
      SampleSynonym sampleSynonym = new SampleSynonym(partyId, "PERSON", "テスト");

      // Act & Assert
      assertThat(sampleSynonym.getPartyId()).isEqualTo(partyId);
    }

    @Test
    @DisplayName("partyTypeが取得できる")
    void shouldGetPartyType() {
      // Arrange
      String partyType = "COMPANY";
      SampleSynonym sampleSynonym = new SampleSynonym(1L, partyType, "テスト");

      // Act & Assert
      assertThat(sampleSynonym.getPartyType()).isEqualTo(partyType);
    }

    @Test
    @DisplayName("simpleNameが取得できる")
    void shouldGetSimpleName() {
      // Arrange
      String simpleName = "テスト企業";
      SampleSynonym sampleSynonym = new SampleSynonym(1L, "COMPANY", simpleName);

      // Act & Assert
      assertThat(sampleSynonym.getSimpleName()).isEqualTo(simpleName);
    }
  }
}
