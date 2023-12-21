package com.flaremicro.homeautomation.xoto.module.impl;

import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;
import com.flaremicro.homeautomation.xoto.module.X10Module;

public class ApplicanceModule extends X10Module {

	public ApplicanceModule(HomeLetter homeNumber, DeviceNumber deviceNumber) {
		super(homeNumber, deviceNumber);
		addFunctions(Function.ON);
		addFunctions(Function.OFF);
	}

}
