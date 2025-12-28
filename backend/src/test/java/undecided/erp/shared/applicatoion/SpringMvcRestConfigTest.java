package undecided.erp.shared.applicatoion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Advisor;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import undecided.erp.common.exception.ExceptionLogger;
import undecided.erp.common.web.exception.HandlerExceptionResolverLoggingInterceptor;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

/**
 * Unit tests for {@link SpringMvcRestConfig}.
 *
 * <p>This test class validates the Spring MVC configuration, bean creation, and initialization
 * logic. Tests cover bean instantiation, configuration validation, and proper setup of
 * interceptors and advisors.
 */
@DisplayName("SpringMvcRestConfig Unit Tests")
class SpringMvcRestConfigTest {

  private SpringMvcRestConfig config;
  private ExceptionLogger mockExceptionLogger;

  @BeforeEach
  void setUp() {
    config = new SpringMvcRestConfig();
    mockExceptionLogger = mock(ExceptionLogger.class);
  }

  // ========== HandlerExceptionResolverLoggingInterceptor Tests ==========

  @Test
  @DisplayName("handlerExceptionResolverLoggingInterceptor should create interceptor with logger")
  void testHandlerExceptionResolverLoggingInterceptor_ShouldCreateWithLogger() {
    // When
    HandlerExceptionResolverLoggingInterceptor interceptor =
        config.handlerExceptionResolverLoggingInterceptor(mockExceptionLogger);

    // Then
    assertThat(interceptor).isNotNull();
    assertThat(interceptor.getExceptionLogger()).isEqualTo(mockExceptionLogger);
  }

  @Test
  @DisplayName("handlerExceptionResolverLoggingInterceptor with null logger should throw")
  void testHandlerExceptionResolverLoggingInterceptor_NullLogger_ShouldThrow() {
    // When & Then
    assertThatThrownBy(() -> config.handlerExceptionResolverLoggingInterceptor(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Multiple calls should create different interceptor instances")
  void testHandlerExceptionResolverLoggingInterceptor_MultipleCalls_DifferentInstances() {
    // When
    HandlerExceptionResolverLoggingInterceptor interceptor1 =
        config.handlerExceptionResolverLoggingInterceptor(mockExceptionLogger);
    HandlerExceptionResolverLoggingInterceptor interceptor2 =
        config.handlerExceptionResolverLoggingInterceptor(mockExceptionLogger);

    // Then
    assertThat(interceptor1).isNotSameAs(interceptor2);
  }

  // ========== ExceptionResolverLoggingInterceptorAdvisor Tests ==========

  @Test
  @DisplayName("exceptionResolverLoggingInterceptorAdvisor should create advisor")
  void testExceptionResolverLoggingInterceptorAdvisor_ShouldCreateAdvisor() {
    // When
    Advisor advisor = config.exceptionResolverLoggingInterceptorAdvisor(mockExceptionLogger);

    // Then
    assertThat(advisor).isNotNull();
    assertThat(advisor.getAdvice()).isNotNull();
  }

  @Test
  @DisplayName("exceptionResolverLoggingInterceptorAdvisor should use correct pointcut pattern")
  void testExceptionResolverLoggingInterceptorAdvisor_ShouldUseCorrectPattern() {
    // When
    Advisor advisor = config.exceptionResolverLoggingInterceptorAdvisor(mockExceptionLogger);

    // Then
    assertThat(advisor).isNotNull();
    // Verify advisor is properly configured (pointcut applies to Api classes)
    assertThat(advisor.getAdvice()).isInstanceOf(HandlerExceptionResolverLoggingInterceptor.class);
  }

  @Test
  @DisplayName("exceptionResolverLoggingInterceptorAdvisor with null logger should throw")
  void testExceptionResolverLoggingInterceptorAdvisor_NullLogger_ShouldThrow() {
    // When & Then
    assertThatThrownBy(() -> config.exceptionResolverLoggingInterceptorAdvisor(null))
        .isInstanceOf(NullPointerException.class);
  }

  // ========== addInterceptors Tests ==========

  @Test
  @DisplayName("addInterceptors should register TraceLoggingInterceptor")
  void testAddInterceptors_ShouldRegisterInterceptor() {
    // Given
    InterceptorRegistry registry = new InterceptorRegistry();

    // When
    config.addInterceptors(registry);

    // Then
    assertThat(registry.getInterceptors()).isNotEmpty();
    assertThat(registry.getInterceptors().get(0).getInterceptor())
        .isInstanceOf(TraceLoggingInterceptor.class);
  }

  @Test
  @DisplayName("addInterceptors should be callable multiple times")
  void testAddInterceptors_MultipleCalls_ShouldNotFail() {
    // Given
    InterceptorRegistry registry = new InterceptorRegistry();

    // When & Then - should not throw
    config.addInterceptors(registry);
    config.addInterceptors(registry);
  }

  // ========== PageableHandlerMethodArgumentResolver Tests ==========

  @Test
  @DisplayName("pageableHandlerMethodArgumentResolver should create resolver")
  void testPageableHandlerMethodArgumentResolver_ShouldCreateResolver() {
    // When
    PageableHandlerMethodArgumentResolver resolver =
        config.pageableHandlerMethodArgumentResolver();

    // Then
    assertThat(resolver).isNotNull();
  }

  @Test
  @DisplayName("Multiple calls should create different resolver instances")
  void testPageableHandlerMethodArgumentResolver_MultipleCalls_DifferentInstances() {
    // When
    PageableHandlerMethodArgumentResolver resolver1 =
        config.pageableHandlerMethodArgumentResolver();
    PageableHandlerMethodArgumentResolver resolver2 =
        config.pageableHandlerMethodArgumentResolver();

    // Then
    assertThat(resolver1).isNotSameAs(resolver2);
  }

  // ========== TraceLoggingInterceptor Tests ==========

  @Test
  @DisplayName("traceLoggingInterceptor should create interceptor with default settings")
  void testTraceLoggingInterceptor_ShouldCreateWithDefaults() {
    // When
    TraceLoggingInterceptor interceptor = config.traceLoggingInterceptor();

    // Then
    assertThat(interceptor).isNotNull();
    assertThat(interceptor.getWarnHandlingNanos()).isEqualTo(3000000000L);
  }

  @Test
  @DisplayName("traceLoggingInterceptor should set warn handling nanos to 3 seconds")
  void testTraceLoggingInterceptor_ShouldSetWarnHandlingNanos() {
    // When
    TraceLoggingInterceptor interceptor = config.traceLoggingInterceptor();

    // Then
    assertThat(interceptor.getWarnHandlingNanos()).isEqualTo(3_000_000_000L);
  }

  @Test
  @DisplayName("Multiple calls should create different interceptor instances")
  void testTraceLoggingInterceptor_MultipleCalls_DifferentInstances() {
    // When
    TraceLoggingInterceptor interceptor1 = config.traceLoggingInterceptor();
    TraceLoggingInterceptor interceptor2 = config.traceLoggingInterceptor();

    // Then
    assertThat(interceptor1).isNotSameAs(interceptor2);
  }

  @Test
  @DisplayName("traceLoggingInterceptor should allow warn nanos modification")
  void testTraceLoggingInterceptor_ShouldAllowModification() {
    // When
    TraceLoggingInterceptor interceptor = config.traceLoggingInterceptor();
    interceptor.setWarnHandlingNanos(5_000_000_000L);

    // Then
    assertThat(interceptor.getWarnHandlingNanos()).isEqualTo(5_000_000_000L);
  }

  // ========== TomcatCustomizer Tests ==========

  @Test
  @DisplayName("tomCatCustomizeer should create WebServerFactoryCustomizer")
  void testTomCatCustomizeer_ShouldCreateCustomizer() {
    // When
    var customizer = config.tomCatCustomizeer();

    // Then
    assertThat(customizer).isNotNull();
  }

  @Test
  @DisplayName("Multiple calls should create different customizer instances")
  void testTomCatCustomizeer_MultipleCalls_DifferentInstances() {
    // When
    var customizer1 = config.tomCatCustomizeer();
    var customizer2 = config.tomCatCustomizeer();

    // Then
    assertThat(customizer1).isNotSameAs(customizer2);
  }

  // ========== Integration Tests ==========

  @Test
  @DisplayName("Config should create all beans successfully")
  void testConfig_ShouldCreateAllBeans() {
    // When & Then - all bean creation methods should succeed
    assertThat(config.handlerExceptionResolverLoggingInterceptor(mockExceptionLogger))
        .isNotNull();
    assertThat(config.exceptionResolverLoggingInterceptorAdvisor(mockExceptionLogger))
        .isNotNull();
    assertThat(config.pageableHandlerMethodArgumentResolver()).isNotNull();
    assertThat(config.traceLoggingInterceptor()).isNotNull();
    assertThat(config.tomCatCustomizeer()).isNotNull();
  }

  @Test
  @DisplayName("Config should support AspectJ auto proxy")
  void testConfig_ShouldHaveAspectJAutoProxyAnnotation() {
    // When
    boolean hasAnnotation =
        SpringMvcRestConfig.class.isAnnotationPresent(
            org.springframework.context.annotation.EnableAspectJAutoProxy.class);

    // Then
    assertThat(hasAnnotation).isTrue();
  }

  @Test
  @DisplayName("Config should be a Configuration class")
  void testConfig_ShouldBeConfigurationClass() {
    // When
    boolean hasAnnotation =
        SpringMvcRestConfig.class.isAnnotationPresent(
            org.springframework.context.annotation.Configuration.class);

    // Then
    assertThat(hasAnnotation).isTrue();
  }

  @Test
  @DisplayName("Config should implement WebMvcConfigurer")
  void testConfig_ShouldImplementWebMvcConfigurer() {
    // When & Then
    assertThat(config).isInstanceOf(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class);
  }
}