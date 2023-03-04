package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.server.Cookie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Lesson46Server extends Server{
    public Lesson46Server(String host, int port) throws IOException, SQLException {
        super(host, port);
        registerGet("/lesson46", this::lesson46Handler);
    }
    private void lesson46Handler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        String name = "times";

        String cookieStr = getCookies(exchange);
        Map<String,String> cookies = Cookie.parse(cookieStr);

        String cookieValue = cookies.getOrDefault(name, "0");
        int times = Integer.parseInt(cookieValue)+1;

        Cookie response = new Cookie(name, times);
        setCookie(exchange, response);

        data.put(name,times);
        data.put("cookies", cookies);
        renderTemplate(exchange, "cookie.html", data);
    }


}
