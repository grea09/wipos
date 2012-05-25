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
}
