package com.flaremicro.homeautomation.xoto.operation;

import com.flaremicro.homeautomation.xoto.enums.Function;
import com.flaremicro.homeautomation.xoto.module.X10Module;

public class UnsupportedFunctionException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedFunctionException(X10Module x10Module, Function function) {
		super("Module " + x10Module + " does not support function " + function);
	}


}
