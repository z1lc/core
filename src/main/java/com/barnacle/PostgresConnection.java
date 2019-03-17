package com.barnacle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public class PostgresConnection {

  public static final Duration QUERY_TIMEOUT = Duration.ofSeconds(30);

  public static Connection getConnection(boolean local) throws SQLException {
    String dbUrl = String.format("jdbc:postgresql://%s?sslmode=require&user=%s&password=%s",
            CommonProvider.getSecret(SecretType.HEROKU_POSTGRES_URL),
            CommonProvider.getSecret(SecretType.HEROKU_POSTGRES_USERNAME),
            CommonProvider.getSecret(SecretType.HEROKU_POSTGRES_PASSWORD));
    if (local) {
      dbUrl = System.getenv("JDBC_DATABASE_URL");
    }
    return DriverManager.getConnection(dbUrl);
  }

  public static String quote(Object s) {
    return s == null ? "NULL" : String.format("'%s'", s.toString());
  }

}
