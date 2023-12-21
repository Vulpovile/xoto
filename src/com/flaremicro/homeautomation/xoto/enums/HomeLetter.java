package com.flaremicro.homeautomation.xoto.enums;

/**
 * HomeLetter enum, home opcodes are left shifted by 4 bits to allow easy bit ORing when combining with device number.
 * @author Vulpovile
 */
public enum HomeLetter {
	A((byte)0x60),
	B((byte)0xE0),
	C((byte)0x20),
	D((byte)0xA0),
	E((byte)0x10),
	F((byte)0x90),
	G((byte)0x50),
	H((byte)0xD0),
	I((byte)0x70),
	J((byte)0xF0),
	K((byte)0x30),
	L((byte)0xB0),
	M((byte)0x00),
	N((byte)0x80),
	O((byte)0x40),
	P((byte)0xC0);
	
	private HomeLetter(byte opcode){
		this.opcode = opcode;
	}
	
	/**
	 * Gets the HomeLetter enum from the home name (A-P)
	 * 
	 * 
	 * @param name
	 * @return HomeLetter
	 */
	public HomeLetter fromName(String name)
	{
		name = name.trim();
		for(HomeLetter home : values()){
			if(home.name().equalsIgnoreCase(name))
				return home;
		}
		return null;
	}
	
	/**
	 * Gets the HomeLetter enum from the home opcode. 
	 * 
	 * There really should be no reason to use this one.
	 * @param opcode
	 * @return HomeLetter
	 */
	public static HomeLetter fromOpcode(byte opcode)
	{
		for(HomeLetter home : values()){
			if(home.opcode == opcode)
				return home;
		}
		return null;
	}
	
	/**
	 * Home opcodes are left shifted by 4 bits to allow easy bit ORing when combining with device number.
	 * To access the home code exactly as defined in the X10 spec, be sure to do (opcode >>> 4) or ((opcode >> 4) & 0x0F) to ensure no unnecessary sign gets inserted.
	 */
	public final byte opcode;
}
