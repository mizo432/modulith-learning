package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.generic.documentMgmt.spi.DocumentVersion;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテム添付ファイル API の統合テスト（#501 段階3）。
 *
 * <p>- 項目49: 添付 API を呼び出したとき、帳票管理モジュール経由で保存される
 * <p>- 項目50: 詳細 API を呼び出したとき、添付ファイル一覧が含まれる
 * <p>- 項目36〜38: 一覧のアクセス制御（メンバー可／ステークホルダー可／チーム外不可）
 * <p>- 項目39〜40: ダウンロードのアクセス制御（メンバー可／チーム外不可）
 * <p>- 項目26: 合計50MB超のアップロード拒否
 * <p>- 項目27: 存在しないバックログアイテムへの添付はエラー
 *
 * <p>DocumentStorageService は spi の公開インタフェースのため、テスト用の S3 実装に差し替える
 * （本物の DocumentStorageServiceImpl は段階2の統合テストで検証済み）。
 */
@Tag("medium")
@Testcontainers
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import({
  TestcontainersConfiguration.class,
  BacklogItemAttachmentsEndpointMediumTest.MinioTestConfig.class
})
@Transactional
@DirtiesContext
@DisplayName("バックログアイテム添付ファイルAPIの統合テスト")
class BacklogItemAttachmentsEndpointMediumTest {

  private static final int MINIO_PORT = 9000;
  private static final String BUCKET = "modulith-documents-test";
  private static final byte[] CONTENT = "添付ファイルの内容".getBytes(StandardCharsets.UTF_8);

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(1000L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);
  private static final SnowflakeId STAKEHOLDER_ID = SnowflakeId.of(900L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(999L);

  @Container
  private static final GenericContainer<?> MINIO =
      new GenericContainer<>("cgr.dev/chainguard/minio:latest")
          .withEnv("MINIO_ROOT_USER", "minioadmin")
          .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
          .withCommand("server", "/data")
          .withExposedPorts(MINIO_PORT)
          .waitingFor(Wait.forHttp("/minio/health/live").forPort(MINIO_PORT));

  private static S3Client s3Client;

  @BeforeAll
  static void setUpMinio() {
    s3Client = createS3Client();
    s3Client.createBucket(b -> b.bucket(BUCKET));
  }

  @AfterAll
  static void tearDownMinio() {
    if (s3Client != null) {
      s3Client.close();
    }
  }

  private static S3Client createS3Client() {
    return S3Client.builder()
        .endpointOverride(
            URI.create("http://" + MINIO.getHost() + ":" + MINIO.getMappedPort(MINIO_PORT)))
        .region(Region.US_EAST_1)
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create("minioadmin", "minioadmin")))
        .forcePathStyle(true)
        .build();
  }

  /** テスト用ストレージ実装（spi インタフェースの S3 実装）。 */
  static class TestDocumentStorageService implements DocumentStorageService {

    private final S3Client s3;

    TestDocumentStorageService(S3Client s3) {
      this.s3 = s3;
    }

    @Override
    public String upload(
        SnowflakeId documentId,
        DocumentVersion version,
        String fileName,
        String contentType,
        InputStream content,
        long contentLength) {
      String key = documentId.value() + "/v" + version.value() + "/" + fileName;
      s3.putObject(
          PutObjectRequest.builder().bucket(BUCKET).key(key).contentType(contentType).build(),
          RequestBody.fromInputStream(content, contentLength));
      return key;
    }

    @Override
    public InputStream download(String key) {
      return s3.getObject(GetObjectRequest.builder().bucket(BUCKET).key(key).build());
    }

    @Override
    public void delete(String key) {
      s3.deleteObject(b -> b.bucket(BUCKET).key(key));
    }
  }

  @TestConfiguration
  static class MinioTestConfig {

    @Bean
    @Primary
    S3Client documentStorageS3Client() {
      return createS3Client();
    }

    @Bean
    @Primary
    DocumentStorageService documentStorageService(S3Client documentStorageS3Client) {
      return new TestDocumentStorageService(documentStorageS3Client);
    }
  }

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private BacklogItemRepository backlogItemRepository;
  @Autowired private ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  @Autowired private BacklogItemDocumentRepository backlogItemDocumentRepository;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    jdbcTemplate.update("DELETE FROM undecided_ef.documents");
    backlogItemDocumentRepository.deleteAll();
    backlogItemRepository.deleteAll();
    scrumTeamSubsetRepository.deleteAll();

    scrumTeamSubsetRepository.save(
        ScrumTeamSubset.builder()
            .id(TEAM_ID)
            .name("Test Team")
            .poEmployeeId(PO_ID)
            .poName("PO")
            .smEmployeeId(SM_ID)
            .smName("SM")
            .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
            .stakeholderEmployeeIds(List.of(STAKEHOLDER_ID))
            .build());
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(ITEM_ID)
            .title("Test Item")
            .creatorName("Creator")
            .scrumTeamId(TEAM_ID)
            .value(50)
            .build());
  }

  private long attachAsMember(String fileName, byte[] content) throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", fileName, "application/pdf", content);
    String body =
        mockMvc
            .perform(
                multipart("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
                    .file(file)
                    .param("uploaderEmployeeId", String.valueOf(MEMBER_ID.value()))
                    .param("uploaderName", "メンバー"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    JsonNode json = objectMapper.readTree(body);
    return json.get("documentId").asLong();
  }

  @Nested
  @DisplayName("項目49・50: 添付と詳細表示")
  class AttachAndDetail {

    @Test
    @DisplayName("項目49: 添付APIを呼び出したとき、帳票管理モジュール経由で保存される")
    void shouldSaveViaDocumentModule() throws Exception {
      long documentId = attachAsMember("design.pdf", CONTENT);

      // documents.id は bytea 型のため、ID 指定ではなく全件数で保存を確認する（setUp で全削除済み）
      Integer documentCount =
          jdbcTemplate.queryForObject(
              "SELECT COUNT(*) FROM undecided_ef.documents", Integer.class);
      assertThat(documentCount).isEqualTo(1);
      assertThat(backlogItemDocumentRepository.findByBacklogItemId(ITEM_ID)).hasSize(1);
    }

    @Test
    @DisplayName("項目50: 詳細APIを呼び出したとき、添付ファイル一覧が含まれる")
    void shouldIncludeAttachmentsInDetail() throws Exception {
      attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(get("/api/v1/backlog-items/" + ITEM_ID.value()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.attachments.length()").value(1))
          .andExpect(jsonPath("$.attachments[0].fileName").value("design.pdf"));
    }
  }

  @Nested
  @DisplayName("項目36〜38: 一覧のアクセス制御")
  class ListAccessControl {

    @Test
    @DisplayName("項目36: チームメンバーが一覧を取得したとき、一覧が返される")
    void shouldListForMember() throws Exception {
      attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(
              get("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
                  .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].fileName").value("design.pdf"));
    }

    @Test
    @DisplayName("項目37: ステークホルダーが一覧を取得したとき、一覧が返される")
    void shouldListForStakeholder() throws Exception {
      attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(
              get("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
                  .param("actorEmployeeId", String.valueOf(STAKEHOLDER_ID.value())))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("項目38: チーム外の従業員が一覧を取得したとき、403となる")
    void shouldForbidListForOutsider() throws Exception {
      attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(
              get("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
                  .param("actorEmployeeId", String.valueOf(OUTSIDER_ID.value())))
          .andExpect(status().isForbidden());
    }
  }

  @Nested
  @DisplayName("項目39・40: ダウンロードのアクセス制御")
  class DownloadAccessControl {

    @Test
    @DisplayName("項目39: チームメンバーがダウンロードしたとき、ファイルが返される")
    void shouldDownloadForMember() throws Exception {
      long documentId = attachAsMember("design.pdf", CONTENT);

      byte[] downloaded =
          mockMvc
              .perform(
                  get(
                          "/api/v1/backlog-items/"
                              + ITEM_ID.value()
                              + "/attachments/"
                              + documentId
                              + "/download")
                      .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
              .andExpect(status().isOk())
              .andExpect(
                  result ->
                      assertThat(result.getResponse().getContentType())
                          .contains(MediaType.APPLICATION_PDF_VALUE))
              .andReturn()
              .getResponse()
              .getContentAsByteArray();

      assertThat(downloaded).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("項目40: チーム外の従業員がダウンロードしたとき、403となる")
    void shouldForbidDownloadForOutsider() throws Exception {
      long documentId = attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(
              get(
                      "/api/v1/backlog-items/"
                          + ITEM_ID.value()
                          + "/attachments/"
                          + documentId
                          + "/download")
                  .param("actorEmployeeId", String.valueOf(OUTSIDER_ID.value())))
          .andExpect(status().isForbidden());
    }
  }

  @Nested
  @DisplayName("項目26・27: サイズ制限と存在チェック")
  class Limits {

    @Test
    @DisplayName("項目26: 合計サイズが50MBを超えるアップロードは400で拒否される")
    void shouldRejectWhenTotalExceeds50Mb() throws Exception {
      byte[] nineMb = new byte[9 * 1024 * 1024];
      for (int i = 1; i <= 5; i++) {
        attachAsMember("file" + i + ".pdf", nineMb);
      }

      MockMultipartFile sixth =
          new MockMultipartFile("file", "file6.pdf", "application/pdf", nineMb);
      mockMvc
          .perform(
              multipart("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
                  .file(sixth)
                  .param("uploaderEmployeeId", String.valueOf(MEMBER_ID.value()))
                  .param("uploaderName", "メンバー"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("項目27: 存在しないバックログアイテムへの添付は400となる")
    void shouldRejectForNonexistentItem() throws Exception {
      MockMultipartFile file =
          new MockMultipartFile("file", "design.pdf", "application/pdf", CONTENT);
      mockMvc
          .perform(
              multipart("/api/v1/backlog-items/999999/attachments")
                  .file(file)
                  .param("uploaderEmployeeId", String.valueOf(MEMBER_ID.value()))
                  .param("uploaderName", "メンバー"))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("削除（添付解除）")
  class Detach {

    @Test
    @DisplayName("アップロード者が削除したとき、204が返り一覧から消える")
    void shouldDetachByUploader() throws Exception {
      long documentId = attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(
              delete("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments/" + documentId)
                  .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
          .andExpect(status().isNoContent());

      mockMvc
          .perform(
              get("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
                  .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("アップロード者以外が削除したとき、403となる")
    void shouldForbidDetachByNonUploader() throws Exception {
      long documentId = attachAsMember("design.pdf", CONTENT);

      mockMvc
          .perform(
              delete("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments/" + documentId)
                  .param("actorEmployeeId", String.valueOf(PO_ID.value())))
          .andExpect(status().isForbidden());
    }
  }

  @Nested
  @DisplayName("項目42・43: バージョン管理")
  class Versions {

    private JsonNode attach(String fileName, byte[] content, Long seriesId) throws Exception {
      MockMultipartFile file = new MockMultipartFile("file", fileName, "application/pdf", content);
      var request =
          multipart("/api/v1/backlog-items/" + ITEM_ID.value() + "/attachments")
              .file(file)
              .param("uploaderEmployeeId", String.valueOf(MEMBER_ID.value()))
              .param("uploaderName", "メンバー");
      if (seriesId != null) {
        request.param("seriesId", String.valueOf(seriesId));
      }
      String body =
          mockMvc
              .perform(request)
              .andExpect(status().isOk())
              .andReturn()
              .getResponse()
              .getContentAsString();
      return objectMapper.readTree(body);
    }

    @Test
    @DisplayName("項目42: バージョン一覧を取得したとき、その系列の全バージョンが返される")
    void shouldListAllVersions() throws Exception {
      JsonNode v1 = attach("design.pdf", CONTENT, null);
      long seriesId = v1.get("seriesId").asLong();
      JsonNode v2 = attach("design.pdf", "改訂版の内容".getBytes(StandardCharsets.UTF_8), seriesId);

      String body =
          mockMvc
              .perform(
                  get(
                          "/api/v1/backlog-items/"
                              + ITEM_ID.value()
                              + "/attachments/"
                              + v1.get("documentId").asLong()
                              + "/versions")
                      .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
              .andExpect(status().isOk())
              .andReturn()
              .getResponse()
              .getContentAsString();

      JsonNode versions = objectMapper.readTree(body);
      assertThat(versions.size()).isEqualTo(2);
      assertThat(versions.get(0).get("version").asInt()).isEqualTo(1);
      assertThat(versions.get(1).get("version").asInt()).isEqualTo(2);
      assertThat(versions.get(0).get("documentId").asLong())
          .isEqualTo(v1.get("documentId").asLong());
      assertThat(versions.get(1).get("documentId").asLong())
          .isEqualTo(v2.get("documentId").asLong());
    }

    @Test
    @DisplayName("項目43: 特定のバージョンをダウンロードしたとき、そのバージョンのファイルが返される")
    void shouldDownloadSpecificVersion() throws Exception {
      byte[] v2Content = "改訂版の内容".getBytes(StandardCharsets.UTF_8);
      JsonNode v1 = attach("design.pdf", CONTENT, null);
      long seriesId = v1.get("seriesId").asLong();
      JsonNode v2 = attach("design.pdf", v2Content, seriesId);

      byte[] downloadedV1 =
          mockMvc
              .perform(
                  get(
                          "/api/v1/backlog-items/"
                              + ITEM_ID.value()
                              + "/attachments/"
                              + v1.get("documentId").asLong()
                              + "/download")
                      .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
              .andExpect(status().isOk())
              .andReturn()
              .getResponse()
              .getContentAsByteArray();
      assertThat(downloadedV1).isEqualTo(CONTENT);

      byte[] downloadedV2 =
          mockMvc
              .perform(
                  get(
                          "/api/v1/backlog-items/"
                              + ITEM_ID.value()
                              + "/attachments/"
                              + v2.get("documentId").asLong()
                              + "/download")
                      .param("actorEmployeeId", String.valueOf(MEMBER_ID.value())))
              .andExpect(status().isOk())
              .andReturn()
              .getResponse()
              .getContentAsByteArray();
      assertThat(downloadedV2).isEqualTo(v2Content);
    }

    @Test
    @DisplayName("チーム外の従業員がバージョン一覧を取得したとき、403となる")
    void shouldForbidVersionsForOutsider() throws Exception {
      JsonNode v1 = attach("design.pdf", CONTENT, null);

      mockMvc
          .perform(
              get(
                      "/api/v1/backlog-items/"
                          + ITEM_ID.value()
                          + "/attachments/"
                          + v1.get("documentId").asLong()
                          + "/versions")
                  .param("actorEmployeeId", String.valueOf(OUTSIDER_ID.value())))
          .andExpect(status().isForbidden());
    }
  }
}
