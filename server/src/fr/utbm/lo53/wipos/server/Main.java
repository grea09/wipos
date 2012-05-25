package fr.utbm.lo53.wipos.server;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import fr.utbm.lo53.wipos.server.logic.Map;

public class Main
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		// this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:sqlite::memory";
		try
		{
			// create a connection source to our database
	        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
			// instantiate the dao
	        Dao<Map, String> accountDao =
	            DaoManager.createDao(connectionSource, Map.class);

	        // if you need to create the 'accounts' table make this call
	        TableUtils.createTable(connectionSource, Map.class);
	        
	        // close the connection source
	        connectionSource.close();
	     
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}
	
}
