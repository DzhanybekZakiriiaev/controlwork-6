package kz.attractor.java.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;

@FunctionalInterface
public interface RouteHandler {
    void handle(HttpExchange exchange) throws SQLException, IOException;
}
