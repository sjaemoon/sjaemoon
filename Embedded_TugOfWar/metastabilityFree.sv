/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program deals with the metastability that happens
	when the input to a D-flipflop (DFF) changes at about
	the same time as the clock edge occurs.
*/
	
// Takes the key as input and sends it to a 
// pair of DFFs and outputs the result.
module metastabilityFree (clk, reset, in, out);
	input logic clk, reset, in;
	output logic out;
	logic temp;
	always_ff @(posedge clk) begin
		if (reset) begin
			out <= 0;
			temp <= 0;
		end
		else begin
			temp <= in;
			out <= temp;
		end
	end
endmodule

// Tests the output corresponding to different series 
// of inputs going through a pair of DFFs.
module metastabilityFree_testbench();
	logic clk, reset, in;
	logic out;
	
	metastabilityFree dut (clk, reset, in, out);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		clk <= 0;
		forever#(CLOCK_PERIOD/2)clk <= ~clk;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		reset <= 1; in <= 0; @(posedge clk);
		reset <= 0; 			@(posedge clk);
						in <= 1; @(posedge clk);
						in <= 0; @(posedge clk);						
						in <= 1; @(posedge clk);
									@(posedge clk);	
						in <= 0;	@(posedge clk);
									@(posedge clk);
									@(posedge clk);					
		$stop; // End the simulation.
	end	
endmodule



