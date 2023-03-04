package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.server.Cookie;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Lesson47Server extends Lesson46Server{
    public Lesson47Server(String host, int port) throws IOException, SQLException {
        super(host, port);
        registerGet("/query", this::handleQueryRequest);
        registerGet("/day", this::handleDayRequest);
        registerGet("/delete", this::handleDeleteRequest);
        registerGet("/add",this::addGet);
        registerPost("/add",this::addPost);
    }
    private Day getDay(Map<String, String> cookies, HttpExchange exchange) {
        Day day = new Day(LocalDate.of(1,1,1), new ArrayList<>());
        if (!cookies.isEmpty() && (cookies.get("date") != null)) {
            for (int i = 0; i < FileService.readUserFile().size(); i++) {
                if (cookies.get("date").equals(FileService.readDaysFile().get(i).getDate().toString())) {
                   day = FileService.readDaysFile().get(i);
                    return day;
                }
            }
        } else   if (!cookies.isEmpty() && (cookies.get(" date") != null)) {
            for (int i = 0; i < FileService.readUserFile().size(); i++) {
                if (cookies.get(" date").equals(FileService.readDaysFile().get(i).getDate().toString())) {
                    day = FileService.readDaysFile().get(i);
                    return day;
                }
            }
        }
        return day;
    }
    private void addPost(HttpExchange exchange) {
        try{
            String getCookie = getCookies(exchange);
            Map<String,String> cookies = Cookie.parse(getCookie);
            Day day = getDay(cookies,exchange);
            String raw = getBody(exchange);
            List<Optional<String>> parsed = Utils.parseInputEncoded(raw,"&");
            List<String> stats = new ArrayList<>();
                for (Optional<String> s : parsed) {
                    stats.add(s.toString().substring(s.toString().indexOf("=") + 1, s.toString().indexOf("]")));
                }
            Task task = new Task(stats.get(0), stats.get(1), Type.valueOf(stats.get(2)));
            day.getTasks().add(task);
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
    private void addGet(HttpExchange exchange) {
        Path path = makeFilePath("add.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
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
           Cookie sessionCookie = Cookie.make("date",(LocalDate.of(2022,10,index)).toString());
           sessionCookie.setMaxAge(86400);
           sessionCookie.setHttpOnly(true);
           exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());
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
