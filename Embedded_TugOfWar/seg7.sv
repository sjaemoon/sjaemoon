/*
	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program takes the binary inputs and converts them
	to chosen item names as a 7-segment display.
*/

// Outputs the binary inputs' names as a 7-segment display.
module seg7 (bcd, leds);
	input logic [2:0] bcd;
	output logic [6:0] leds;
	
	always_comb begin 
		case (bcd)
			3'b000: leds = 7'b1000000;
			3'b001: leds = 7'b1111001;
			3'b010: leds = 7'b0100100;
			3'b011: leds = 7'b0110000;
			3'b100: leds = 7'b0011001;
			3'b101: leds = 7'b0010010;
			3'b110: leds = 7'b0000010;
			3'b111: leds = 7'b1111000;
			default: leds = 7'bX;
		endcase
	end
	
endmodule

// Tests the coversion of the binary inputs' names 
// to their matching HEX position.
module seg7_testbench;
	logic [2:0] bcd;
	logic [6:0] leds;	
	
	seg7 dut (bcd, leds);
	
	integer i;
	initial begin
		for (i=0; i<2**3; i++) begin
			{bcd} = i; #10;
		end
	end
	
endmodule
	