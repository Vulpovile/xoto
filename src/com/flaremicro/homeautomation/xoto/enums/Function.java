package com.flaremicro.homeautomation.xoto.enums;

/**
 * Function enum
 * @author Vulpovile
 *
 */
public enum Function {
	ALL_UNITS_OFF("All Units Off", 						0x00, false),
	ALL_LIGHTS_ON("All Lights On", 						0x01, false),
	ON("On", 											0x02, false),
	OFF("Off", 											0x03, false),
	DIM("Dim", 											0x04, true),
	BRIGHT("Bright", 									0x05, true),
	ALL_LIGHTS_OFF("All Lights Off", 					0x06, false),
	EXTENDED_CODE("Extended Code", 						0x07, false),
	HAIL_REQUEST("Hail Request", 						0x08, false),
	HAIL_ACKNOWLEDGE("Hail Acknowledge", 				0x09, false),
	PRESET_DIM_1("Pre-set Dim (1)", 					0x0A, false),
	PRESET_DIM_2("Pre-set Dim (2)", 					0x0B, false),
	EXTENDED_DATA_TRANSFER("Extended Data Transfer", 	0x0C, false),
	STATUS_ON("Status On", 								0x0D, false),
	STATUS_OFF("Status Off", 							0x0E, false),
	STATUS_REQUEST("Status Request", 					0x0F, false);
	
	private Function(String prettyName, int opcode, boolean isExtended){
		this.opcode = (byte)opcode;
		this.prettyName = prettyName;
		this.isExtended = isExtended;
	}
	
	/**
	 * Gets the function enum from the enum name
	 * 
	 * @param name
	 * @return Function
	 */
	public static Function fromName(String name)
	{
		name = name.trim();
		for(Function function : values()){
			if(function.name().equalsIgnoreCase(name))
				return function;
		}
		return null;
	}
	
	/**
	 * Gets the function enum from the pretty name, case insensitive
	 * 
	 * @param name
	 * @return Function
	 */
	public static Function fromPrettyName(String name)
	{
		name = name.trim();
		for(Function function : values()){
			if(function.prettyName.equalsIgnoreCase(name))
				return function;
		}
		return null;
	}
	
	/**
	 * Gets the function enum from the function opcode. 
	 * 
	 * There really should be no reason to use this one.
	 * @param opcode
	 * @return Function
	 */
	public static Function fromOpcode(byte opcode)
	{
		for(Function function : values()){
			if(function.opcode == opcode)
				return function;
		}
		return null;
	}
	
	/**
	 * The function's pretty name
	 */
	public final String prettyName;

	/**
	 * The X10 signaling device opcode
	 */
	public final byte opcode;
	
	/**
	 * Whether the function has extended features (dimming)
	 */
	public final boolean isExtended;
}
