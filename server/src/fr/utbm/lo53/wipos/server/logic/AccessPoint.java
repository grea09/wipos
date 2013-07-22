package fr.utbm.lo53.wipos.server.logic;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;

import fr.utbm.lo53.wipos.server.Common;

@DatabaseTable
public class AccessPoint
{
	public AccessPoint()
	{
		super();
	}
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(width = 18)
	public String macAddr;
	
	public static AccessPoint get(String mac) throws SQLException
	{
		ConnectionSource connectionSource = new JdbcConnectionSource(Common.DATABASE_URL);
		Dao<AccessPoint, String> dao = DaoManager.createDao(connectionSource, AccessPoint.class);
		QueryBuilder<AccessPoint, String> statementBuilder = dao.queryBuilder();
		statementBuilder.where().eq("macAddr", mac);
		AccessPoint accessPoint = dao.queryForFirst(statementBuilder.prepare());
		if(accessPoint == null)
		{
			accessPoint = new AccessPoint();
			accessPoint.macAddr = mac;
			dao.createIfNotExists(accessPoint);
		}
		return accessPoint;
	}
}
