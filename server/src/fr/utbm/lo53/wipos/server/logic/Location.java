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
	
	public String toString()
	{
		return x+";"+y+";";
	}

	public void fromRequest(String[] params)
	{
		if(params.length > 4)
		{
			x = Integer.decode(params[1]);
			y = Integer.decode(params[2]);
		}
	}
}
