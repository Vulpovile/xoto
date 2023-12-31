package com.flaremicro.homeautomation.xoto.module.impl;

import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;
import com.flaremicro.homeautomation.xoto.module.X10Module;

/**
 * Null module container class, useful mostly for the "ALL *" functions.
 * @author Vulpovile
 *
 */
public class NullModule extends X10Module {

	public NullModule(HomeLetter homeNumber) {
		super(homeNumber, DeviceNumber.NULL_DEVICE);
	}

}
