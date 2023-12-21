package com.flaremicro.homeautomation.xoto.operation;

import java.io.IOException;

import com.flaremicro.homeautomation.xoto.controller.SerialController;

/**
 * Abstract operation class, this is used to perform "operations" on X10 connected devices. 
 * SimpleOperation is currently the only class extending this.
 * @author Vulpovile
 *
 */
public abstract class Operation {
	
	/**
	 * The abstracted operation on a controller class.
	 * Each controller may have their own way of performing a task, so generally no direct I/O access should be used here.
	 * 
	 * If a function does not exist in the controller and you need it, rather than using the controller's I/O directly,
	 * write a function in the controller classes instead.
	 */
	public abstract void performOperation(SerialController serialController) throws IOException, InterruptedException;
}
