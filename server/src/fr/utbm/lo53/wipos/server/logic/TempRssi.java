package fr.utbm.lo53.wipos.server.logic;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TempRssi
{
	public TempRssi()
	{
		super();
	}
	@DatabaseField(foreign = true)
	public AccessPoint accessPoint;
	@DatabaseField(width = 18)
	public String clientMac;
	@DatabaseField
	public double average;
	
	public void fromRequest(String[] params) throws SQLException
	{
		if(params.length > 3)
		{
			accessPoint = AccessPoint.get(params[params.length-2]);
			clientMac = params[params.length-3];
			average = Double.parseDouble(params[params.length-1]);
		}
	}
}
