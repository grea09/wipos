package fr.utbm.lo53.wipos.server.thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import fr.utbm.lo53.wipos.server.Common;
import fr.utbm.lo53.wipos.server.logic.Location;
import fr.utbm.lo53.wipos.server.logic.Map;
import fr.utbm.lo53.wipos.server.logic.Rssi;
import fr.utbm.lo53.wipos.server.logic.TempRssi;
import fr.utbm.lo53.wipos.server.servlets.Locate;

public class UdpThread extends Thread
{
	private ConnectionSource connectionSource;
	private HashMap<Class<?>, Dao<?, Integer>> daos;
	private static UdpThread instance;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public UdpThread() {
    	super();
		try
		{
			// create a connection source to our database
	        connectionSource = new JdbcConnectionSource(Common.DATABASE_URL);
	        daos = Common.getDao(connectionSource);
	     
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	@Override
	public void finalize() throws Throwable
    {
    		super.finalize();
    		// close the connection source
			connectionSource.close();
    }
	
	public static void send(String message) throws IOException
	{
		byte[] buffer = message.getBytes();
		DatagramSocket socket = new DatagramSocket(Common.PORT);
		
		for(byte[] accessPointIp: Common.ACCESS_POINT_IP)
		{
			InetAddress address = InetAddress.getByAddress(accessPointIp);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Common.PORT);
			socket.send(packet);
		}
	}
	
	@Override
	public void run()
	{
		if(!(instance instanceof UdpThread))
			instance = this;
		if(instance == this)
		{
			while(true)
			{
				try
				{
					byte[]buffer = new byte[100];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					DatagramSocket socket = new DatagramSocket(1234);
					socket.receive(packet);
					String[] params = new String(packet.getData(), 0, packet.getLength()).split(";");
					
					//TODO Verify and parse data
					assert params.length >= 4;
					
					TempRssi tempRssi = new TempRssi();
					tempRssi.fromRequest(params);
					createIfNotExists(tempRssi);
					
					if(params.length > 4)
					{
						Map map = (Map) daos.get(Map.class).queryForId(Integer.decode(params[3]));
						
						Location location = new Location();
						location.fromRequest(params);
						location.map = map;
						createIfNotExists(location);
						
						Rssi rssi = new Rssi();
						rssi.fromRequest(params);
						createIfNotExists(rssi);
					}
					else
					{
						Locate locate = Locate.get(tempRssi.clientMac);
						locate.rssis.put(tempRssi.accessPoint, tempRssi.average);
						locate.notify();
					}
					
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
	
	@SuppressWarnings("unchecked")
	private<T> void createIfNotExists(final T dbo) throws SQLException
	{
		((Dao<T, Integer>)daos.get(dbo.getClass())).createIfNotExists(dbo);
	}
}
