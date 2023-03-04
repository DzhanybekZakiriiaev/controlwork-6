package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kz.attractor.java.server.BasicServer;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.server.Cookie;
import kz.attractor.java.server.ResponseCodes;
import kz.attractor.java.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Lesson47Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    public Lesson47Server(String host, int port) throws IOException, SQLException {
        super(host, port);
        registerGet("/query", this::handleQueryRequest);
        registerGet("/day", this::handleDayRequest);
        registerGet("/delete", this::handleDeleteRequest);
        registerGet("/add",this::addGet);
        registerPost("/add",this::addPost);
        registerGet("/", this::daysHandler);
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
    private void daysHandler(HttpExchange exchange) {
        try {
            Cookie outCookie = new Cookie<>("", "");
            outCookie.setMaxAge(1);
            setCookie(exchange, outCookie);
            renderTemplate(exchange, "days.html", getDaysDataModel());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private DaysDataModel getDaysDataModel() {
        return new DaysDataModel();
    }
    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Day getDay(Map<String, String> cookies, HttpExchange exchange) {
        Day day = new Day(LocalDate.of(1,1,1), new ArrayList<>());
        if (!cookies.isEmpty() && (cookies.get("date") != null)) {
            for (int i = 0; i < FileService.readDaysFile().size(); i++) {
                if (cookies.get("date").equals(FileService.readDaysFile().get(i).getDate().toString())) {
                   day = FileService.readDaysFile().get(i);
                    return day;
                }
            }
        } else   if (!cookies.isEmpty() && (cookies.get(" date") != null)) {
            for (int i = 0; i < FileService.readDaysFile().size(); i++) {
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
