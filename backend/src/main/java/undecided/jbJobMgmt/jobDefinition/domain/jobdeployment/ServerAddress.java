package undecided.jbJobMgmt.jobDefinition.domain.jobdeployment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * サーバーの接続先アドレスを表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerAddress {

  @Column(name = "host", nullable = false, length = 255)
  private String host;

  @Column(name = "port", nullable = false)
  private int port;

  @Column(name = "protocol", nullable = false, length = 10)
  private String protocol;

  public ServerAddress(String host, int port, String protocol) {
    if (host == null || host.isBlank()) {
      throw new IllegalArgumentException("Host must not be null or blank");
    }
    if (port < 1 || port > 65535) {
      throw new IllegalArgumentException("Port must be between 1 and 65535");
    }
    if (protocol == null || (!protocol.equals("http") && !protocol.equals("https"))) {
      throw new IllegalArgumentException("Protocol must be http or https");
    }
    this.host = host;
    this.port = port;
    this.protocol = protocol;
  }

  public String toUrl() {
    return protocol + "://" + host + ":" + port;
  }

  @Override
  public String toString() {
    return toUrl();
  }
}
