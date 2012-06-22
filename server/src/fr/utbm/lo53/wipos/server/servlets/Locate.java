package fr.utbm.lo53.wipos.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import fr.utbm.lo53.wipos.server.Common;
import fr.utbm.lo53.wipos.server.logic.AccessPoint;
import fr.utbm.lo53.wipos.server.logic.Location;
import fr.utbm.lo53.wipos.server.thread.UdpThread;

/**
 * Servlet implementation class Locate
 */
@WebServlet("/Locate")
public class Locate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UdpThread udpThread = new UdpThread();
	private static HashMap<String, Locate> waitList = new HashMap<String, Locate>();
	private String mac;
	
	public Map<AccessPoint, Double> rssis = new HashMap<AccessPoint, Double>();

	public void finalize() throws Throwable
	{
		udpThread.finalize();
	}
	
	public static Locate get(String mac)
	{
		return waitList.get(mac);
	}
	
	protected Location locate() throws SQLException
	{
		ConnectionSource connectionSource = new JdbcConnectionSource(Common.DATABASE_URL);
		Dao<Location, String> rawDao = DaoManager.createDao(connectionSource, Location.class);
		GenericRawResults<String[]> result = rawDao.queryRaw(
			    "select id " +
			    "from TempRssi as T " +
			    "inner join Rssi as R " +
			    "on T.accessPoint = R.accessPoint " +
			    "where T.clientMac = '" + mac + "' " + 
			    "group by 1 " +
			    "order by (avg(abs(R.average - T.average))) asc limit 1"
			    );
		int id = Integer.parseInt(result.getFirstResult()[0]);
		result.close();
		Dao<Location, String> locationDao = DaoManager.createDao(connectionSource, Location.class);
		QueryBuilder<Location, String> statementBuilder = locationDao.queryBuilder();
		statementBuilder.where().eq("id", id);
		return locationDao.queryForFirst(statementBuilder.prepare());
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mac = Common.toMac(request.getRemoteAddr());
		String[] params = request.getRemoteAddr().split(";");
		assert params.length == 1;
		assert params[0] == getClass().getSimpleName().toUpperCase();
		
		if(!(waitList.containsKey(mac)))
		{
			this.mac = mac;
			udpThread.start();
			
			response.setContentType("text/html;charset=UTF-8");
		    PrintWriter out = response.getWriter();
		    
			UdpThread.send("GET;" + mac);
			waitList.put(mac, this);
			for (int i = 0; i < Common.ACCESS_POINT_IP.length; i++)
			{
				synchronized (this) {
					try
					{
						this.wait(5000);
					} catch (InterruptedException e)
					{
						out.println("ERROR");
						e.printStackTrace(out);
					}
				}
			}
			
			Location location = null;
			try
			{
				location = locate();
			} catch (SQLException e)
			{
				out.println("ERROR");
				e.printStackTrace(out);
			}
			if(location == null)
			{
				out.println("ERROR : Cannot find location. Or we don't have data in DB or we have no response from AP" );
			}
			else
			{
				out.println("LOCATION;" + location + location.map);
			}
	
		    
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
