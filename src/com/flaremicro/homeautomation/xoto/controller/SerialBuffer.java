package com.flaremicro.homeautomation.xoto.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fazecast.jSerialComm.SerialPort;

public class SerialBuffer extends InputStream implements Runnable {

	boolean closed = false;

	SerialPort serialPort;
	Queue<Byte> byteBuffer = new ConcurrentLinkedQueue<Byte>();

	SerialBuffer(SerialPort serialPort) {
		this.serialPort = serialPort;
		new Thread(this).start();
	}

	public int availible() {
		return byteBuffer.size();
	}

	@Override
	public int read() throws IOException {
		if (byteBuffer.size() > 0)
			return byteBuffer.poll();
		else return -1;
	}

	@Override
	public void close() {
		this.closed = true;
	}

	@Override
	public void run() {
		while (!closed)
		{
			if (serialPort.bytesAvailable() > 0)
			{
				byte[] buffer = new byte[serialPort.bytesAvailable()];
				int read = serialPort.readBytes(buffer, buffer.length);
				for (int i = 0; i < read; i++)
				{
					byteBuffer.add(buffer[i]);
					System.out.println("Got 0x" + Integer.toString(buffer[i], 16));
				}
			}
		}
	}

}
