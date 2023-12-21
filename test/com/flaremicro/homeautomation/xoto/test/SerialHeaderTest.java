package com.flaremicro.homeautomation.xoto.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flaremicro.homeautomation.xoto.controller.SerialHeader;

public class SerialHeaderTest {

	@Test
	public void testStandardFunctionHeaderWithDim16() {
		SerialHeader header = new SerialHeader(16, SerialHeader.TYPE_FUNCTION, SerialHeader.TRANSMISSION_STANDARD);
		assertEquals(0x86, header.binaryHeader & 0xFF);
		assertEquals(16, header.getDimSteps());
		assertEquals(SerialHeader.TYPE_FUNCTION, header.getType());
		assertEquals(SerialHeader.TRANSMISSION_STANDARD, header.getTransmission());
	}
	
	@Test
	public void testExtendedFunctionHeaderWithDim16() {
		SerialHeader header = new SerialHeader(16, SerialHeader.TYPE_FUNCTION, SerialHeader.TRANSMISSION_EXTENDED);
		assertEquals(0x87, header.binaryHeader & 0xFF);
		assertEquals(16, header.getDimSteps());
		assertEquals(SerialHeader.TYPE_FUNCTION, header.getType());
		assertEquals(SerialHeader.TRANSMISSION_EXTENDED, header.getTransmission());
	}
	
	@Test
	public void testStandardAddressHeaderWithDim0() {
		SerialHeader header = new SerialHeader(0, SerialHeader.TYPE_ADDRESS, SerialHeader.TRANSMISSION_STANDARD);
		assertEquals(0x04, header.binaryHeader & 0xFF);
		assertEquals(0, header.getDimSteps());
		assertEquals(SerialHeader.TYPE_ADDRESS, header.getType());
		assertEquals(SerialHeader.TRANSMISSION_STANDARD, header.getTransmission());
	}
	
	@Test
	public void testExtendedAddressHeaderWithDim0() {
		SerialHeader header = new SerialHeader(0, SerialHeader.TYPE_ADDRESS, SerialHeader.TRANSMISSION_EXTENDED);
		assertEquals(0x05, header.binaryHeader & 0xFF);
		assertEquals(0, header.getDimSteps());
		assertEquals(SerialHeader.TYPE_ADDRESS, header.getType());
		assertEquals(SerialHeader.TRANSMISSION_EXTENDED, header.getTransmission());
	}
	
	@Test
	public void testStandardFunctionHeaderWithDim32() {
		SerialHeader header = new SerialHeader(32, SerialHeader.TYPE_FUNCTION, SerialHeader.TRANSMISSION_STANDARD);
		assertEquals(0xB6, header.binaryHeader & 0xFF);
		assertEquals(22, header.getDimSteps());
		assertEquals(SerialHeader.TYPE_FUNCTION, header.getType());
		assertEquals(SerialHeader.TRANSMISSION_STANDARD, header.getTransmission());
	}
}
