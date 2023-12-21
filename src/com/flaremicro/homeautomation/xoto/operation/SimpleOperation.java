package com.flaremicro.homeautomation.xoto.operation;

import java.io.IOException;
import java.util.Arrays;

import com.flaremicro.homeautomation.xoto.controller.SerialController;
import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;
import com.flaremicro.homeautomation.xoto.module.X10Module;

public class SimpleOperation extends Operation {
	private final X10Module[] modules;
	private final Function function;
	private final int dimSteps;

	public SimpleOperation(Function function, int dimSteps, X10Module... modules) throws UnsupportedFunctionException {
		this.function = function;
		this.modules = modules;
		this.dimSteps = dimSteps;
		for(int i = 0; i < modules.length; i++)
		{
			if(!modules[i].hasFunction(function))
				throw new UnsupportedFunctionException(modules[i], function);
		}
		Arrays.sort(this.modules);
	}
	
	public void performOperation(SerialController serialController) throws IOException, InterruptedException
	{
		int currModuleIndex = 0;
		while(currModuleIndex < modules.length)
		{
			HomeLetter homeLetter = null;
			for(; currModuleIndex < modules.length; currModuleIndex++)
			{
				if(homeLetter == null || modules[currModuleIndex].getHomeLetter() == homeLetter)
				{
					homeLetter = modules[currModuleIndex].getHomeLetter();
					serialController.address(modules[currModuleIndex].getHomeLetter(), modules[currModuleIndex].getDeviceNumber());
				}
				else break;
			}
			serialController.perform(homeLetter, dimSteps, function);
		}
	}

}
