package fr.utbm.lo53.wipos.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.utbm.lo53.wipos.server.Common;
import fr.utbm.lo53.wipos.server.logic.Location;
import fr.utbm.lo53.wipos.server.logic.Map;
import fr.utbm.lo53.wipos.server.thread.UdpThread;

/**
 * Servlet implementation class Mesure
 */
@WebServlet("/Mesure")
public class Measure extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UdpThread udpThread = new UdpThread();
	private static DatagramSocket socket;
	static
	{
		try {
			socket = new DatagramSocket(Common.PORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public void finalize() throws Throwable
	{
		udpThread.finalize();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mac = Common.toMac(request.getRemoteAddr());
		String[] params = request.getRemoteAddr().split(";");
		assert params.length == 4;
		assert params[0] == getClass().getSimpleName().toUpperCase();
		
		Location location = new Location();
		location.fromRequest(params);
		
		Map map = new Map();
		map.fromRequest(params);
		
		udpThread.start();
		
		byte[] buffer = ("GET;" + location + map + mac).getBytes();
		
		for(byte[] accessPointIp: Common.ACCESS_POINT_IP)
		{
			InetAddress address = InetAddress.getByAddress(accessPointIp);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 1234);
			socket.send(packet);
		}
		
		response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    while(true);
	    //out.println("OK");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
