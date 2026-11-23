package undecided.generic.accountMgmt.internal.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import undecided.generic.accountMgmt.spi.User;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("UserQueryImplのテスト")
@ExtendWith(MockitoExtension.class)
class UserQueryImplTest {

  @Mock private UserRepository userRepository;

  private MockRestServiceServer server;
  private UserQueryImpl userQueryImpl;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder();
    server = MockRestServiceServer.bindTo(builder).build();
    userQueryImpl = new UserQueryImpl(userRepository, builder.build());
  }

  @Test
  @DisplayName("ユーザー名で検索したとき、リポジトリの検索結果がそのまま返る")
  void shouldReturnRepositoryResultOnFindByUsername() {
    User user =
        User.create(SnowflakeId.EMPTY, "testuser", LocalDateTime.of(2026, 1, 1, 0, 0), null);
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

    Optional<User> result = userQueryImpl.findByUsername("testuser");

    assertThat(result).contains(user);
  }

  @Test
  @DisplayName("初回ログイン状態がtrueのとき、needsPasswordChangeはtrueとなる")
  void shouldReturnTrueWhenFirstLogin() {
    server
        .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "{\"username\":\"testuser\",\"isFirstLogin\":true}", MediaType.APPLICATION_JSON));

    assertThat(userQueryImpl.needsPasswordChange("testuser")).isTrue();

    server.verify();
  }

  @Test
  @DisplayName("初回ログイン状態がfalseのとき、needsPasswordChangeはfalseとなる")
  void shouldReturnFalseWhenNotFirstLogin() {
    server
        .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "{\"username\":\"testuser\",\"isFirstLogin\":false}", MediaType.APPLICATION_JSON));

    assertThat(userQueryImpl.needsPasswordChange("testuser")).isFalse();

    server.verify();
  }

  @Test
  @DisplayName("authorizationサービスが500を返すとき、needsPasswordChangeはfalseとなる")
  void shouldReturnFalseWhenAuthorizationServiceReturns500() {
    server
        .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThat(userQueryImpl.needsPasswordChange("testuser")).isFalse();
  }

  @Test
  @DisplayName("authorizationサービスへ接続できないとき、needsPasswordChangeはfalseとなる")
  void shouldReturnFalseWhenAuthorizationServiceUnreachable() {
    server
        .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withException(new IOException("Connection refused")));

    assertThat(userQueryImpl.needsPasswordChange("testuser")).isFalse();
  }
}
