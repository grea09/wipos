package fr.utbm.lo53.wipos.mobile.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.AsyncTask;
import fr.utbm.lo53.wipos.mobile.Common;

public class Measure extends AsyncTask<Long, Void, Void>
{
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
			DataOutputStream dataOuputStream = (DataOutputStream) (Common.SERVER.openConnection().getOutputStream());
			dataOuputStream.writeBytes("MEASURE;" + coordinate[0] + ";" + coordinate[1] + ";" + 1); // TODO mid
			dataOuputStream.flush();
			dataOuputStream.close();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Common.SERVER.openConnection().getInputStream()));
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