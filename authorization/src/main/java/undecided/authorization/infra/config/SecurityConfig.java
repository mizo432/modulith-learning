package undecided.authorization.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ設定
 *
 * <p>Spring Securityの設定を行うクラスです。 OAuth2認可サーバーと連携して、フォームログインとAPI認証をサポートします。 REQ-00018:
 * 認可エンドポイント、トークンエンドポイントはHTTPSのみで提供すること。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  /**
   * パスワードエンコーダーを提供します。
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * デフォルトのセキュリティフィルターチェーンを設定します。 フォームログインとAPI認証をサポートします。
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception 例外
   */
  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        // REQ-00018: HTTPからHTTPSへのリダイレクトを強制する
        .redirectToHttps(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers("/login", "/login/**")
                    .permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(formLogin -> formLogin.loginPage("/login").permitAll());

    return http.build();
  }
}
