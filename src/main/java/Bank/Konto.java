package Bank;

import CustomException.OutOfMoneyException;

/**
 * Klasa Konto
 *
 * @author Karol Kistela
 * @version 1.0
 * @deprecated zmieniełem zdanie i jednak urzyje bazy danych do przechowywania informacji o kontach
 */
public class Konto {
    /** numer Konta, numery są przydizelane automatycznie, zaczynając od (2^63)-1*/
    private long numerKonta;
    /** Właściciel konta, pole przechowuje adress e-mail.
     *  Para numerKonta i wlasciciel tworzy unikalny klucz. Ten sam adres email może miec przypisane różne nr kont, ale jeden nr konta ma tylko jednego wlasciela.
     *  */
    private String wlasciciel;
    /** Saldo konta */
    private double saldo;
    /** Ile kont zostało utworzonych do tej pory */
    public static long ileKont = 0;


    static {ileKont += 1;}
    {   this.numerKonta = Long.MAX_VALUE - ileKont;
        this.saldo = 0.0;     }

    /** Tworzy nowe konto dla wlasciciela, nr konta jest generowany automatycznie ze wzoru: (2^63)-1-ileKont */
    public Konto(String wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
    /** Tworzy nowe anonimowe konto, nr konta jest generowany automatycznie ze wzoru: (2^63)-1-ileKont */
    public Konto() {
        this.wlasciciel = "Joe.Doe@domain.com";
    }
    /** @return numer konta*/
    public long getNumerKonta() {
        return numerKonta;
    }
    /** @deprecated nr konta jest automatycznie nadawany przez program */
    public void setNumerKonta(long numerKonta) {
        this.numerKonta = numerKonta;
    }
    /** @return email wlasciciela */
    public String getWlasciciel() {
        return wlasciciel;
    }
    /** Ustawia email */
    public void setWlasciciel(String wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
    /** @return slado konta */
    public double getSaldo() {
        return saldo;
    }
    /** Ustawia slado konta */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    /** Metoda wplacajaca kwote na konto */
    public void Wplac(double kwota){
        this.saldo = this.saldo + kwota;
    }
    /** Sprawdza czy można wypłacic kwotę.
     * @return TRUE jezeli saldo - kwota >= 0 */
    public boolean MoznaWyplacic (double kwota) {
        if ((this.saldo - kwota) >= 0) {
            return true;
        } else {
            return false;
        }
    }
    /** Metoda wyplacajaca kwote z konta, uzywa metody MoznaWyplacic
     * @throws OutOfMoneyException {@link OutOfMoneyException}*/
    public void Wyplac(double kwota) throws OutOfMoneyException {
        if (this.MoznaWyplacic(kwota)) {
            this.saldo -= kwota;
        } else {
            throw new OutOfMoneyException("Brak srodków na koncie do zrealizowania wyplaty gotówki!");
        }
    }
    /** Metoda przelewająca kwote z konta na konto k, uzywa metody MoznaWyplacic
     * @throws OutOfMoneyException {@link OutOfMoneyException}*/
    public void Przelej(Konto k, double kwota) throws OutOfMoneyException{
        if (this.MoznaWyplacic(kwota)) {
            this.Wyplac(kwota);
            k.Wplac(kwota);
        } else {
            throw new OutOfMoneyException("Brak środków na zrealizowanie przelewu");
        }
    }
    /** Metoda przelewająca kwote z konta k1 na konto k2, uzywa metody MoznaWyplacic
     * @throws OutOfMoneyException {@link OutOfMoneyException}*/
    static void Przelej(Konto k1, Konto k2, double kwota) throws OutOfMoneyException {
        if (k1.MoznaWyplacic(kwota)) {
            k1.Wyplac(kwota);
            k2.Wplac(kwota);
        } else {
            throw new OutOfMoneyException("Konto k1 nie posiada wystarczajacych srodków na wykonanie przelewu");
        }
    }
    @Override
    public String toString() {
        System.out.println("NR: " + this.numerKonta);
        System.out.println("Wlasciciel: " + this.wlasciciel);
        System.out.println("Saldo " + this.saldo);
        return null;
    }
}



