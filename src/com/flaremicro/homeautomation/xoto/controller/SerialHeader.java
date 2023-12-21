package com.flaremicro.homeautomation.xoto.controller;

/**
 * Serial header builder
 * This class constructs the correct 8-bit X10 header for X10 serial devices.
 * You should <b>not</b> use your own integer values for Type and Transmission.
 *  
 * @author Vulpovile
 *
 */
public class SerialHeader {
	/**
	 * Maximum amount of dim steps. Dims are calculated steps/22 = 100%.
	 * This class will generate a header limited to 22, so you generally won't need to check this.
	 */
	public static final int MAX_DIM_STEPS = 22;
	
	/**
	 * Type code for addressing an X10 device
	 */
	public static final int TYPE_ADDRESS = 0;
	
	/**
	 * Type code for performing an X10 function
	 */
	public static final int TYPE_FUNCTION = 2;

	/**
	 * Standard transmission mode, to perform simple tasks and functions such as dimming.
	 */
	public static final int TRANSMISSION_STANDARD = 0;
	
	/**
	 * Extended transmission mode, to perform extended tasks and functions such as macros.
	 */
	public static final int TRANSMISSION_EXTENDED = 1;
	
	/**
	 * The generated 8 bit header. This is what you send to the X10 serial device.
	 */
	public final byte binaryHeader;

	/**
	 * Generate a header based on percentage to dim/brighten, type of operation, and transmission mode.
	 * @param dimPercentage
	 * @param type
	 * @param transmission
	 */
	public SerialHeader(float dimPercentage, int type, int transmission){
		this((int)(dimPercentage*22), type, transmission);
	}
	
	/**
	 * Generate a header based on the number of steps to dim/brighten, type of operation, and transmission mode.
	 * @param dimSteps - Limited to 22 max steps, will automatically set it to this value if above.
	 * @param type
	 * @param transmission
	 */
	public SerialHeader(int dimSteps, int type, int transmission)
	{
		dimSteps = Math.min(dimSteps, MAX_DIM_STEPS);
		byte buildHeader = (byte)(dimSteps << 3);
		buildHeader |= 4;
		buildHeader |= (type & 0x02);
		buildHeader |= (transmission & 0x01) ;
		binaryHeader = buildHeader;
	}
	
	/**
	 * Reconstructs the dim steps from the 8-bit header
	 * 
	 * @return dimSteps
	 */
	public int getDimSteps()
	{
		return (binaryHeader & 0xF8) >> 3;
	}
	
	/**
	 * Reconstructs the type from the 8-bit header
	 * 
	 * @return type
	 */
	public int getType()
	{
		return (binaryHeader & 0x2) >> 1;
	}
	
	/**
	 * Reconstructs the transmission mode from the 8-bit header
	 * 
	 * @return transmission
	 */
	public int getTransmission()
	{
		return (binaryHeader & 0x1);
	}
}
