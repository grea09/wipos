package fr.utbm.lo53.wipos.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.utbm.lo53.wipos.server.Common;
import fr.utbm.lo53.wipos.server.logic.AccessPoint;
import fr.utbm.lo53.wipos.server.thread.UdpThread;

/**
 * Servlet implementation class Locate
 */
@WebServlet("/Locate")
public class Locate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UdpThread udpThread = new UdpThread();
	private static HashMap<String, Locate> waitList;
	public Map<AccessPoint, Double> rssis;

	public void finalize() throws Throwable
	{
		udpThread.finalize();
	}
	
	public static Locate get(String mac)
	{
		return waitList.get(mac);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mac = Common.toMac(request.getRemoteAddr());
		String[] params = request.getRemoteAddr().split(";");
		assert params.length == 1;
		assert params[0] == getClass().getSimpleName().toUpperCase();
		
		udpThread.start();
		if(!(waitList.containsKey(mac)))
		{
			UdpThread.send("GET;" + mac);
			for (int i = 0; i < Common.ACCESS_POINT_IP.length; i++)
			{
				try
				{
					this.wait();
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//TODO Location locate(double rssi)
			
			
			response.setContentType("text/html;charset=UTF-8");
		    PrintWriter out = response.getWriter();
	
		    out.println("Ha ! ha! your lost !");
		}
		else
		{
			response.setContentType("text/html;charset=UTF-8");
		    PrintWriter out = response.getWriter();
		    out.println("PENDING");
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
