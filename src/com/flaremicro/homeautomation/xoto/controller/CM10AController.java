package com.flaremicro.homeautomation.xoto.controller;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;

public class CM10AController extends SerialController {

	private static final int POWER_FAIL = 0xa5;
	private static final int STATE_CHANGE = 0x5a;
	
	private Function function = null;
	
	public CM10AController(SerialPort serialPort) {
		super(serialPort);
	}

	@Override
	public void handleByte(int read) throws InterruptedException, IOException {
			if(read == POWER_FAIL){
				//Send macro header
				os.write(0xfb);
				for (int i = 0; i < 42; i++)
					os.write(0x00);
				os.flush();
				waitForChecksum(0x00, 10000);
				os.write(RESPONSE_OK);
				os.flush();
				waitForChecksum(INTERFACE_RDY, 10000);
				System.out.println("Serial controller on port " + serialPort.getDescriptivePortName() + " lost power! Port is back up and time is set");
				Thread.sleep(500L);
			}
			else if(read == STATE_CHANGE){
				os.write(ACCEPT_BUFFER);
				int length = waitForByte(1000);
				while(length == STATE_CHANGE) //Sends every second, prevent issues if sent as length
					length = waitForByte(1000);
				if(length > 0)
				{
					int funcmask = waitForByte(1000);
					for (int i = 0; i < length - 1; i++)
					{
						boolean isFunction = (funcmask & 1) == 1;
						funcmask >>>= 1;
						int recvByte = waitForByte(1000);
						if (!isFunction)
						{
							addresses.add(recvByte);
						}
						else
						{
							int functionOpcode = recvByte & 0x0F;
							function = Function.fromOpcode((byte) (functionOpcode));
							int extension = 0;
							if (function.isExtended)
							{
								extension = waitForByte(1000);
								i += 1;
							}
							if(addresses.isEmpty())
							{
								for (int address : lastAddresses)
								{
									HomeLetter home = HomeLetter.fromOpcode((byte) (address & 0xF0));
									DeviceNumber deviceNumber = DeviceNumber.fromOpcode((byte) (address & 0x0F));
									System.out.println("Got message that device " + home.toString() + deviceNumber.deviceNumber + " performed function " + function.prettyName);
									if(extension > 0)
										System.out.println("Performed function with extension " + extension);
								}
							}
							else
							{
								for (int address : addresses)
								{
									HomeLetter home = HomeLetter.fromOpcode((byte) (address & 0xF0));
									DeviceNumber deviceNumber = DeviceNumber.fromOpcode((byte) (address & 0x0F));
									System.out.println("Got message that device " + home.toString() + deviceNumber.deviceNumber + " performed function " + function.prettyName);
									if(extension > 0)
										System.out.println("Performed function with extension " + extension);
									lastAddresses.clear();
									lastAddresses.addAll(addresses);
								}
							}
							function = null;
							//TODO do something with the information you just got.
							addresses.clear();
						}
					}
				}
			}
				/*System.out.println(b);
				if (b > 0)
				{
					int funcmask = waitForByte(1000);
					ArrayList<Integer> addresses = new ArrayList<Integer>();
					for (int i = 0; i < b - 1; i++)
					{
						funcmask >>>= 1;
						boolean isFunction = (funcmask & 1) == 1;
						int recvByte = waitForByte(1000);
						System.out.println(Integer.toString(recvByte, 16));
						if (!isFunction)
						{
							addresses.add(recvByte);
						}
						else
						{
							int functionOpcode = recvByte & 0x0F;
							Function function = Function.fromOpcode((byte) (functionOpcode));
							int extension = 0;
							if (function.isExtended)
							{
								extension = waitForByte(1000);
							}
							for (int address : addresses)
							{
								HomeLetter home = HomeLetter.fromOpcode((byte) (address & 0xF0));
								DeviceNumber deviceNumber = DeviceNumber.fromOpcode((byte) (address & 0x0F));
								System.out.println("Got message that device " + home.toString() + deviceNumber.deviceNumber + " performed function " + function.prettyName);
							}
							//TODO do something with the information you just got.
							addresses.clear();
						}
					}
				}*/
	}
}
