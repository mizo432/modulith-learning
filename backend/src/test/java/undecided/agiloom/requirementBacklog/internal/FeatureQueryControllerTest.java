package undecided.agiloom.requirementBacklog.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureQueryControllerのテスト")
class FeatureQueryControllerTest {

  private MockMvc mockMvc;

  @Mock private FeatureQueryService featureQueryService;

  private FeatureResponseConverter featureResponseConverter;

  private static final SnowflakeId FEATURE_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SCRUM_TEAM_ID = SnowflakeId.of(10L);

  private Feature feature;

  @BeforeEach
  void setUp() {
    featureResponseConverter = new FeatureResponseConverter();
    mockMvc = MockMvcBuilders.standaloneSetup(new FeatureQueryController(
            featureQueryService,
            featureResponseConverter))
        .setControllerAdvice(new FeatureExceptionHandler())
        .build();

    feature = Feature.builder()
        .id(FEATURE_ID)
        .name("Test Feature")
        .description("Test Description")
        .status(FeatureStatus.IN_PROGRESS)
        .scrumTeamId(SCRUM_TEAM_ID)
        .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
        .updatedAt(LocalDateTime.of(2026, 1, 1, 0, 0))
        .build();
  }

  @Nested
  @DisplayName("GET /api/v1/features のテスト")
  class GetFeaturesTest {

    @Test
    @DisplayName("チームのフィーチャー一覧を取得したとき、200と一覧が返される")
    void shouldGetFeatures() throws Exception {
      when(featureQueryService.findByScrumTeamId(SCRUM_TEAM_ID)).thenReturn(List.of(feature));

      mockMvc.perform(
              MockMvcRequestBuilders.get("/api/v1/features")
                  .param("scrumTeamId", String.valueOf(SCRUM_TEAM_ID.value())))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }
  }

  @Nested
  @DisplayName("GET /api/v1/features/{id} のテスト")
  class GetFeatureTest {

    @Test
    @DisplayName("進捗を含むフィーチャー詳細を取得したとき、200と進捗情報が返される")
    void shouldGetFeatureWithProgress() throws Exception {
      when(featureQueryService.findById(FEATURE_ID)).thenReturn(java.util.Optional.of(feature));
      when(featureQueryService.calculateProgress(FEATURE_ID))
          .thenReturn(new FeatureQueryService.FeatureProgress(5, 10, 50.0));
      when(featureQueryService.findBacklogItemIds(FEATURE_ID))
          .thenReturn(List.of(SnowflakeId.of(200L)));

      mockMvc.perform(
              MockMvcRequestBuilders.get("/api/v1/features/" + FEATURE_ID.value()))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("存在しないフィーチャーの詳細を取得したとき、400が返される")
    void shouldReturn400WhenFeatureNotFound() throws Exception {
      when(featureQueryService.findById(FEATURE_ID)).thenReturn(java.util.Optional.empty());

      mockMvc.perform(
              MockMvcRequestBuilders.get("/api/v1/features/" + FEATURE_ID.value()))
          .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
  }
}
