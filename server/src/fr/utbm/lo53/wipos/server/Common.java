package fr.utbm.lo53.wipos.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.HashMap;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import fr.utbm.lo53.wipos.server.logic.AccessPoint;
import fr.utbm.lo53.wipos.server.logic.Location;
import fr.utbm.lo53.wipos.server.logic.Map;
import fr.utbm.lo53.wipos.server.logic.Rssi;
import fr.utbm.lo53.wipos.server.logic.TempRssi;

public final class Common
{
	public static final String DATABASE_URL = "jdbc:sqlite:server.db";
	public static final int PORT = 1337;
	
	public static final byte[][] ACCESS_POINT_IP = new byte[][] 
			{
				{(byte) 192,(byte) 168,1,2}
			};
	
	public static final Class<?>[] CLASSES = new Class<?>[]{Map.class, Location.class, AccessPoint.class, TempRssi.class, Rssi.class};
	
	private static final String MAC_FILE = "/proc/net/arp";
	private static final int[] MAC_COLUMNS = {41, 58};
	
	@SuppressWarnings({ "null", "unchecked" })
	public static HashMap<Class<?>, Dao<?, Integer>> getDao(ConnectionSource connectionSource) throws SQLException
	{
		HashMap<Class<?>, Dao<?, Integer>> dao = null;
		for(Class<?> clazz: Common.CLASSES)
		{
			dao.put(clazz,(Dao<?, Integer>) DaoManager.createDao(connectionSource, clazz));
			TableUtils.createTable(connectionSource, clazz);
		}
		return dao;
	}
	
	public static String toMac(String ip) throws IOException, FileNotFoundException
	{
		String mac = null;
		RandomAccessFile arpTable = new RandomAccessFile(MAC_FILE, "r");
		while((mac = arpTable.readLine())!= null )
		{
			if(mac.contains(ip))
			{
				//TODO split
				//mac = mac.split(" +")[4];
				mac = mac.substring(MAC_COLUMNS[0],MAC_COLUMNS[1]);
				break;
			}
		}
		arpTable.close();
		return mac;
	}
}
