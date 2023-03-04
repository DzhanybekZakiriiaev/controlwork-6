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
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    private final static String name = "id";

    public Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::daysHandler);
        registerGet("/books", this::daysHandler);
//        registerGet("/failed", this::failedHandler);
//        registerGet("/success", this::successHandler);
//        registerGet("/failed2", this::failedLoginHandler);
//        registerGet("/success2", this::successLoginHandler);
//        registerGet("/users", this::accountsHandler);
//        registerGet("/accounts", this::accountsHandler);
//        registerGet("/login",this::loginGet);
//        registerPost("/login",this::loginPost);
//        registerGet("/out",this::outGet);
//        registerPost("/out",this::outPost);
//        registerGet("/registration",this::registrationGet);
//        registerPost("/registration",this::registrationPost);
//        registerGet("/give",this::giveGet);
//        registerPost("/give",this::givePost);
//        registerGet("/take",this::takeGet);
//        registerPost("/take",this::takePost);
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
           renderTemplate(exchange, "days.html", getDaysDataModel());
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    private DaysDataModel getDaysDataModel() {
        return new DaysDataModel();
    }

    //    private void userHandler(HttpExchange exchange) {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//        if (!Objects.equals(user.getEmail(), "")){
//            renderTemplate(exchange, "user.html", getUserDataModel(exchange));
//        }
//        else {
//            redirect303(exchange, "/login");
//        }
//    }
//    private User getUser(Map<String, String> cookies, HttpExchange exchange) {
//        User user = new User("", "", "", "");
//        if (!cookies.isEmpty() && (cookies.get("id") != null)) {
//            for (int i = 0; i < FileService.readUserFile().size(); i++) {
//                if (cookies.get("id").equals(FileService.readUserFile().get(i).getId())) {
//                    user = FileService.readUserFile().get(i);
//                    return user;
//                }
//            }
//        }else if (!cookies.isEmpty() && (cookies.get(" id") != null)) {
//            for (int i = 0; i < FileService.readUserFile().size(); i++) {
//                if (cookies.get(" id").equals(FileService.readUserFile().get(i).getId())) {
//                    user = FileService.readUserFile().get(i);
//                    return user;
//                }
//            }
//        }
//        return user;
//    }
//    private void outGet(HttpExchange exchange) {
//        Path path = makeFilePath("out.html");
//        sendFile(exchange, path, ContentType.TEXT_HTML);
//    }
//    private void outPost(HttpExchange exchange){
//        try{
//            Cookie logoutCookie = new Cookie<>(name, "");
//            logoutCookie.setMaxAge(3);
//            setCookie(exchange, logoutCookie);
//            redirect303(exchange, "/login");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    private Object getUserDataModel(HttpExchange exchange) {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//        return new SingleUserDataModel(user);
//    }
//
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

    //
//    private void takeGet(HttpExchange exchange) {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//        if (!Objects.equals(user.getEmail(), "")){
//            Path path = makeFilePath("take.html");
//            sendFile(exchange, path, ContentType.TEXT_HTML);
//        }
//        else {
//            redirect303(exchange, "/login");
//        }
//    }
//    private void takePost(HttpExchange exchange) {
//        String raw = getBody(exchange);
//        String parsed = Utils.parseUrlEncodedBook(raw);
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//            try{
//                parsed = parsed.replace("Optional[id=","");
//                parsed = parsed.replace("]","");
//                int index = Integer.parseInt(parsed);
//                for(int i = 0; i < user.getBooks().size(); i++){
//                    if (user.getBooks().get(i).getId() == index){
//                        user.getBooks().remove(i);
//                    }
//                }
//                List<User> users = new ArrayList<>();
//                for (int i = 0; i < FileService.readUserFile().size(); i++){
//                    if (user.getId().equals(FileService.readUserFile().get(i).getId())){
//                        users.add(user);
//                    }
//                    else {
//                        users.add(FileService.readUserFile().get(i));
//                    }
//                }
//                FileService.writeUserFile(users);
//                redirect303(exchange,"/");
//            }catch (Exception e){
//                e.printStackTrace();
//                redirect303(exchange,"/login");
//            }
//        }
//    private void giveGet(HttpExchange exchange) {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//        if (!Objects.equals(user.getEmail(), "")){
//            Path path = makeFilePath("give.html");
//            sendFile(exchange, path, ContentType.TEXT_HTML);
//        }
//        else {
//            redirect303(exchange, "/login");
//        }
//    }
//    private void givePost(HttpExchange exchange) {
//        String raw = getBody(exchange);
//        String parsed = Utils.parseUrlEncodedBook(raw);
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//            if (user.getBooks().size() < 2){
//                try{
//                    parsed = parsed.replace("Optional[id=","");
//                    parsed = parsed.replace("]","");
//                    int index = Integer.parseInt(parsed);
//                    for(int i = 0; i < FileService.readBookFile().size(); i++){
//                        if (FileService.readBookFile().get(i).getId() == index){
//                            List<Book> books = user.getBooks();
//                            books.add(FileService.readBookFile().get(i));
//                            user.getHistory().add(FileService.readBookFile().get(i));
//                            user.setBooks(books);
//                        }
//                    }
//                    List<User> users = new ArrayList<>();
//                    for (int i = 0; i < FileService.readUserFile().size(); i++){
//                        if (user.getId().equals(FileService.readUserFile().get(i).getId())){
//                            users.add(user);
//                        }
//                        else {
//                            users.add(FileService.readUserFile().get(i));
//                        }
//                    }
//                    FileService.writeUserFile(users);
//                    redirect303(exchange,"/");
//                }catch (Exception e){
//                    e.printStackTrace();
//                    redirect303(exchange,"/login");
//                }
//            }
//            else {
//                redirect303(exchange,"/");
//            }
//    }
//    private void loginGet(HttpExchange exchange) {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//        if (Objects.equals(user.getEmail(), "")){
//            Path path = makeFilePath("login.html");
//            sendFile(exchange, path, ContentType.TEXT_HTML);
//        }
//        else {
//            redirect303(exchange, "/user");
//        }
//    }
//    private void loginPost(HttpExchange exchange){
//        try{
//            String raw = getBody(exchange);
//            List<Optional<String>> parsed = Utils.parseInputEncoded(raw,"&");
//            List<String> stats = new ArrayList<>();
//            for (Optional<String> s : parsed) {
//                stats.add(s.toString().substring(s.toString().indexOf("=") + 1, s.toString().indexOf("]")));
//            }
//            List<User> users = FileService.readUserFile();
//            boolean contains = false;
//            for (User user : users) {
//                if (user.getEmail() != null && user.getPassword() != null){
//                    if (user.getEmail().equals(stats.get(0)) && user.getPassword().equals(stats.get(1))) {
//                        contains = true;
//                        Cookie sessionCookie = Cookie.make(name,user.getId());
//                        sessionCookie.setMaxAge(86400);
//                        sessionCookie.setHttpOnly(true);
//                        exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());
//                    }
//                }
//            }
//            if (!contains){
//                redirect303(exchange,"/failed2");
//            }else {
//                redirect303(exchange,"/success2");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    private void registrationGet(HttpExchange exchange) {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User user = getUser(cookies, exchange);
//        if (Objects.equals(user.getEmail(), "")){
//            Path path = makeFilePath("registration.html");
//            sendFile(exchange, path, ContentType.TEXT_HTML);
//        }
//        else {
//            redirect303(exchange, "/user");
//        }
//    }
//    private void registrationPost(HttpExchange exchange){
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User userCookie = getUser(cookies, exchange);
//        if (Objects.equals(userCookie.getEmail(), "")){
//            try{
//                String raw = getBody(exchange);
//                List<Optional<String>> parsed = Utils.parseInputEncoded(raw,"&");
//                List<String> stats = new ArrayList<>();
//                for (Optional<String> s : parsed) {
//                    stats.add(s.toString().substring(s.toString().indexOf("=") + 1, s.toString().indexOf("]")));
//                }
//                List<User> users;
//                FileService.readUserFile();
//                users = FileService.readUserFile();
//                boolean contains = false;
//                for (User user : users) {
//                    if (user.getEmail() != null){
//                        if (user.getEmail().equals(stats.get(2))) {
//                            contains = true;
//                        }
//                    }
//                }
//                if (contains){
//                    redirect303(exchange,"/failed");
//                }else {
//                    FileService.addUser(stats);
//                    Cookie sessionCookie = Cookie.make(name,FileService.readUserFile().get(FileService.readUserFile().size()-1).getId());
//                    sessionCookie.setMaxAge(86400);
//                    sessionCookie.setHttpOnly(true);
//                    exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());
//                    redirect303(exchange,"/success");
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }else {
//            redirect303(exchange,"/profile");
//        }
//    }
//
//    private void successLoginHandler(HttpExchange exchange) {
//        renderTemplate(exchange, "success2.html", getUsersDataModel());
//    }
//    private void failedLoginHandler(HttpExchange exchange) {
//        renderTemplate(exchange, "failed2.html", getUsersDataModel());
//    }
//
//    private void successHandler(HttpExchange exchange) throws SQLException {
//        renderTemplate(exchange, "success.html", getUsersDataModel());
//    }
//    private void failedHandler(HttpExchange exchange) throws SQLException {
//        renderTemplate(exchange, "failed.html", getUsersDataModel());
//    }
//
//    private void accountsHandler(HttpExchange exchange) throws SQLException {
//        String getCookie = getCookies(exchange);
//        Map<String,String> cookies = Cookie.parse(getCookie);
//        User userCookie = getUser(cookies, exchange);
//        if (!Objects.equals(userCookie.getEmail(), "")){
//            renderTemplate(exchange, "accounts.html", getUsersDataModel());
//        }
//        else {
//            redirect303(exchange,"/login");
//        }
//    }
//
//}
//    private UserDataModel getUsersDataModel(){
//        return new UserDataModel();
//    }
    }
