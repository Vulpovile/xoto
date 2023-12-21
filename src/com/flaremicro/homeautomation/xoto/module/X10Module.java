package com.flaremicro.homeautomation.xoto.module;

import java.util.Arrays;
import java.util.HashSet;

import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;

/**
 * X10 Module abstract container class.
 * Contains all information necessary for a certain type of controller.<br><br>
 * 
 * TODO add Name, Location, and other useful information for UI
 *
 *
 * @author Vulpovile
 *
 */
public abstract class X10Module implements Comparable<X10Module>{
	
	/**
	 * The list of permitted functions. This should in general be checked before sending a command, 
	 * though no harm comes from sending an invalid function to a module, i.e. Dim to an Appliance Module.
	 */
	private final HashSet<Function> functions = new HashSet<Function>();
	
	private HomeLetter homeLetter;
	private DeviceNumber deviceNumber;
	
	/**
	 * Initializer for the X-10 Module container.
	 * @param homeNumber
	 * @param deviceNumber
	 */
	public X10Module(HomeLetter homeNumber, DeviceNumber deviceNumber){
		this.homeLetter = homeNumber;
		this.deviceNumber = deviceNumber;
		
		addFunctions(Function.ALL_LIGHTS_OFF,
								Function.ALL_LIGHTS_ON,
								Function.ALL_UNITS_OFF);
	}
	
	/**
	 * Get the currently set home letter
	 * @return {@link com.flaremicro.homeautomation.xoto.enums.HomeLetter HomeLetter}
	 */
	public HomeLetter getHomeLetter()
	{
		return homeLetter;
	}
	
	/**
	 * Set a new home letter
	 * @param homeLetter
	 */
	public void setHomeLetter(HomeLetter homeLetter)
	{
		this.homeLetter = homeLetter;
	}
	
	/**
	 * Get the currently set device number
	 * @return {@link com.flaremicro.homeautomation.xoto.enums.DeviceNumber DeviceNumber}
	 */
	public DeviceNumber getDeviceNumber()
	{
		return deviceNumber;
	}
	
	/**
	 * Set a new device number
	 * @param deviceNumber
	 */
	public void setDeviceNumber(DeviceNumber deviceNumber)
	{
		this.deviceNumber = deviceNumber;
	}
	
	/**
	 * Add permitted functions to the list of allowed functions for this device
	 * @param function
	 */
	protected void addFunctions(Function... function)
	{
		functions.addAll(Arrays.asList(function));
	}
	
	/**
	 * Returns true if this module supports the function
	 * 
	 * @param function
	 * @return hasFunction
	 */
	public boolean hasFunction(Function function)
	{
		return functions.contains(function);
	}
	
	@Override
    public int compareTo(X10Module otherModule) {
        return this.homeLetter.compareTo(otherModule.homeLetter);
    }
	
}
