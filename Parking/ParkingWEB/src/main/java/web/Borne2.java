package web;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.Operation;
import jpa.Ticket;

@WebServlet("/Borne2")
public class Borne2 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private Operation op;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Borne2() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String numero = request.getParameter("numero");
    	if (!numero.isEmpty()) {
    		int num = Integer.parseInt(numero);
        	
        	Ticket element = op.getTicket(num);
        	
        	if (element != null) {
        		request.setAttribute("ticket", element);
        		request.getRequestDispatcher("/infoTicket.jsp").forward(request, response);
        	}
    	}
    	request.getRequestDispatcher("/borne2.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}