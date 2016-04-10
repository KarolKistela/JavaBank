package BankMongoDB;

import CustomException.OutOfMoneyException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Karol Kistela on 29.01.2016. Klasa Data Access Object, łączy się kolekcją accounts.
 * Struktura dokumentu w kolekcji:
 * {
 * "_id":Long,
 * "saldo":double
 * }
 *
 * @author Karol Kistela
 * @version 1.0
 */
public class AccountsDAO {
    private final MongoCollection<Document> accountsCollection;
    /** do hashowania hasła */
    private Random random = new SecureRandom();
    static public Long howManyAccounts;


    /** tworzy Data acces object do kolekcji accounts */
    public AccountsDAO(final MongoDatabase bankDatabase) {
        accountsCollection = bankDatabase.getCollection("accounts");
        this.howManyAccounts = accountsCollection.count();
    }
    /** tworzy nowy dokument w kolekcji accounts, nadajac mu kolejny nr konta i saldo = 0.00 */
    public Document newAccount(){
        Document account = new Document();
        account.append("_id",Long.MAX_VALUE - howManyAccounts)
                .append("saldo",0.0);
        this.accountsCollection.insertOne(account);
        this.howManyAccounts = accountsCollection.count();
        return account;
    }

    public Document getAcc(Long ID){
        Document acc = this.accountsCollection.find(eq("_id",ID)).first();
        return acc;
    }
    /** Wplac kwote na konto */
    public void Wplac(Long accID, double kwota){
        this.accountsCollection.updateOne(eq("_id",accID), new Document("$inc", new Document("saldo",kwota)));
    }
    /** Sprawdza czy mamy wystarczajaca ilosc pieniedzy na koncie do realizacji wyplaty*/
    public boolean MoznaWyplacic(Long accID, double kwota){
        Document account = this.accountsCollection.find(eq("_id",accID)).first();
        double saldoKonta = account.getDouble("saldo");

        if (saldoKonta >= kwota){
            return true;
        } else {
            return false;
        }
    }
    /** Wyplac pieniadze z konta */
    public void Wyplac(Long accID, double kwota) throws OutOfMoneyException{
        if (this.MoznaWyplacic(accID, kwota)) {
            this.accountsCollection.updateOne(eq("_id", accID), new Document("$inc", new Document("saldo", -kwota)));
        } else {
            throw new OutOfMoneyException("Brak środków do realizacji wypłaty");
        }
    }

    /** Przelej pieniadze na konto */
    public void przelej (long zKonta ,long naKonto,double kwota) throws OutOfMoneyException{
        if (MoznaWyplacic(zKonta, kwota)) {
            Wyplac(zKonta,kwota);
            Wplac(naKonto,kwota);
        }else{
            throw new OutOfMoneyException("Brak środków na koncie do realizacji przelewu");
        }

    }
    /** Liczba zalozonych kont w baknku */
    public Long accCount(){
        return this.accountsCollection.count();
    }
    /** Suma pieniedzy w banku */
    public double total(){
        double sum = 0.0;
        List<Document> all = this.accountsCollection.find().into(new ArrayList<Document>());
        for (Document a:all) {
            sum += a.getDouble("saldo");
        }
        return sum;
    }

    /** zwraza jeden losowy dokument z kolekcji */
    public Document accountsGetLucky(){
        Random gen = new Random();
        int lucky = gen.nextInt((int) this.accountsCollection.count());
        Document luckyShot = this.accountsCollection.find().skip(lucky).first();
        return luckyShot;
    }

    public double accSaldo(Long ID){
        return this.accountsCollection.find(eq("_id",ID)).first().getDouble("saldo");
    }



}
