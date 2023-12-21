package com.flaremicro.homeautomation.xoto.operation;

import java.io.IOException;

import com.flaremicro.homeautomation.xoto.controller.SerialController;

public abstract class Operation {
	public abstract void performOperation(SerialController serialController) throws IOException, InterruptedException;
}
