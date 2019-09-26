/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program returns a "random" 10-bit number based on the LFSR.
*/

// Returns a random 10-bit number.
module LFSR (clk, reset, out);
	input logic clk, reset;
	output logic [9:0] out;
	logic feedback;
	
	assign feedback = ~(out[9]^out[6]);
	
	always_ff @(posedge clk) begin
		if (reset) 
			out <= 9'b0;
		else 
			out <= {out[8:0], feedback};
	end
endmodule

// Tests the shifting of 10-bit numbers
module LFSR_testbench();
	logic clk, reset;
	logic [9:0] out;
		
	LFSR dut (clk, reset, out);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		clk <= 0;
		forever#(CLOCK_PERIOD/2)clk <= ~clk;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		reset <= 1; @(posedge clk)
		reset <= 0; out <= 9'b1111111111; @(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);
		@(posedge clk);		
		$stop; // End the simulation.
	end
endmodule
	
	