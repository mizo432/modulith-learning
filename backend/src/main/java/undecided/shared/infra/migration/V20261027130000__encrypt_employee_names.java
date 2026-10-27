package undecided.shared.infra.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * employeesテーブルのfirstNameとlastNameカラムの既存データを暗号化するFlyway Javaマイグレーションです。
 *
 * <p>V20261027080000のマイグレーションでカラム型をTEXTに変更した後、このマイグレーションで既存の平文データを暗号化します。
 * 暗号化されていないデータ（復号化できないデータ）を識別し、それらを暗号化して更新します。
 */
public class V20261027130000__encrypt_employee_names extends BaseJavaMigration {

  private static final String ALGORITHM = "PBEWithHMACSHA512AndAES_256";
  private static final int POOL_SIZE = 2;
  private static final int ITERATIONS = 1000;

  @Override
  public void migrate(Context context) throws Exception {
    StringEncryptor encryptor = createEncryptor();
    if (encryptor == null) {
      log(context, "暗号化パスワードが設定されていないため、スキップします");
      return;
    }

    Connection connection = context.getConnection();
    String query =
        "SELECT employee_id, firstName, lastName FROM employees WHERE firstName IS NOT NULL OR lastName IS NOT NULL";
    String updateQuery =
        "UPDATE employees SET firstName = ?, lastName = ? WHERE employee_id = ?";

    try (PreparedStatement selectStmt = connection.prepareStatement(query);
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        ResultSet rs = selectStmt.executeQuery()) {

      int count = 0;
      while (rs.next()) {
        UUID employeeId = UUID.fromString(rs.getString("employee_id"));
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        boolean updated = false;

        String encryptedFirstName = encryptIfPlaintext(firstName, encryptor);
        if (encryptedFirstName != null && !encryptedFirstName.equals(firstName)) {
          firstName = encryptedFirstName;
          updated = true;
        }

        String encryptedLastName = encryptIfPlaintext(lastName, encryptor);
        if (encryptedLastName != null && !encryptedLastName.equals(lastName)) {
          lastName = encryptedLastName;
          updated = true;
        }

        if (updated) {
          updateStmt.setString(1, firstName);
          updateStmt.setString(2, lastName);
          updateStmt.setObject(3, employeeId);
          updateStmt.executeUpdate();
          count++;
        }
      }
      log(context, String.format("暗号化された従業員数: %d", count));
    }
  }

  /**
   * 平文データの場合は暗号化し、既に暗号化されている場合はnullを返します。
   */
  private String encryptIfPlaintext(String value, StringEncryptor encryptor) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      // 既に暗号化されているか確認（復号化を試みる）
      encryptor.decrypt(value);
      // 復号化に成功した場合は既に暗号化されている
      return null;
    } catch (Exception e) {
      // 復号化に失敗した場合は平文とみなして暗号化する
      return encryptor.encrypt(value);
    }
  }

  private StringEncryptor createEncryptor() {
    String password = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
    if (password == null || password.isEmpty()) {
      password = System.getProperty("jasypt.encryptor.password", "");
    }
    if (password.isEmpty()) {
      return null;
    }
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(password);
    config.setAlgorithm(ALGORITHM);
    config.setKeyObtentionIterations(ITERATIONS);
    config.setPoolSize(POOL_SIZE);
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
  }

  private void log(Context context, String message) {
    System.out.println("[Flyway Migration] " + message);
  }
}
