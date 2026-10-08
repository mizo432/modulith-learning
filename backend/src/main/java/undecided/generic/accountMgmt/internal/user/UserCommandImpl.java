package undecided.generic.accountMgmt.internal.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.generic.accountMgmt.spi.ChangePasswordRequest;
import undecided.generic.accountMgmt.spi.User;
import undecided.generic.accountMgmt.spi.UserCommand;
import undecided.supporting.exception.BusinessException;

/** ユーザー操作の実装クラスです。 パスワード変更機能を提供します。 */
@RequiredArgsConstructor
@Service
public class UserCommandImpl implements UserCommand {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void changePassword(String username, ChangePasswordRequest request) {
    // パスワード検証
    if (!request.isValidPasswordLength()) {
      throw new BusinessException("新しいパスワードは8文字以上64文字以下で指定してください");
    }

    if (!request.isNewPasswordMatched()) {
      throw new BusinessException("新しいパスワードと確認用パスワードが一致しません");
    }

    // ユーザー検索
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません: " + username));

    // 初回ログインでない場合は、現在のパスワードを確認
    if (!user.needsPasswordChange()
        && !passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
      throw new BusinessException("現在のパスワードが正しくありません");
    }

    // パスワードをハッシュ化して変更
    String hashedPassword = passwordEncoder.encode(request.newPassword());
    user.changePassword(hashedPassword);
    userRepository.save(user);
  }
}
