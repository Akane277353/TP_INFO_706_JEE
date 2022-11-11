package jpa;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Ticket implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue
    private int numero;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntree;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSortie;
    private List<Paiement> paiement;
    private boolean payer;
    
    @Version
    private long version;

    public Ticket() {
        super();
        dateEntree = new Date();
        dateSortie = null;
        paiement = new ArrayList<Paiement>();
        payer = false;
    }

    public int getNumero() {
        return numero;
    }

    public Date getDateEntree() {
        return this.dateEntree;
    }
    public Date getDateSortie() {
        return this.dateSortie;
    }   
    
    public boolean getPayer() {
        return this.payer;
    }
    public void setPayer() {
        this.payer = true;
    }  
    public long getDuration() {
    	Date now = new Date();
    	long intervalle = now.getTime() - this.getDateEntree().getTime();
        return TimeUnit.SECONDS.convert(intervalle, TimeUnit.MILLISECONDS);
    }

	public List<Paiement> getPaiement() {
		return this.paiement;
	}
	
	public void addPaiement() {
		Paiement p = new Paiement((int)getDuration());
		this.setPayer();
		paiement.add(p);
	} 
	
	public void updateExit() {
		dateSortie = new Date();
	} 
	
	public Date getLastPaiement() {
		Date res = null;
		if (paiement != null) {
			if (paiement.size()-1 > 0) {
				res =  paiement.get(paiement.size()-1).getDatePaiement();
			}
		}
		return res;
	}
	
	public String dateToString(Date date) {
		String res = "encore dedans";
		if (date != null) {
			String pattern = "MM/dd/yyyy HH:mm:ss";
			DateFormat df = new SimpleDateFormat(pattern);
			res = df.format(date);
		}
		return res;
	}
	
	
	public boolean canLeave() {
		Date last= this.getLastPaiement();
		if (last != null) {
			Date now = new Date();
	    	long intervalle = now.getTime() -last.getTime();
	        return TimeUnit.SECONDS.convert(intervalle, TimeUnit.MILLISECONDS) < 10 && payer;
		}
		return false;
	}
	
	public String toString() {
		return "numero : " + Integer.toString(this.getNumero()) + "<br>  date entre : " + dateToString(getDateEntree()) + "<br>   date sortie : " + dateToString(getDateSortie()) + "<br>   date dernier paiement : " + dateToString(getLastPaiement()) + "<br>   dedans depuis : " + Long.toString(getDuration())+ "<br>   payer : " + getPayer();
	}
}
