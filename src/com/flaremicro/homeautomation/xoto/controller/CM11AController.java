package com.flaremicro.homeautomation.xoto.controller;

import java.io.IOException;
import java.util.Calendar;

import com.fazecast.jSerialComm.SerialPort;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;

public class CM11AController extends SerialController{

	public CM11AController(SerialPort serialPort) {
		super(serialPort);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleByte(int read) throws InterruptedException, IOException {
		if(read == 0xa5){
			//Time request
			//os.write(0xfb);
			os.write(0x9b);
			
			Calendar calendar = Calendar.getInstance();
			os.write(calendar.get(Calendar.SECOND) & 0xFF);
			os.write(calendar.get(Calendar.MINUTE) & 0xFF);
			os.write(calendar.get(Calendar.HOUR) & 0xFF);
			os.write(calendar.get(Calendar.DAY_OF_YEAR) & 0xFF);
			os.write(0x01 & 0xFF);
			os.write(HomeLetter.A.opcode | 0x04);
			os.flush();
			System.out.println("Serial controller on port " + serialPort.getDescriptivePortName() + " lost power! Port is back up and time is set");
			Thread.sleep(500L);
		}
	}

}
