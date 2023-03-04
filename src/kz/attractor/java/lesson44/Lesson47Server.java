package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Lesson47Server extends Lesson46Server{
    public Lesson47Server(String host, int port) throws IOException, SQLException {
        super(host, port);
        registerGet("/query", this::handleQueryRequest);
        registerGet("/book", this::handleBookRequest);
    }
    private void handleBookRequest(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        String params = Utils.parseUrlEncodedBook(queryParams);
        params = params.replace("Optional[id=","");
        params = params.replace("]","");
        int index = Integer.parseInt(params);
        Book book = new Book(1, "", "", "", 1, "", "");
        for (int i = 0; i < FileService.readBookFile().size(); i++){
            if (FileService.readBookFile().get(i).getId() == index){
                book = FileService.readBookFile().get(i);
            }
        }
        renderTemplate(exchange, "book.html", new SingleBookDataModel(book));
    }
    protected String getQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        return Objects.nonNull(query) ? query : "";
    }
    private void handleQueryRequest(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        Map<String, Object> data = new HashMap<>();
        data.put("params", params);
        renderTemplate(exchange, "query.ftl", data);
    }
}
