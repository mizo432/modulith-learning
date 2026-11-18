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
 * ScrumTeamメンバー更新エンドポイントの統合テストです。
 *
 * <p>項目18の期待ステータスは500（InternalServerError）です。
 * ScrumTeamCommandControllerには専用のExceptionHandlerがなく、
 * GlobalExceptionHandlerのException.classハンドラーが500を返すためです。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ScrumTeamメンバー更新エンドポイントの統合テスト")
class ScrumTeamMembersEndpointMediumTest {

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private ScrumTeamRepository scrumTeamRepository;

  @Autowired private ProductRepository productRepository;

  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  private SnowflakeId productId;
  private SnowflakeId scrumTeamId;
  private SnowflakeId poId;
  private SnowflakeId smId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    scrumTeamRepository.deleteAll();
    productRepository.deleteAll();

    productId = SnowflakeId.of(1000L);
    scrumTeamId = SnowflakeId.of(2000L);
    poId = SnowflakeId.of(100L);
    smId = SnowflakeId.of(200L);

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
            .poEmployeeId(poId)
            .poName("Test PO")
            .smEmployeeId(smId)
            .smName("Test SM")
            .product(product)
            .build();
    scrumTeamRepository.save(scrumTeam);
  }

  @Nested
  @DisplayName("PATCH /api/v1/products/{productId}/scrum-teams/{scrumTeamId}/members のテスト")
  class UpdateMembersEndpointTest {

    @Test
    @DisplayName("有効なリクエストを送信したとき、200が返りレスポンスにmemberEmployeeIdsが含まれる")
    void shouldReturn200WithMemberEmployeeIds() throws Exception {
      SnowflakeId memberId = SnowflakeId.of(300L);
      UpdateScrumTeamMembersRequest request =
          UpdateScrumTeamMembersRequest.builder()
              .memberEmployeeIds(List.of(memberId))
              .build();

      String responseBody =
          mockMvc.perform(
                  patch("/api/v1/products/" + productId.value() + "/scrum-teams/" + scrumTeamId.value() + "/members")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.memberEmployeeIds").isNotEmpty())
              .andReturn()
              .getResponse()
              .getContentAsString();

      // レスポンスにmemberEmployeeIdsが含まれることを確認
      assertThat(responseBody).contains("memberEmployeeIds");
    }

    @Test
    @DisplayName("存在しないscrumTeamIdを指定したとき、500が返る")
    void shouldReturn500WhenScrumTeamNotFound() throws Exception {
      SnowflakeId nonexistentId = SnowflakeId.of(9999L);
      UpdateScrumTeamMembersRequest request =
          UpdateScrumTeamMembersRequest.builder()
              .memberEmployeeIds(List.of(SnowflakeId.of(300L)))
              .build();

      mockMvc.perform(
              patch("/api/v1/products/" + productId.value() + "/scrum-teams/" + nonexistentId.value() + "/members")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("空リストを送信してPO/SMが設定されているとき、レスポンスのmemberEmployeeIdsにPO/SMが含まれる")
    void shouldContainPoAndSmWhenEmptyList() throws Exception {
      UpdateScrumTeamMembersRequest request =
          UpdateScrumTeamMembersRequest.builder().memberEmployeeIds(List.of()).build();

      String responseBody =
          mockMvc.perform(
                  patch("/api/v1/products/" + productId.value() + "/scrum-teams/" + scrumTeamId.value() + "/members")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.memberEmployeeIds").isNotEmpty())
              .andReturn()
              .getResponse()
              .getContentAsString();

      // レスポンスにPOとSMのIDが含まれることを確認
      assertThat(responseBody).contains(String.valueOf(poId.value()));
      assertThat(responseBody).contains(String.valueOf(smId.value()));
    }
  }
}
