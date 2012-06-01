package fr.utbm.lo53.wipos.server.logic;

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
	
	public void fromRequest(String[] params)
	{
		if(params.length > 3)
		{
			//TODO getAP from Mac
			//AccessPoint.get(params[params.length-3]);
			//if null then insert
			clientMac = params[params.length-2];
			average = Double.parseDouble(params[params.length-1]);
		}
	}
}
