package ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.Ticket;

@Stateless
@Remote
public class OperationBean implements Operation {
    
    @PersistenceContext
    private EntityManager em;
    
    public OperationBean() {
        super();
    }
    
    @Override
    public Ticket creerTicket() {
        Ticket ticket = new Ticket();
        em.persist(ticket);
        return ticket;
    }

    @Override
	public Ticket getTicket(int numero) {
		return em.find((Ticket.class), numero);
	}
    
    public void Payer(int numero) {
    	em.find((Ticket.class), numero).addPaiement();;
    }

	@Override
	public void updateExit(int numero) {
		em.find((Ticket.class), numero).updateExit();;
	}
    
    
}