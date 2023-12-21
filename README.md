# XOTO Home Automation Suite
Xoto (Pronounced Zoto, for X-10 Automation) is an Automation suite for X-10 devices.
Currently supports the CM11A and CM10A (Also known as the IBM Home Director (not to beconfused with the 80's CP290)) interfaces, with more on the way

## THIS SOFTWARE IS NOT COMPLETE
It's like, very, very not complete, it doesn't even actually trigger anything yet. This will change soon, and this disclaimer will to!
The actual code to trigger stuff is *there*, but nothing actually uses it yet.

## Build Requirements
You require:
1. Java 6+
2. Eclise (Or your favorite IDE)
3. A copy of jSerialComm https://fazecast.github.io/jSerialComm/
4. Knowledge

## Module Support
These don't necessarly work yet (re: disclaimer), but have code for functioning

### Currently Supported
Controllers:
	- CM10A
		- IBM Home Director 2-Way Serial Interface Model 75H8381
	- CM11A
Modules:
	- Lamp Modules
	- Appliance Modules
	- RF Transmitter Modules

### Planned Support
Controllers:
	- CP290
	- CM17A
Modules:
	- Wall Modules

### Why is my module not listed?
I can only support what I own, or at the very least have access too. I do not own the CM15A module for instance.
I also have not listed everything I have recieved yet as I need to document everything.
