package testForSparkAndFreemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KarolK on 02.02.2016.
 */
public class sparkTest {
    public static void main(String[] args) {
        /** FreeMarker ustawienia */
        final Configuration config = new Configuration();
        config.setClassForTemplateLoading(sparkTest.class, "/");


        /** definicja ścieżki http://localhost:4567/ */
        Spark.get(new Route("/") {
            @Override
            public Object handle(final Request request,final Response response) {
                /** Tutaj zapiszemy HTML z podminionymi zmiennymi przez freeMarker */
                StringWriter writer = new StringWriter();

                try {
                    /** Podmieniamy zmienne w templejcie */
                    Template helloTemplate = config.getTemplate("bankOverview.ftl");

                    /** Mapa helloMap przechowuje co zamieniamy na co */
                    Map<String,Object> helloMap = new HashMap<String,Object>();
                    helloMap.put("title","HelloWorld");

                    helloTemplate.process(helloMap, writer);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return writer;
            }
        });

        Spark.get(new Route("/1"){
            @Override
            public Object handle(final Request request,final Response response)   {
                return 1;
            }
        });

        /** Przekazywanie parametrów z adresu strony do programu */
        Spark.get(new Route("/1/:var"){
            @Override
            public Object handle(final Request request,final Response response)   {
                String parametr = request.params(":var");
               // String parametr2 = request.params(":var2");
                return parametr;
            }
        });

        Spark.get(new Route("/2/:var/:var2"){
            @Override
            public Object handle(final Request request,final Response response)   {
                StringWriter w = new StringWriter();

                try {
                    Template t2 = config.getTemplate("unused/2.ftl");

                    Map<String,Object> helloMap = new HashMap<String,Object>();
                    helloMap.put("var1",request.params(":var"));
                    helloMap.put("var2",request.params(":var2"));

                    t2.process(helloMap, w);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return w;
//                String parametr = request.params(":var");
//                String parametr2 = request.params(":var2");
//                return parametr + " " + parametr2;
            }

        });
    }
}
