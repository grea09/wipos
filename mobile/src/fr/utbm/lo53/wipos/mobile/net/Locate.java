package fr.utbm.lo53.wipos.mobile.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;
import fr.utbm.lo53.wipos.mobile.Common;

public class Locate extends AsyncTask<Void, Void, Long[]>
{
	public static final URL LOCATE;
	static
	{
		URL tmp = null; //Whoa ! Java sucks sometime ...
		try
		{
			tmp = new URL(Common.SERVER + "Locate");
		} catch (MalformedURLException e)
		{
			Log.e(Locate.class.getName(), "Network", e);
		}
		LOCATE = tmp;
	}
	
	
	@Override
	protected Long[] doInBackground(Void... arg0)
	{
		byte[] buffer = "SPAM".getBytes();
		try
		{
			DatagramSocket socket = new DatagramSocket(Common.PORT);
			long begin = System.currentTimeMillis();
			while((System.currentTimeMillis() - begin) < Common.BURST_TIME)
			{
				InetAddress address = InetAddress.getByAddress(new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255});
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Common.PORT);
				socket.send(packet);
				Thread.yield();
			}
			DataOutputStream dataOuputStream = (DataOutputStream) (LOCATE.openConnection().getOutputStream());
			dataOuputStream.writeBytes("LOCATE");
			dataOuputStream.flush();
			dataOuputStream.close();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(LOCATE.openConnection().getInputStream()));
			String[] params = bufferedReader.readLine().split(";");
			bufferedReader.close();
			assert params.length == 4;
			assert params[0] == "LOCATION";
			//TODO parse mapId
			return new Long[]{Long.valueOf(params[1]),Long.valueOf(params[2])};
		}
		catch (IOException e)
		{
			Log.e(getClass().getName(), "Network", e);
		}
		return null;
	}
}
