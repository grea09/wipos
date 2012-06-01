package fr.utbm.lo53.wipos.server.logic;

import java.sql.SQLException;
import java.util.HashMap;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public final class Constants
{
	public static final String DATABASE_URL = "jdbc:sqlite:server.db";
	public static final int PORT = 1337;
	
	public static final byte[][] ACCESS_POINT_IP = new byte[][] 
			{
				{(byte) 192,(byte) 168,1,1}
			};
	
	public static final Class<?>[] CLASSES = new Class<?>[]{Map.class, Location.class, AccessPoint.class, TempRssi.class, Rssi.class};
	@SuppressWarnings({ "null", "unchecked" })
	public static HashMap<Class<?>, Dao<?, String>> getDao(ConnectionSource connectionSource) throws SQLException
	{
		HashMap<Class<?>, Dao<?, String>> dao = null;
		for(Class<?> clazz: Constants.CLASSES)
        {
        	dao.put(clazz,(Dao<?, String>) DaoManager.createDao(connectionSource, clazz));
	        TableUtils.createTable(connectionSource, clazz);
        }
		return dao;
	}
}
