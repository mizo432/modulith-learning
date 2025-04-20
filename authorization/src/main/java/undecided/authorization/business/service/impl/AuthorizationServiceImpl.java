package undecided.authorization.business.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.authorization.business.service.AuthorizationService;
import undecided.authorization.domain.model.user.UserRepository;

/**
 * 認可サービス実装
 * <p>
 * ユーザー認可に関するビジネスロジックを提供するサービス実装クラスです。
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorizationServiceImpl implements AuthorizationService {

  private final UserRepository userRepository;

  @Override
  public boolean hasRole(String username, String roleName) {
    return userRepository.findByUsername(username)
        .map(user -> user.hasRole(roleName))
        .orElse(false);
  }

  @Override
  public boolean hasPermission(String username, String permissionName) {
    return userRepository.findByUsername(username)
        .map(user -> user.hasPermission(permissionName))
        .orElse(false);
  }

  @Override
  public boolean isAuthorized(String username, String resource, String action) {
    // リソースとアクションの組み合わせに基づいて必要な権限を決定
    String requiredPermission = determineRequiredPermission(resource, action);

    // ユーザーが必要な権限を持っているかチェック
    return hasPermission(username, requiredPermission);
  }

  /**
   * リソースとアクションの組み合わせに基づいて必要な権限を決定します。 実際の実装では、リソースとアクションのマッピングテーブルなどを使用して
   * より複雑な権限決定ロジックを実装することができます。
   *
   * @param resource リソース
   * @param action アクション
   * @return 必要な権限
   */
  private String determineRequiredPermission(String resource, String action) {
    // 単純な実装例: リソース名とアクション名を組み合わせて権限名を生成
    return resource + ":" + action;
  }
}
