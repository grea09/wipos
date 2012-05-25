package fr.utbm.lo53.wipos.server.logic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
}
