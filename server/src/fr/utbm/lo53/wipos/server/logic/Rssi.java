package fr.utbm.lo53.wipos.server.logic;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Rssi
{
	public Rssi()
	{
		super();
	}
	@DatabaseField(foreign = true)
	public Location location;
	@DatabaseField(foreign = true)
	public AccessPoint accessPoint;
	@DatabaseField
	public double average;
	@DatabaseField
	public double standardDeviation;
	
	public void fromRequest(String[] params) throws SQLException
	{
		if(params.length > 4)
		{
			accessPoint = AccessPoint.get(params[params.length-2]);
			average = Double.parseDouble(params[params.length-1]);
		}
	}
}
