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

/**
 * Abstract class for dealing with most serial-based controllers. CP290 and CM17A may differ, however, but they are generally outliers.
 * Runs as a thread to process input as well.
 * 
 * A base class for all controllers may be necessary when expanding to different types (i.e. USB with CM15A)
 * 
 * @author Vulpovile
 *
 */
public abstract class SerialController implements Runnable {

	private boolean running = false;
	private boolean terminated = true;

	private final Queue<Operation> operations = new ConcurrentLinkedQueue<Operation>();
	

	/**
	 * Utilized by the controller to determine what the address incoming is. 
	 * One STATE_CHANGE signal may not have all the information in the buffer yet, 
	 * so best to keep these until no longer required.
	 */
	protected ArrayList<Integer> addresses = new ArrayList<Integer>();
	
	/**
	 * Used to determine which address got a dim/bright signal
	 */
	protected ArrayList<Integer> lastAddresses = new ArrayList<Integer>();

	/**
	 * Serial port input
	 */
	protected InputStream is;
	/**
	 * Serial port output
	 */
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

	/**
	 * Waits for a specific checksum, and returns true if the checksum is equal.
	 * 
	 * @param checksum
	 * @param timeout
	 * @return checksum equal
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	/**
	 * Waits for the INTERFACE_RDY signal 
	 * (currently waits for any signal, TODO improve this)
	 * 
	 * @param timeout
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void waitForReady(int timeout) throws IOException, InterruptedException {
		waitForChecksum(INTERFACE_RDY, timeout);
	}

	/**
	 * Waits for the next byte and returns once read, wait 500ms on no input.
	 * 
	 * @param timeout
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	/**
	 * Tell the controller to address the specific home and device number.
	 * 
	 * Currently, a failed address call with an invalid checksum can cause an infinite loop. TODO improve
	 * 
	 * @param homeLetter
	 * @param deviceNumber
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	/**
	 * Tell the controller to perform a function. Home letter is likely unnecessary, but this is how X10 works, so this is how the call is implemented.
	 * 
	 * 
	 * @param homeLetter
	 * @param dimSteps - Not useful for anything other than Bright/Dim functions
	 * @param function
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	/**
	 * Handles a byte input from the serial controller. Inputs and their handling may differ, so this function is abstract.
	 * 
	 * TODO see if CM11A and CM10A have anything in common and move here instead 
	 * 
	 * @param read
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected abstract void handleByte(int read) throws InterruptedException, IOException;

}
