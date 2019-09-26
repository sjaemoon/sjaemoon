/*
 	Seungjae Moon
	bmoon9
	06/04/19
	EE271
	Final Project: Frogger
	This program keeps track of each element on the red LED array 
	(representing cars) based on the adjacent elements in the same row.
*/

// Turns the car light on if the the element 
// that is opposite the preset direction for that row is ON.
module cars (clk, reset, needReset, resetRound, pattern, movingRight, NL, NR, lightOn);
	input logic clk, reset, needReset, resetRound, pattern, movingRight, NL, NR;
	output logic lightOn;
	
	logic ps;
	
	always_ff @(posedge clk) begin
		if(reset | resetRound)
			ps <= pattern;
		else if (~needReset)
			ps <= ((movingRight & NL) | (~movingRight & NR));
		else
			ps <= ps;
	end
	
	assign lightOn = ps;

endmodule

// Tests if the light is ON/OFF as expected at the 
// given state of adjacent lights, preset direction, and reset.
module cars_testbench();
	logic clk, reset, needReset, resetRound, pattern, movingRight, NL, NR;
	logic lightOn;
	
	cars dut (clk, reset, needReset, resetRound, pattern, movingRight, NL, NR, lightOn);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		clk <= 0;
		forever#(CLOCK_PERIOD/2)clk <= ~clk;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		reset <= 1; needReset <= 0; resetRound <= 0; pattern <= 0; movingRight <= 0; NL <= 0; NR <= 0; @(posedge clk); 
		reset <= 0; @(posedge clk); // starts on/off
		movingRight <= 1; @(posedge clk); // turns off
		NL <= 1; @(posedge clk); // turns on
		NL <= 0; @(posedge clk); // turns off
		movingRight <= 0; @(posedge clk); // stays off
		NR <= 1; @(posedge clk); // turns on
		NR <= 0; @(posedge clk); // turns off
		@(posedge clk); // stays off
		needReset <= 1; @(posedge clk); // does not move anymore
		@(posedge clk);
		needReset <= 0; pattern <= 1; @(posedge clk);
		reset <= 1; @(posedge clk); // reset now keeps the light on
		@(posedge clk);
		$stop; // End the simulation.
	end
endmodule