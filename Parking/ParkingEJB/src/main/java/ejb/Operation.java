package ejb;

import java.util.List;

import jpa.Ticket;

public interface Operation {
	public Ticket creerTicket();
	public Ticket getTicket(int numero);
	public void Payer(int numero);
	public void updateExit(int numero);
}
