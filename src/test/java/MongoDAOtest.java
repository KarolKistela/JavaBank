import BankMongoDB.AccountsDAO;
import BankMongoDB.UsersDAO;
import CustomException.OutOfMoneyException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.bson.Document;

import java.io.IOException;


/**
 * Created by KarolK on 29.01.2016.
 *
 * klasa do testowania klas DAO
 *
 */
public class MongoDAOtest {
    private final AccountsDAO accountsDAO;
    private final UsersDAO usersDAO;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new MongoDAOtest("mongodb://karolk:goliat11@ds033255.mongolab.com:33255/karolk");
        }
        else {
            new MongoDAOtest(args[0]);
        }
    }

    public MongoDAOtest(String mongoURIString) throws IOException{
        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final MongoDatabase BankDatabase = mongoClient.getDatabase("karolk");

        accountsDAO = new AccountsDAO(BankDatabase);
        usersDAO = new UsersDAO(BankDatabase);

//        usersDAO.newUser("JoeDoe@gmail.com","haslo",accountsDAO.newAccount());
//        usersDAO.newUser("karol@server.com","haslo",accountsDAO.newAccount());

//        accountsDAO.Wplac(usersDAO.accountNR("karol.kistela@gmail.com"), 90.00);
//        accountsDAO.Wplac(usersDAO.accountNR("karol@server.com"), 150.00);
//        System.out.println(usersDAO.accountNR("JoeDoe@gmail.com"));
//        if (accountsDAO.MoznaWyplacic(usersDAO.accountNR("JoeDoe@gmail.com"),180.00)){
//            System.out.println("Mozna wyplacic");
//        }

//        try {
//            accountsDAO.Wyplac(usersDAO.accountNR("JoeDoe@gmail.com"), 180.00);
//        } catch (OutOfMoneyException e) { e.printStackTrace(); }
//
//        try {
//            accountsDAO.Wyplac(usersDAO.accountNR("karol@server.com"), 55.00);
//        } catch (OutOfMoneyException e) { e.printStackTrace(); }

//        try {
//            accountsDAO.przelej(usersDAO.accountNR("karol@server.com"),usersDAO.accountNR("JoeDoe@gmail.com"),300.00);
//        } catch (OutOfMoneyException e) { e.printStackTrace();  }
        try{
            System.out.println("Saldo: " + accountsDAO.getAcc(usersDAO.accountNR("karol.kistela@gmail.com")).getDouble("saldo"));
        } catch (NullPointerException e){
            System.out.print("Saldo: 0.0");
        }

    }

}
