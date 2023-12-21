package com.flaremicro.homeautomation.xoto.module;

import java.util.Arrays;
import java.util.HashSet;

import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;

public abstract class X10Module implements Comparable<X10Module>{
	
	protected final HashSet<Function> functions = new HashSet<Function>();
	
	protected HomeLetter homeLetter;
	protected DeviceNumber deviceNumber;
	
	public X10Module(HomeLetter homeNumber, DeviceNumber deviceNumber){
		this.homeLetter = homeNumber;
		this.deviceNumber = deviceNumber;
		
		addFunctions(Function.ALL_LIGHTS_OFF,
								Function.ALL_LIGHTS_ON,
								Function.ALL_UNITS_OFF,
								Function.ALL_LIGHTS_OFF);
	}
	
	public HomeLetter getHomeLetter()
	{
		return homeLetter;
	}
	
	public void setHomeLetter(HomeLetter homeLetter)
	{
		this.homeLetter = homeLetter;
	}
	
	public DeviceNumber getDeviceNumber()
	{
		return deviceNumber;
	}
	
	public void setDeviceNumber(DeviceNumber deviceNumber)
	{
		this.deviceNumber = deviceNumber;
	}
	
	protected void addFunctions(Function... function)
	{
		functions.addAll(Arrays.asList(function));
	}
	
	public boolean hasFunction(Function function)
	{
		return functions.contains(function);
	}
	
	@Override
    public int compareTo(X10Module otherModule) {
        return this.homeLetter.compareTo(otherModule.homeLetter);
    }
	
}
