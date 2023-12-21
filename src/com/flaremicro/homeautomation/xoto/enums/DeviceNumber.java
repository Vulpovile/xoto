package com.flaremicro.homeautomation.xoto.enums;

/**
 * DeviceNumber enum
 * @author Vulpovile
 *
 */
public enum DeviceNumber {
	NULL_DEVICE(0, 0x00),
	_1(1, 0x06),
	_2(2, 0x0E),
	_3(3, 0x02),
	_4(4, 0x0A),
	_5(5, 0x01),
	_6(6, 0x09),
	_7(7, 0x05),
	_8(8, 0x0D),
	_9(9, 0x07),
	_10(10, 0x0F),
	_11(11, 0x03),
	_12(12, 0x0B),
	_13(13, 0x00),
	_14(14, 0x08),
	_15(15, 0x04),
	_16(16, 0x0C);
	private DeviceNumber(int deviceNumber, int opcode){
		this.opcode = (byte)opcode;
		this.deviceNumber = (byte)deviceNumber;
	}
	
	/**
	 * Gets the DeviceNumber enum from the numeric device number (1-16)
	 * 
	 * The absolute <b>fastest</b> to get a DeviceNumber enum without addressing the enum directly.
	 * This method uses a lookup table to get the enum directly.
	 * 
	 * @param deviceNumber
	 */
	public static DeviceNumber fromNumber(int deviceNumber)
	{
		if(deviceNumber >= 0 && deviceNumber < values().length){
			return values()[deviceNumber];
		}
		return null;
	}
	
	/**
	 * Gets the DeviceNumber enum from the device name _(1-16)
	 * <br>
	 * <b>MUST be prefixed by an undersore!</b><br>
	 * Example: _16
	 * 
	 * @param name
	 * @return Device
	 */
	public static DeviceNumber fromName(String name)
	{
		name = name.trim();
		for(DeviceNumber device : values()){
			if(device.name().equalsIgnoreCase(name))
				return device;
		}
		return null;
	}
	
	/**
	 * Gets the DeviceNumber enum from the device opcode. 
	 * 
	 * There really should be no reason to use this one.
	 * @param opcode
	 * @return DeviceNumber
	 */
	public static DeviceNumber fromOpcode(byte opcode)
	{
		for(DeviceNumber device : values()){
			if(device.opcode == opcode)
				return device;
		}
		return null;
	}
	
	/**
	 * The device number, this should not be used to send signals as the number differs from the device opcode
	 */
	public final byte deviceNumber;
	
	/**
	 * The X10 signaling device opcode
	 */
	public final byte opcode;
}
