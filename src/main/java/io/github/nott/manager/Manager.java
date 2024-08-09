package io.github.nott.manager;

import java.sql.Connection;

public interface Manager {

    Connection getConnect() throws Exception;
}
