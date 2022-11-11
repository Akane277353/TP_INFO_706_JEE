package jpa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import jpa.Ticket;

public class Paiement extends Ticket {

	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue
    private int numero;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    private int montant;
	
    
    @Version
    private long version;

    public Paiement(int montant) {
        super();
        this.montant = montant;
        datePaiement = new Date();
    }

    public int getNumero() {
        return numero;
    }

    public Date getDatePaiement() {
        return this.datePaiement;
    }

	public int getMontant() {
		return montant;
	}
	
	public String toString() {
		String pattern = "MM/dd/yyyy HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(getDatePaiement());
	}
   
}
