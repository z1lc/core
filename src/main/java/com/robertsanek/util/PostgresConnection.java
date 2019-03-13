package com.robertsanek.util;

import static com.robertsanek.util.SecretType.HEROKU_POSTGRES_PASSWORD;
import static com.robertsanek.util.SecretType.HEROKU_POSTGRES_USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;

public class PostgresConnection {

  public static final Duration QUERY_TIMEOUT = Duration.ofSeconds(30);

  public static Connection getConnection(boolean local) {
    String dbUrl = local ? String.format(
        "jdbc:postgresql://ec2-184-72-243-166.compute-1.amazonaws.com:5432/d78ml1ie48ald4" +
            "?sslmode=require" +
            "&user=%s" +
            "&password=%s",
        CommonProvider.getSecret(HEROKU_POSTGRES_USERNAME),
        CommonProvider.getSecret(HEROKU_POSTGRES_PASSWORD)) :
        System.getenv("JDBC_DATABASE_URL");
    return Unchecked.get(() -> DriverManager.getConnection(dbUrl));
  }

}
