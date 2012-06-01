package fr.utbm.lo53.wipos.server.servlets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import fr.utbm.lo53.wipos.server.logic.Constants;
import fr.utbm.lo53.wipos.server.logic.Location;
import fr.utbm.lo53.wipos.server.logic.Map;

/**
 * Servlet implementation class Mesure
 */
@WebServlet("/Mesure")
public class Mesure extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ConnectionSource connectionSource;
	private HashMap<Class<?>, Dao<?, String>> daos;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public Mesure() {
    	super();
		try
		{
			// create a connection source to our database
	        connectionSource = new JdbcConnectionSource(Constants.DATABASE_URL);
	        daos = Constants.getDao(connectionSource);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void finalize()
    {
    	try
		{
    		// close the connection source
			connectionSource.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mac = mac(request.getRemoteAddr());
		String[] params = request.getRemoteAddr().split(";");

		Location location = new Location();
		location.fromRequest(params);
		
		Map map = new Map();
		map.fromRequest(params);
		
		byte[] buffer = ("GET;" + location + map + mac).getBytes();
		
		for(byte[] accessPointIp: Constants.ACCESS_POINT_IP)
		{
			DatagramSocket socket = new DatagramSocket(Constants.PORT);
			InetAddress address = InetAddress.getByAddress(accessPointIp);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 1234);
			socket.send(packet);
		}
		
		response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();

	    out.println("OK");
	}
	
	protected String mac(String ip) throws IOException, FileNotFoundException
	{
		String mac = null;
		//TODO string constant
		RandomAccessFile arpTable = new RandomAccessFile("/proc/net/arp", "r");
		while((mac = arpTable.readLine())!= null )
		{
			if(mac.contains(ip))
			{
				//TODO columns constants
				mac = mac.substring(41,58);
				break;
			}
		}
		arpTable.close();
		return mac;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
