package fr.utbm.lo53.wipos.server.logic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Location
{
	public Location()
	{
		super();
	}
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField
	public double x;
	@DatabaseField
	public double y;
	@DatabaseField(foreign = true)
	public Map map;
}
