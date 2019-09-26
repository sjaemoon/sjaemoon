/*
 	Seungjae Moon
	bmoon9
	06/04/19
	EE271
	Final Project Frogger
	This program counts the number of wins for a given player and returns
	the 4-bit equivalent of the number of wins. Also returns if the game is over
	(a player reaching 9-wins) and is in need of a reset.
*/

// Returns the 4-bit equivalent of the number of wins,
// and returns game over if the number of wins is 9.
module scoreCalculator (clk, reset, clear, gameOver, score);
	input logic clk, reset, clear;
	output gameOver;
	output logic [3:0] score;
	enum {w0, w1, w2, w3, w4, w5, w6, w7, w8, w9} ps, ns;
	
	always_comb begin
		case (ps)
			w0: if (clear) ns = w1;
				 else ns = w0;
			w1: if (clear) ns = w2;
				 else ns = w1;
			w2: if (clear) ns = w3;
				 else ns = w2;
			w3: if (clear) ns = w4;
				 else ns = w3;
			w4: if (clear) ns = w5;
				 else ns = w4;
			w5: if (clear) ns = w6;
				 else ns = w5;
			w6: if (clear) ns = w7;
				 else ns = w6;
			w7: if (clear) ns = w8;
				 else ns = w7;
			w8: if (clear) ns = w9;
				 else ns = w8;
			w9: ns = w9;
		endcase
	end
	
	assign gameOver = (ps == w9);
	assign score[3] = (ps == w8 | ps == w9);
	assign score[2] = (ps == w4 | ps == w5 | ps == w6 | ps == w7);
	assign score[1] = (ps == w2 | ps == w3 | ps == w6 | ps == w7);
	assign score[0] = (ps == w1 | ps == w3 | ps == w5 | ps == w7 | ps == 9);
	
	always_ff @(posedge clk) begin
		if (reset)
			ps <= w0;
		else
			ps <= ns;
	end
endmodule

// Tests if the correct 4-bit value and game over value 
// is passed based on the number of wins.
module scoreCalculator_testbench();
	logic clk, reset, clear;
	logic gameOver;
	logic [2:0] score;
	
	scoreCalculator dut (clk, reset, clear, gameOver, score);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		clk <= 0;
		forever#(CLOCK_PERIOD/2)clk <= ~clk;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		reset <= 1; clear <= 0; @(posedge clk);
		reset <= 0;  			   @(posedge clk);
						clear <= 1; @(posedge clk);
									   @(posedge clk);						
									   @(posedge clk);
									   @(posedge clk);	
									   @(posedge clk);
									   @(posedge clk);										 
									   @(posedge clk);
									   @(posedge clk);
		reset <= 1; 			   @(posedge clk);	
									   @(posedge clk);
		$stop; // End the simulation.
	end	
endmodule

