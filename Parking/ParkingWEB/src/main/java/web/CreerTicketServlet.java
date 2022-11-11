package web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.Operation;
import jpa.Ticket;

@WebServlet("/CreerTicketServlet")
public class CreerTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private Operation op;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreerTicketServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
        Ticket ticket = op.creerTicket();
        
        request.setAttribute("ticket", ticket);
        
        request.getRequestDispatcher("/infoTicket.jsp").forward(request, response);    
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}