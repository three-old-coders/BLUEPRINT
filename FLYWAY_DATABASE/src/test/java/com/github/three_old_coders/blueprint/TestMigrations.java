package com.github.three_old_coders.blueprint;

import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.junit.Test;

public class TestMigrations {

    public static final String urlLocalhost = "jdbc:hsqldb:hsql://localhost:9001";
    public static final String user = "SA";
    public static final String password = "";

    @SneakyThrows
    @Test
    public void testLocalMigration() {
        Flyway flyway = Flyway.configure().dataSource(urlLocalhost, user, password).load();
        flyway.migrate();
    }


}
