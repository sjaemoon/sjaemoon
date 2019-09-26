/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program turns ON the normal light (non-center lights) if:
		- the left button is pressed when the right neighboring light is ON
		- the right button is pressed when the left neighboring light is ON
		- the both users do not press anything or press left/right simultaneously
		  when the normal light is already ON
	otherwise OFF.
	The normal light is OFF at the beginning and when resetting.
*/

// Outputs that whether the normal light is on/off 
// based on what is happening to the user inputs, 
// adjacent lights, and the reset.
module normalLight (clk, reset, resetRound, L, R, NL, NR, lightOn);
	input logic clk, reset, resetRound, L, R, NL, NR;
	output logic lightOn;
	enum {off, on} ps, ns;
	
	always_comb begin
		case (ps)
			off: if((L & ~R & NR) | (R & ~L & NL)) ns = on;
				else ns = off;
			on: if(R ^ L) ns = off;
				else ns = on;
		endcase
	end
	
	assign lightOn = (ps == on);
	
	always_ff @(posedge clk) begin
		if (reset | resetRound)
			ps <= off;
		else
			ps <= ns;
	end
endmodule

// Tests if the center light is ON/OFF as expected at the 
// given state of user inputs, adjacent lights, and reset.
module normalLight_testbench();
	logic clk, reset, resetRound, L, R, NL, NR;
	logic lightOn;
	
	normalLight dut (clk, reset, resetRound, L, R, NL, NR, lightOn);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		clk <= 0;
		forever#(CLOCK_PERIOD/2)clk <= ~clk;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		reset <= 1; resetRound <= 0; L <= 0; R <= 0; NL <= 0; NR <= 0; @(posedge clk); // off
		reset <= 0; 										    @(posedge clk); // stay off
						L <= 1;		 							 @(posedge clk); 
										    NL <= 1; 			 @(posedge clk);
										    NL <= 0;  		    @(posedge clk);
						L <= 0; 									 @(posedge clk);
								  R <= 1; 			 		    @(posedge clk); 
								 		    NL <= 1; 		    @(posedge clk); // on
										    NL <= 0;          @(posedge clk); // off
														 NR <= 1; @(posedge clk);
														 NR <= 0; @(posedge clk);
								  R <= 0; 			 		    @(posedge clk);
						L <= 1; 		  			    NR <= 1; @(posedge clk); // on	
						L <= 0; R <= 0;		   		    @(posedge clk); // stay on
						L <= 1; R <= 1;		   		    @(posedge clk); 				
														 NR <= 0; @(posedge clk); // off	
		resetRound <= 1;										 @(posedge clk); // stays off, works just like normal reset
						L <= 1; 		  			 	 NR <= 1; @(posedge clk); 
		$stop; // End the simulation.
	end
endmodule