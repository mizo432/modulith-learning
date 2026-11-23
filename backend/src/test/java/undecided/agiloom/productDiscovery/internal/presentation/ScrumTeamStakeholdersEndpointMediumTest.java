package undecided.agiloom.productDiscovery.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ScrumTeam のステークホルダー更新（PATCH members の stakeholderEmployeeIds）の統合テストです（#734）。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ScrumTeamステークホルダー更新エンドポイントの統合テスト")
class ScrumTeamStakeholdersEndpointMediumTest {

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private ScrumTeamRepository scrumTeamRepository;

  @Autowired private ProductRepository productRepository;

  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  private SnowflakeId productId;
  private SnowflakeId scrumTeamId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    scrumTeamRepository.deleteAll();
    productRepository.deleteAll();

    productId = SnowflakeId.of(1000L);
    scrumTeamId = SnowflakeId.of(2000L);

    Product product =
        Product.builder()
            .id(productId)
            .name("Test Product")
            .description("Test")
            .status(ProductStatus.ACTIVE)
            .build();
    productRepository.save(product);

    ScrumTeam scrumTeam =
        ScrumTeam.builder()
            .id(scrumTeamId)
            .name("Test Team")
            .businessDomain("Test Domain")
            .poEmployeeId(SnowflakeId.of(100L))
            .poName("Test PO")
            .smEmployeeId(SnowflakeId.of(200L))
            .smName("Test SM")
            .product(product)
            .build();
    scrumTeamRepository.save(scrumTeam);
  }

  @Nested
  @DisplayName("PATCH members のステークホルダー更新")
  class UpdateStakeholdersTest {

    @Test
    @DisplayName("stakeholderEmployeeIdsを送信したとき、200が返りレスポンスとDBに反映される")
    void shouldUpdateStakeholders() throws Exception {
      UpdateScrumTeamMembersRequest request =
          UpdateScrumTeamMembersRequest.builder()
              .memberEmployeeIds(List.of(SnowflakeId.of(300L)))
              .stakeholderEmployeeIds(List.of(SnowflakeId.of(900L), SnowflakeId.of(901L)))
              .build();

      mockMvc
          .perform(
              patch(
                      "/api/v1/products/"
                          + productId.value()
                          + "/scrum-teams/"
                          + scrumTeamId.value()
                          + "/members")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.stakeholderEmployeeIds").isNotEmpty());

      ScrumTeam saved = scrumTeamRepository.findById(scrumTeamId).orElseThrow();
      assertThat(saved.getStakeholderEmployeeIds())
          .containsExactlyInAnyOrder(SnowflakeId.of(900L), SnowflakeId.of(901L));
    }

    @Test
    @DisplayName("stakeholderEmployeeIdsを送信しないとき、既存のステークホルダーは変更されない")
    void shouldKeepStakeholdersWhenNotSent() throws Exception {
      ScrumTeam team = scrumTeamRepository.findById(scrumTeamId).orElseThrow();
      team.setStakeholderEmployeeIds(List.of(SnowflakeId.of(900L)));
      scrumTeamRepository.save(team);

      UpdateScrumTeamMembersRequest request =
          UpdateScrumTeamMembersRequest.builder()
              .memberEmployeeIds(List.of(SnowflakeId.of(300L)))
              .build();

      mockMvc
          .perform(
              patch(
                      "/api/v1/products/"
                          + productId.value()
                          + "/scrum-teams/"
                          + scrumTeamId.value()
                          + "/members")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      ScrumTeam saved = scrumTeamRepository.findById(scrumTeamId).orElseThrow();
      assertThat(saved.getStakeholderEmployeeIds()).containsExactly(SnowflakeId.of(900L));
    }

    @Test
    @DisplayName("DBのscrum_teamsのstakeholder_employee_idsカラムがBIGINT[]型であること")
    void shouldStakeholderColumnBeBigintArray() {
      // Flywayマイグレーションで追加されたカラムの型を確認する
      ScrumTeam saved = scrumTeamRepository.findById(scrumTeamId).orElseThrow();
      assertThat(saved.getStakeholderEmployeeIds()).isEmpty();
    }
  }
}
