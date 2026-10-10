package undecided.ci;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/** CI設定の契約を検証する。GitHub Actionsの実行や外部サービスへの接続は行わない。 */
@Tag("small")
@DisplayName("サービスレジストリのCIワークフローのテスト")
class ServiceRegistryWorkflowTest {

  private static Map<?, ?> jobs;

  @BeforeAll
  static void loadWorkflow() throws IOException {
    // GradleのモジュールディレクトリとIDEのリポジトリルートの両方に対応する。
    Path directory = Path.of("").toAbsolutePath();
    while (directory != null && !Files.isRegularFile(directory.resolve(".github/workflows/ci.yml"))) {
      directory = directory.getParent();
    }
    assertThat(directory).as("検証対象のCIワークフローが存在すること").isNotNull();
    try (var reader = Files.newBufferedReader(directory.resolve(".github/workflows/ci.yml"))) {
      Map<?, ?> workflow = mapping(yaml().load(reader));
      jobs = mapping(workflow.get("jobs"));
    }
  }

  @Nested
  @DisplayName("ジョブの実行イベントのテスト")
  class JobEligibilityTest {

    @Test
    @DisplayName("小規模テストはプッシュ時にも実行対象になること")
    void shouldAllowSmallTestsOnPushAndPullRequest() {
      assertThat(job(RegistryJob.SMALL).get("if")).isNull();
    }

    @Test
    @DisplayName("統合テストはプルリクエスト時だけ実行対象になること")
    void shouldRestrictMediumTestsToPullRequests() {
      assertThat(job(RegistryJob.MEDIUM).get("if"))
          .isEqualTo("github.event_name == 'pull_request'");
    }
  }

  @Nested
  @DisplayName("変更検出のテスト")
  class ChangeDetectionTest {

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("モジュール直下と子階層を対象にして他モジュールの変更を含めないこと")
    void shouldFilterOnlyTheServiceRegistryTree(RegistryJob registryJob) {
      Map<?, ?> filter = action(registryJob, "dorny/paths-filter");
      assertThat(filter.get("id")).isEqualTo("changes");
      Object filters = mapping(filter.get("with")).get("filters");
      assertThat(filters).isInstanceOf(String.class);

      // 実際のアクションに渡すYAMLを検証し、独自のglob評価器は作らない。
      Map<?, ?> parsedFilters = mapping(yaml().load((String) filters));
      assertThat(parsedFilters.keySet()).isEqualTo(Set.of("service-registry"));
      assertThat(parsedFilters.get("service-registry"))
          .isEqualTo(List.of("service-registry/**"));
    }

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("チェックアウトと変更検出が未設定の出力に依存せず実行されること")
    void shouldDetectChangesBeforeReadingItsOutput(RegistryJob registryJob) {
      assertThat(action(registryJob, "actions/checkout").get("if")).isNull();
      assertThat(action(registryJob, "dorny/paths-filter").get("if")).isNull();
    }

    @Test
    @DisplayName("プッシュ時の比較に必要な直前のコミットを取得すること")
    void shouldFetchThePreviousCommitForSmallTests() {
      Object depth = mapping(action(RegistryJob.SMALL, "actions/checkout").get("with"))
          .get("fetch-depth");
      assertThat(depth).isInstanceOf(Number.class);
      assertThat(((Number) depth).intValue()).satisfiesAnyOf(
          value -> assertThat(value).isZero(),
          value -> assertThat(value).isGreaterThanOrEqualTo(2));
    }

    @Test
    @DisplayName("統合テストでは比較元の履歴を省略しないこと")
    void shouldFetchFullHistoryForMediumTests() {
      assertThat(mapping(action(RegistryJob.MEDIUM, "actions/checkout").get("with"))
          .get("fetch-depth")).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("テスト準備と実行条件のテスト")
  class TestPreparationTest {

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("変更検出の出力が文字列のtrueの場合だけ準備とテストを実行すること")
    void shouldRequireAnExplicitTrueOutputForEveryExpensiveStep(RegistryJob registryJob) {
      // 文字列のfalseも真と評価される単純な出力参照への退行を防ぐ。
      List<Map<?, ?>> guardedSteps = steps(registryJob).stream()
          .filter(step -> step.containsKey("run") || usesAction(step, "actions/setup-java"))
          .toList();

      assertThat(guardedSteps).hasSize(3).allSatisfy(step ->
          assertThat(step.get("if"))
              .as("%s: 変更なしや出力未設定の場合はスキップすること", step.get("name"))
              .isEqualTo("steps.changes.outputs.service-registry == 'true'"));
    }

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("Java 25とGradleキャッシュを使用すること")
    void shouldPrepareTheRequiredJavaToolchain(RegistryJob registryJob) {
      Map<?, ?> inputs = mapping(action(registryJob, "actions/setup-java").get("with"));

      assertThat(inputs.get("java-version")).isEqualTo("25");
      assertThat(inputs.get("distribution")).isEqualTo("temurin");
      assertThat(inputs.get("cache")).isEqualTo("gradle");
    }

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("変更検出後にJavaと実行権限を準備してからテストを実行すること")
    void shouldRunStepsInDependencyOrder(RegistryJob registryJob) {
      assertThat(steps(registryJob)).containsSubsequence(
          action(registryJob, "actions/checkout"),
          action(registryJob, "dorny/paths-filter"),
          action(registryJob, "actions/setup-java"),
          step(registryJob, candidate -> "chmod +x gradlew".equals(candidate.get("run"))),
          step(registryJob, candidate -> registryJob.command.equals(candidate.get("run"))));
    }
  }

  @Nested
  @DisplayName("Gradleテスト実行のテスト")
  class GradleExecutionTest {

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("対応するサイズのサービスレジストリのテストだけを実行すること")
    void shouldRunOnlyTheMatchingModuleTask(RegistryJob registryJob) {
      List<?> commands = steps(registryJob).stream()
          .filter(step -> step.containsKey("run"))
          .map(step -> step.get("run"))
          .toList();

      assertThat(commands).isEqualTo(List.of("chmod +x gradlew", registryJob.command));
    }

    @ParameterizedTest
    @EnumSource(RegistryJob.class)
    @DisplayName("テストや変更検出の失敗を成功として扱わないこと")
    void shouldPropagateFailuresFromBothJobsAndTheirSteps(RegistryJob registryJob) {
      assertThat(job(registryJob).get("continue-on-error")).isIn(null, false);
      assertThat(steps(registryJob)).allSatisfy(step ->
          assertThat(step.get("continue-on-error")).isIn(null, false));
    }
  }

  private static Yaml yaml() {
    return new Yaml(new SafeConstructor(new LoaderOptions()));
  }

  private static Map<?, ?> mapping(Object value) {
    assertThat(value).as("YAMLのマッピングであること").isInstanceOf(Map.class);
    return (Map<?, ?>) value;
  }

  private static Map<?, ?> job(RegistryJob registryJob) {
    assertThat(jobs.get(registryJob.id)).as("ジョブ %s が存在すること", registryJob.id).isNotNull();
    return mapping(jobs.get(registryJob.id));
  }

  private static List<Map<?, ?>> steps(RegistryJob registryJob) {
    Object value = job(registryJob).get("steps");
    assertThat(value).isInstanceOf(List.class);
    return ((List<?>) value).stream().map(ServiceRegistryWorkflowTest::mapping).toList();
  }

  private static Map<?, ?> action(RegistryJob registryJob, String action) {
    return step(registryJob, candidate -> usesAction(candidate, action));
  }

  private static boolean usesAction(Map<?, ?> step, String action) {
    return step.get("uses") instanceof String uses && uses.startsWith(action + "@");
  }

  private static Map<?, ?> step(RegistryJob registryJob, Predicate<Map<?, ?>> predicate) {
    List<Map<?, ?>> matches = steps(registryJob).stream().filter(predicate).toList();
    assertThat(matches).as("%s の対象ステップが一意であること", registryJob.id).hasSize(1);
    return matches.getFirst();
  }

  enum RegistryJob {
    SMALL("service-registry-test", "./gradlew :service-registry:test"),
    MEDIUM("service-registry-medium-test", "./gradlew :service-registry:mediumTest");

    private final String id;
    private final String command;

    RegistryJob(String id, String command) {
      this.id = id;
      this.command = command;
    }
  }
}
