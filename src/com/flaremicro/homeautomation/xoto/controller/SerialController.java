package com.flaremicro.homeautomation.xoto.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fazecast.jSerialComm.SerialPort;
import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;
import com.flaremicro.homeautomation.xoto.operation.Operation;

public abstract class SerialController implements Runnable {

	private boolean running = false;
	private boolean terminated = true;

	private final Queue<Operation> operations = new ConcurrentLinkedQueue<Operation>();
	

	protected ArrayList<Integer> addresses = new ArrayList<Integer>();
	protected ArrayList<Integer> lastAddresses = new ArrayList<Integer>();

	protected InputStream is;
	protected OutputStream os;
	protected SerialPort serialPort;

	protected static final int INTERFACE_RDY = 0x55;
	protected static final int RESPONSE_OK = 0x00;
	protected static final int ACCEPT_BUFFER = 0xc3;

	public SerialController(SerialPort serialPort) {
		this.serialPort = serialPort;
		serialPort.setComPortParameters(4800, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		serialPort.openPort();
		is = serialPort.getInputStreamWithSuppressedTimeoutExceptions();
		os = serialPort.getOutputStream();
	}

	public void start() {
		if (terminated)
		{
			running = true;
			terminated = false;
			new Thread(this).start();
		}
	}

	public void end() {
		running = false;
		try
		{
			this.is.close();
			this.os.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		serialPort.closePort();
	}

	@Override
	public void run() {
		while (running)
		{
			try
			{
				int read = is.read();
				if (read != -1)
				{
					handleByte(read);
				}
				while (!operations.isEmpty())
				{
					Operation operation = operations.poll();
					operation.performOperation(this);
					System.out.println("Operation completed");
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		terminated = true;
	}

	public void insertOperation(Operation operation) {
		operations.add(operation);
	}

	public boolean waitForChecksum(int checksum, int timeout) throws IOException, InterruptedException {
		long timeStarted = System.currentTimeMillis() + timeout;
		while (running)
		{
			int recievedChecksum = is.read();
			if (recievedChecksum == -1)
			{
				if (timeStarted < System.currentTimeMillis())
					throw new IOException("Reading expected value timed out");
				Thread.sleep(500L);
				continue;
			}
			else if (recievedChecksum == checksum)
			{
				System.out.println("Got " + Integer.toString(recievedChecksum, 16));
				return true;
			}
			else
			{
				System.out.println("Got " + Integer.toString(recievedChecksum, 16) + ", expected " + Integer.toString(checksum, 16));
				return false;
			}
		}
		return false;
	}

	public void waitForReady(int timeout) throws IOException, InterruptedException {
		waitForChecksum(INTERFACE_RDY, timeout);
	}

	protected int waitForByte(int timeout) throws IOException, InterruptedException {
		long timeStarted = System.currentTimeMillis() + timeout;
		int recievedByte = -1;
		while (running)
		{
			recievedByte = is.read();
			if (recievedByte == -1)
			{
				if (timeStarted < System.currentTimeMillis())
					throw new IOException("Reading expected value timed out");
				Thread.sleep(500L);
				continue;
			}
			else
			{
				return recievedByte;
			}
		}
		return -1;
	}

	public void address(HomeLetter homeLetter, DeviceNumber deviceNumber) throws IOException, InterruptedException {
		SerialHeader serialHeader = new SerialHeader(0, SerialHeader.TYPE_ADDRESS, SerialHeader.TRANSMISSION_STANDARD);
		int addressByte = homeLetter.opcode | deviceNumber.opcode;
		int checksum = (serialHeader.binaryHeader + addressByte) & 0xFF;

		boolean isChecksumOK = false;
		while (!isChecksumOK)
		{
			System.out.println("Writing 0x" + Integer.toString(serialHeader.binaryHeader) + ", 0x" + Integer.toString(addressByte, 16));
			os.write(serialHeader.binaryHeader);
			os.write(addressByte);
			System.out.println("Waiting for checksum...");
			isChecksumOK = waitForChecksum(checksum, 5000);
		}
		System.out.println("Checksum OK, sending OK signal");
		os.write(RESPONSE_OK);
		waitForReady(5000);
		
		addresses.add(addressByte);
	}

	public void perform(HomeLetter homeLetter, int dimSteps, Function function) throws IOException, InterruptedException {
		SerialHeader serialHeader = new SerialHeader(dimSteps, SerialHeader.TYPE_FUNCTION, SerialHeader.TRANSMISSION_STANDARD);
		int functionByte = homeLetter.opcode | function.opcode;
		int checksum = (serialHeader.binaryHeader + functionByte) & 0xFF;

		boolean isChecksumOK = false;
		while (!isChecksumOK)
		{
			System.out.println("Writing 0x" + Integer.toString(serialHeader.binaryHeader) + ", 0x" + Integer.toString(functionByte, 16));
			os.write(serialHeader.binaryHeader);
			os.write(functionByte);
			System.out.println("Waiting for checksum...");
			isChecksumOK = waitForChecksum(checksum, 10000);
		}
		System.out.println("Checksum OK, sending OK signal");
		os.write(RESPONSE_OK);
		waitForReady(10000);
		
		lastAddresses.clear();
		lastAddresses.addAll(addresses);
		addresses.clear();
	}

	public abstract void handleByte(int read) throws InterruptedException, IOException;

}
