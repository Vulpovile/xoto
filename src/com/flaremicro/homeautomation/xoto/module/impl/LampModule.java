package com.flaremicro.homeautomation.xoto.module.impl;

import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;

public class LampModule extends ApplicanceModule {

	public LampModule(HomeLetter homeNumber, DeviceNumber deviceNumber) {
		super(homeNumber, deviceNumber);
		addFunctions(Function.DIM);
		addFunctions(Function.BRIGHT);
	}

}
