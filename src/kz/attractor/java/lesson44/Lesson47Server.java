package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Lesson47Server extends Lesson46Server{
    public Lesson47Server(String host, int port) throws IOException, SQLException {
        super(host, port);
        registerGet("/query", this::handleQueryRequest);
        registerGet("/day", this::handleDayRequest);
        registerGet("/delete", this::handleDeleteRequest);
    }
    private void handleDeleteRequest(HttpExchange exchange) {
        try{
            String queryParams = getQueryParams(exchange);
            String params = Utils.parseUrlEncodedBook(queryParams);
            params = params.replace("Optional[date=","");
            params = params.replace("]","");
            params = params.replace("name=","");
            List<String> stats = List.of(params.split("&"));
            Day day = new Day(LocalDate.parse(stats.get(0)), new ArrayList<>());
            for (int i = 0; i < FileService.readDaysFile().size(); i++){
                if (FileService.readDaysFile().get(i).getDate().toString().equals(stats.get(0))){
                    day = FileService.readDaysFile().get(i);
                }
            }
            for (int i = 0; i < day.getTasks().size(); i++){
                if (day.getTasks().get(i).getName().equals(stats.get(1))){
                    day.getTasks().remove(i);
                }
            }
            List<Day> days = new ArrayList<>();
            for(int i = 0; i < FileService.readDaysFile().size(); i++){
                if (FileService.readDaysFile().get(i).getDate().toString().equals(day.getDate().toString())){
                    days.add(day);
                }
                else {
                    days.add(FileService.readDaysFile().get(i));
                }
            }
            FileService.writeDaysFile(days);
            redirect303(exchange, "/day?date="+day.getDate().getDayOfMonth());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void handleDayRequest(HttpExchange exchange) {
       try{
           String queryParams = getQueryParams(exchange);
           String params = Utils.parseUrlEncodedBook(queryParams);
           params = params.replace("Optional[date=","");
           params = params.replace("]","");
           int index = Integer.parseInt(params);
           Day day = new Day(LocalDate.of(2022,10,index), new ArrayList<>());
           for (int i = 0; i < FileService.readDaysFile().size(); i++){
               if (FileService.readDaysFile().get(i).getDate().getDayOfMonth() == index){
                   day = FileService.readDaysFile().get(i);
               }
           }
           renderTemplate(exchange, "day.html", new SingleDayDataModel(day));
       }catch (Exception e){
           e.printStackTrace();
       }
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
