package com.robertsanek.data.etl.local.sqllite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.Unchecked;

public abstract class SQLiteEtl<T> extends Etl<T> {

  private static final Duration QUERY_TIMEOUT = Duration.ofSeconds(30);

  public void preEtlStep() {

  }

  public void postEtlStep() {

  }

  @Override
  public List<T> getObjects() throws Exception {
    preEtlStep();
    Unchecked.run(() -> Class.forName("org.sqlite.JDBC"));
    List<T> objects = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(getConnectionString());
         Statement statement = connection.createStatement()) {
      statement.setQueryTimeout((int) QUERY_TIMEOUT.getSeconds());
      ResultSet rs = statement.executeQuery(String.format("select * from %s;", getImportTableName()));
      while (rs.next()) {
        objects.addAll(transformRow(rs));
      }
    }

    postEtlStep();
    return objects;
  }

  public String getConnectionString() {
    return String.format("jdbc:sqlite:%s", getSQLiteDbFileLocation().getAbsolutePath());
  }

  public abstract List<T> transformRow(ResultSet row) throws Exception;

  public abstract File getSQLiteDbFileLocation();

  public abstract String getImportTableName();

}
