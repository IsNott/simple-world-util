package io.github.nott.manager;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Nott
 * @date 2024-8-5
 */
public class SqlLiteManager implements Manager{

    public Connection getConnect() throws Exception{
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:plugins/SimpleWorldPlugins/database.db");
    }
}
