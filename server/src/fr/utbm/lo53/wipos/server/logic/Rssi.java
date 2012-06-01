package fr.utbm.lo53.wipos.server.logic;

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
}
