package fr.utbm.lo53.wipos.server.logic;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Map
{
	public Map()
	{
		super();
	}
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(width = 100)
	public String description;
	@DatabaseField
	public int pixelWidth;
	@DatabaseField
	public int pixelHeight;
	@DatabaseField
	public double meterWidth;
	@DatabaseField
	public double meterHeight;
	@DatabaseField(dataType=DataType.BYTE_ARRAY)
	public byte[] content;
	
	public String toString()
	{
		return id +";";
	}
	
	public void fromRequest(String[] params)
	{
		if(params.length > 4)
		{
			id = Integer.decode(params[3]);
		}
	}
}
