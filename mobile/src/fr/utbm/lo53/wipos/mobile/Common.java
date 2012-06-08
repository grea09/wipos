package fr.utbm.lo53.wipos.mobile;

import java.net.MalformedURLException;
import java.net.URL;

public final class Common
{
	public static final int PORT = 1337;
	public static final URL SERVER;
	static
	{
		URL tmp = null; //Whoa ! Java sucks sometime ...
		try
		{
			tmp = new URL("http://192.168.1.56");
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SERVER = tmp;
	}
	public static final long BURST_TIME = 500;
}
