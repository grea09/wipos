package fr.utbm.lo53.wipos.server.thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import fr.utbm.lo53.wipos.server.logic.Constants;
import fr.utbm.lo53.wipos.server.logic.Location;
import fr.utbm.lo53.wipos.server.logic.Map;

public class UdpThread extends Thread
{
	private ConnectionSource connectionSource;
	private HashMap<Class<?>, Dao<?, String>> daos;
	private static UdpThread instance;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public UdpThread() {
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
	
	@Override
	public void run()
	{
		if(instance == null)
			instance = this;
		if(instance == this)
		{
			while(true)
			{
				try
				{
					byte[]buffer = new byte[100 + 23 + 10]; //23 = -xxx and 10 for precision
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					DatagramSocket socket = new DatagramSocket(1234);
					socket.receive(packet);
					String[] params = new String(packet.getData(), 0, packet.getLength()).split(";");
					
					//TODO verify response
					Location location = new Location();
					location.fromRequest(params);
					
					Map map = (Map) daos.get(Map.class).queryForId(params[3]);
					
					//TODO getAP from Mac
					location.map = map;
				}
				catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				catch (SQLException e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
}
