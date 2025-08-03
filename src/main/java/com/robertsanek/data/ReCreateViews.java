package com.robertsanek.data;

import static com.robertsanek.util.SecretType.RENDER_SQL_Z_BI_POSTGRES_PASSWORD;
import static com.robertsanek.util.SecretType.RENDER_SQL_Z_BI_POSTGRES_USERNAME;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;

public class ReCreateViews {

  static final Log log = Logs.getLog(ReCreateViews.class);

  @Inject SecretProvider secretProvider;

  public void executeQueries() {
    String jdbcUrl = "jdbc:postgresql://dpg-cefuij1gp3jk7mi5qev0-a.oregon-postgres.render.com:5432/z_bi";
    String username = secretProvider.getSecret(RENDER_SQL_Z_BI_POSTGRES_USERNAME);
    String password = secretProvider.getSecret(RENDER_SQL_Z_BI_POSTGRES_PASSWORD);

    Unchecked.run(() -> Class.forName("org.postgresql.Driver"));
    String lastStatement = "";
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
      String sqlFile = Unchecked.get(() -> Resources.toString(
          Resources.getResource("com/robertsanek/sql/CommonQueries.sql"), StandardCharsets.UTF_8));
      try (Statement statement = connection.createStatement()) {
        for (String s : sqlFile.split(";")) {
          lastStatement = s;
          statement.execute(s);
        }
      }
    } catch (SQLException e) {
      log.error(lastStatement);
      throw new RuntimeException(e);
    }
  }

}
