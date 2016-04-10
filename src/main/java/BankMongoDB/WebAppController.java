package BankMongoDB;

import CustomException.OutOfMoneyException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

/**
 * Created by KarolK on 03.02.2016.
 * Ta klasa odpowiada za obsluge aplikacji webowej. Przekierowuje interakcje z Baza Danych do klas DAOs.
 */
public class WebAppController {
    private final Configuration cfg;
    private final AccountsDAO accountsDAO;
    private final UsersDAO usersDAO;
    private final SessionsDAO sessionsDAO;
    static final String DNS = "ds033255.mongolab.com:33255/karolk";
    static final String DB = "karolk";
    static String bankOperation = "";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new WebAppController("mongodb://karolk:*******@"+DNS);
        }
        else {
            new WebAppController(args[0]);
        }
    }

    public WebAppController(String mongoURIString) throws IOException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final MongoDatabase blogDatabase = mongoClient.getDatabase(DB);

        accountsDAO = new AccountsDAO(blogDatabase);
        usersDAO = new UsersDAO(blogDatabase);
        sessionsDAO = new SessionsDAO(blogDatabase);

        cfg = createFreemarkerConfiguration();
        setPort(8082);
        initializeRoutes();
    }

    abstract class FreemarkerBasedRoute extends Route {
        final Template template;

        /**
         * Zrodlo: https://university.mongodb.com/m101j/
         * Constructor
         *
         * @param path The route path which is used for matching. (e.g. /hello, users/:name)
         */
        protected FreemarkerBasedRoute(final String path, final String templateName) throws IOException {
            super(path);
            template = cfg.getTemplate(templateName);
        }

        @Override
        public Object handle(Request request, Response response) {
            StringWriter writer = new StringWriter();
            try {
                doHandle(request, response, writer);
            } catch (Exception e) {
                e.printStackTrace();
                response.redirect("/internal_error");
            }
            return writer;
        }

        protected abstract void doHandle(final Request request, final Response response, final Writer writer)
                throws IOException, TemplateException;

    }

    private void initializeRoutes() throws IOException{
        get(new FreemarkerBasedRoute("/bankOverview","bankOverview.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);

                if (username == null) {
                    System.out.println("welcome() can't identify the user, redirecting to signup");
                    response.redirect("/signup");
                } else {
                    root.put("username", username);
                }

                root.put("DNS",DNS);
                root.put("DB",DB);
                root.put("userCount",usersDAO.userCount());
                root.put("accCount",accountsDAO.accCount());
                root.put("totalCash",accountsDAO.total());
                root.put("HTMLlist",usersDAO.listOfUsers());
                root.put("accountsJSON", printJson(accountsDAO.accountsGetLucky()));
                root.put("usersJSON", printJson(usersDAO.usersGetLucky()));
                root.put("sessionsJSON", printJson(sessionsDAO.sessionsGetLucky()));

                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/myAccount", "myAccount.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                SimpleHash root = new SimpleHash();

                if (username == null) {
                    System.out.println("welcome() can't identify the user, redirecting to signup");
                    response.redirect("/signup");
                } else {
                    root.put("username", username);
                }
                root.put("accNR",usersDAO.accountNR(username));
                root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                root.put("HTMLlist",usersDAO.listOfUsers());
                root.put("lastOperation",WebAppController.bankOperation);
                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/myAccount/deposit","deposit.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                SimpleHash root = new SimpleHash();

                root.put("depositValue", "");
                root.put("accNR",usersDAO.accountNR(username));
                root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                root.put("HTMLlist",usersDAO.listOfUsers());
                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/myAccount/deposit", "deposit.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                String depositValue = request.queryParams("depositValue");
                double value = Double.parseDouble(depositValue);

                accountsDAO.Wplac(usersDAO.accountNR(username),value);
                WebAppController.bankOperation = "Value of last deposit: "+value+" PLN";
                response.redirect("/myAccount");

                root.put("accNR",usersDAO.accountNR(username));
                root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                root.put("HTMLlist",usersDAO.listOfUsers());
                template.process(root, writer);
                }
            });

        get(new FreemarkerBasedRoute("/myAccount/withdraw","withdraw.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                SimpleHash root = new SimpleHash();

                root.put("withdraw", "");
                root.put("accNR",usersDAO.accountNR(username));
                root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                root.put("HTMLlist",usersDAO.listOfUsers());
                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/myAccount/withdraw", "withdraw.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                String depositValue = request.queryParams("withdraw");
                double value = Double.parseDouble(depositValue);

                try {
                    accountsDAO.Wyplac(usersDAO.accountNR(username),value);
                    WebAppController.bankOperation = "Value of last withdraw: "+value+" PLN";
                    response.redirect("/myAccount");

                    root.put("accNR",usersDAO.accountNR(username));
                    root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                    root.put("HTMLlist",usersDAO.listOfUsers());
                } catch (OutOfMoneyException e) {
                    WebAppController.bankOperation = "Brak środków na koncie!";
                    response.redirect("/myAccount");
                }
                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/myAccount/transfer","transfer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                SimpleHash root = new SimpleHash();

                root.put("transferTo", "");
                root.put("transfer", "");
                root.put("accNR",usersDAO.accountNR(username));
                root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                root.put("HTMLlist",usersDAO.listOfUsers());
                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/myAccount/transfer", "transfer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                String cookie = getSessionCookie(request);
                String username = sessionsDAO.findUserNameBySessionId(cookie);
                String depositValue = request.queryParams("transfer");
                double value = Double.parseDouble(depositValue);
                String transferTo = request.queryParams("transferTo");
                try {
                    accountsDAO.przelej(usersDAO.accountNR(username),usersDAO.accountNR(transferTo),value);
                    WebAppController.bankOperation = "transfered: "+value+" PLN to "+transferTo;
                    response.redirect("/myAccount");

                    root.put("accNR",usersDAO.accountNR(username));
                    root.put("saldo",accountsDAO.getAcc(usersDAO.accountNR(username)).getDouble("saldo"));
                    root.put("HTMLlist",usersDAO.listOfUsers());
                } catch (OutOfMoneyException e) {
                    WebAppController.bankOperation = "Brak środków na koncie!";
                    response.redirect("/myAccount");
                }
                template.process(root, writer);
            }
        });



        get(new FreemarkerBasedRoute("/logout", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String sessionID = getSessionCookie(request);

                if (sessionID == null) {
                    // no session to end
                    response.redirect("/");
                }
                else {
                    // deletes from session table
                    sessionsDAO.endSession(sessionID);

                    // this should delete the cookie
                    Cookie c = getSessionCookieActual(request);
                    c.setMaxAge(0);

                    response.raw().addCookie(c);
                    WebAppController.bankOperation = "";
                    response.redirect("/");
                }
            }
        });

        get(new FreemarkerBasedRoute("/signup","signup.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                root.put("username", "");
                root.put("login_error", "");

                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/signup", "signup.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String username = request.queryParams("username");
                String password = request.queryParams("password");

                System.err.println("Login: User submitted: " + username + "  " + password);

                usersDAO.newUser(username, password, accountsDAO.newAccount());

                Document user = usersDAO.validateLogin(username, password);

                if (user != null) {
                    // valid user, let's log them in
                    String sessionID = sessionsDAO.startSession(user.get("_id").toString());

                    if (sessionID == null) {
                        response.redirect("/internal_error");
                    }
                    else {
                        // set the cookie for the user's browser
                        response.raw().addCookie(new Cookie("session", sessionID));

                        response.redirect("/bankOverview");
                    }
                }
                else {
                    SimpleHash root = new SimpleHash();

                    root.put("username", username);
                    root.put("password", "");
                    root.put("login_error", "Invalid Login");
                    template.process(root, writer);
                }
                }
        });

        get(new FreemarkerBasedRoute("/","login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                root.put("username", "");
                root.put("login_error", "");

                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String username = request.queryParams("username");
                String password = request.queryParams("password");

                System.err.println("Login: User submitted: " + username + "  " + password);

                Document user = usersDAO.validateLogin(username, password);

                if (user != null) {
                    // valid user, let's log them in
                    String sessionID = sessionsDAO.startSession(user.get("_id").toString());

                    if (sessionID == null) {
                        response.redirect("/internal_error");
                    } else {
                        // set the cookie for the user's browser
                        response.raw().addCookie(new Cookie("session", sessionID));

                        response.redirect("/bankOverview");
                    }
                } else {
                    SimpleHash root = new SimpleHash();

                    root.put("username", username);
                    root.put("password", "");
                    root.put("login_error", "Invalid Login");
                    template.process(root, writer);
                }
            }
        });

        // used to process internal errors
        get(new FreemarkerBasedRoute("/internal_error", "error_template.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                root.put("error", "System has encountered an error.");
                template.process(root, writer);
            }
        });


        get(new FreemarkerBasedRoute("/JavaDoc","/JavaDoc/Index.html") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                template.process(root, writer);
                //
            }
        });

        get(new FreemarkerBasedRoute("/overview-summary.html","/JavaDoc/overview-summary.html") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                template.process(root, writer);
                //
            }
        });

        get(new FreemarkerBasedRoute("/overview-frame.html","/JavaDoc/overview-frame.html") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                template.process(root, writer);
                //
            }
        });

        get(new FreemarkerBasedRoute("/allclasses-frame.html","/JavaDoc/allclasses-frame.html") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                template.process(root, writer);
                //
            }
        });

    }

    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(WebAppController.class, "/");
        return retVal;
    }

    private String printJson(Document document) {
        JsonWriter jsonWriter = new JsonWriter(new StringWriter(),new JsonWriterSettings(JsonMode.SHELL, false));
        new DocumentCodec().encode(jsonWriter,document, EncoderContext.builder()
                .isEncodingCollectibleDocument(true)
                .build());
         return jsonWriter.getWriter().toString();
    }

    // helper function to get session cookie as string
    private String getSessionCookie(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // helper function to get session cookie as string
    private Cookie getSessionCookieActual(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie;
            }
        }
        return null;
    }

}
