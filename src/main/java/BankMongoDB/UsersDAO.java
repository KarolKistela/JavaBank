package BankMongoDB;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import sun.misc.BASE64Encoder;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Karol Kistela on 29.01.2016. Klasa Data Access Object, łączy się kolekcją users.
 * Struktura dokumentu w kolekcji:
 * {
 * "_id":"userEmail",
 * "password":"password",
 * "accountNumber":Long,
 * }
 *
 * @author Karol Kistela
 * @version 1.0
 */
public class UsersDAO {
    /** kolekcja users w bazie danych */
    private final MongoCollection<Document> usersCollection;
    /** do hashowania hasła */
    private Random random = new SecureRandom();

    /** tworzy Data acces object do kolekcji users */
    public UsersDAO(final MongoDatabase bankDatabase) {
        usersCollection = bankDatabase.getCollection("users");
    }

    /** Dodaje do kolekcji users nowe konto */
    public boolean newUser(String userEmail, String password, Document acc){
        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

        Document user = new Document();

        user.append("_id",userEmail)
            .append("password",passwordHash)
            .append("accountNumber",acc.getLong("_id"));

        try {
            usersCollection.insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Username already in use: " + userEmail);
                return false;
            }
            throw e;
        }
    }
    /** Zwraca z kolekcji users Document JSON zawierający dane użytkownika, jeżeli userEmail i hasło są poprawne
     *
     *  @return Document account */
    public Document validateLogin(String userEmail, String password) {
        Document user;

        user = usersCollection.find(eq("_id", userEmail)).first();

        if (userEmail == null) {
            System.out.println("User not in database");
            return null;
        }

        String hashedAndSalted = user.get("password").toString();

        String salt = hashedAndSalted.split(",")[1];

        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
            System.out.println("Submitted password is not a match");
            return null;
        }

        return user;
    }

    /** Zwraca Nr konta dla podanego usera */
    public Long accountNR(String userMail){
        Document userAcc =  this.usersCollection.find(eq("_id",userMail)).first();
        return userAcc.getLong("accountNumber");
    }

    /** żródło: https://university.mongodb.com/m101j/
     *  metoda haszujaca hasło */
    private String makePasswordHash(String password, String salt) {
        try {
            String saltedAndHashed = password + "," + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            return encoder.encode(hashedBytes) + "," + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }
    /** zwraca liczbe userow w kolekcji users */
    public Long userCount(){
        return this.usersCollection.count();
    }
    /** zwraca czesc kodu HTML z tabela user: accoutnNR */
    public String listOfUsers() {
        String HTMLList = "";
        List<Document> users = this.usersCollection.find().into(new ArrayList<Document>());
        for (Document u:users) {
            HTMLList += "<tr><td>"+u.getString("_id")+"</td><td>"+u.getLong("accountNumber")+"</td></tr>";
        }
        return HTMLList;
    }

    /** zwraza jeden losowy dokument z kolekcji */
    public Document usersGetLucky(){
        Random gen = new Random();
        int lucky = gen.nextInt((int) this.usersCollection.count());
        Document luckyShot = this.usersCollection.find().skip(lucky).first();
        return luckyShot;
    }
}
