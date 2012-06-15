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
import fr.utbm.lo53.wipos.mobile.Common;

public class Measure extends AsyncTask<Long, Void, Void>
{
	public static final URL MEASURE;
	static
	{
		URL tmp = null; //Whoa ! Java sucks sometime ...
		try
		{
			tmp = new URL(Common.SERVER + "Measure");
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MEASURE = tmp;
	}
	
	@Override
	protected Void doInBackground(Long... coordinate)
	{
		assert coordinate.length == 2;
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
			DataOutputStream dataOuputStream = (DataOutputStream) (MEASURE.openConnection().getOutputStream());
			dataOuputStream.writeBytes("MEASURE;" + coordinate[0] + ";" + coordinate[1] + ";" + 1); // TODO mid
			dataOuputStream.flush();
			dataOuputStream.close();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(MEASURE.openConnection().getInputStream()));
			String[] params = bufferedReader.readLine().split(";");
			bufferedReader.close();
			assert params[0] == "OK";
			//TODO parse mapId
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
