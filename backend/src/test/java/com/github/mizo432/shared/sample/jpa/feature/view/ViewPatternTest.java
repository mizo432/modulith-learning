package com.github.mizo432.shared.sample.jpa.feature.view;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Viewパターンの単体テスト")
class ViewPatternTest {

  @Nested
  @DisplayName("SampleEmployeeSummaryViewのテスト")
  class SampleEmployeeSummaryViewTest {

    @Test
    @DisplayName("SampleEmployeeSummaryViewが正常に作成される")
    void shouldCreateEmployeeSummaryView() {
      // Arrange
      Long partyId = 1L;
      String partyType = "PERSON";
      String simpleMame = "テストパーティ";

      // Act
      SampleEmployeeSummaryView view = new SampleEmployeeSummaryView(
          partyId, partyType, simpleMame);

      // Assert
      assertThat(view).isNotNull();
      assertThat(view.getPartyId()).isEqualTo(partyId);
      assertThat(view.getPartyType()).isEqualTo(partyType);
      assertThat(view.getSimpleMame()).isEqualTo(simpleMame);
    }

    @Test
    @DisplayName("@Table.nameはv_party_summaryに設定されている")
    void shouldTableNameBeSet() {
      // Arrange & Act
      Table table = SampleEmployeeSummaryView.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("v_party_summary");
    }

    @Test
    @DisplayName("@Immutableアノテーションが付与されている")
    void shouldHaveImmutableAnnotation() {
      // Arrange & Act
      Immutable immutable = SampleEmployeeSummaryView.class.getAnnotation(Immutable.class);

      // Assert
      assertThat(immutable).isNotNull();
    }

    @Test
    @DisplayName("複数のSampleEmployeeSummaryViewが作成される")
    void shouldCreateMultipleViews() {
      // Act
      SampleEmployeeSummaryView view1 = new SampleEmployeeSummaryView(
          1L, "PERSON", "パーティA");
      SampleEmployeeSummaryView view2 = new SampleEmployeeSummaryView(
          2L, "COMPANY", "パーティB");

      // Assert
      assertThat(view1.getPartyId()).isNotEqualTo(view2.getPartyId());
      assertThat(view1.getPartyType()).isNotEqualTo(view2.getPartyType());
    }
  }

  @Nested
  @DisplayName("SamplePartySummaryMVのテスト")
  class SamplePartySummaryMVTest {

    @Test
    @DisplayName("SamplePartySummaryMVが正常に作成される")
    void shouldCreatePartySummaryMV() {
      // Arrange
      String partyType = "PERSON";
      Long partyCount = 100L;

      // Act
      SamplePartySummaryMV mv = new SamplePartySummaryMV(partyType, partyCount);

      // Assert
      assertThat(mv).isNotNull();
      assertThat(mv.getPartyType()).isEqualTo(partyType);
      assertThat(mv.getPartyCount()).isEqualTo(partyCount);
    }

    @Test
    @DisplayName("@Table.nameはmv_party_summaryに設定されている")
    void shouldTableNameBeSet() {
      // Arrange & Act
      Table table = SamplePartySummaryMV.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("mv_party_summary");
    }

    @Test
    @DisplayName("@Immutableアノテーションが付与されている")
    void shouldHaveImmutableAnnotation() {
      // Arrange & Act
      Immutable immutable = SamplePartySummaryMV.class.getAnnotation(Immutable.class);

      // Assert
      assertThat(immutable).isNotNull();
    }

    @Test
    @DisplayName("複数のSamplePartySummaryMVが作成される")
    void shouldCreateMultipleMVs() {
      // Act
      SamplePartySummaryMV mv1 = new SamplePartySummaryMV("PERSON", 100L);
      SamplePartySummaryMV mv2 = new SamplePartySummaryMV("COMPANY", 50L);

      // Assert
      assertThat(mv1.getPartyType()).isNotEqualTo(mv2.getPartyType());
      assertThat(mv1.getPartyCount()).isNotEqualTo(mv2.getPartyCount());
    }
  }
}
