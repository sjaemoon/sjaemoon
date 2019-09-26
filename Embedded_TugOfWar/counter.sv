/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program counts the number of wins for a given player and returns
	the 3-bit equivalent of the number of wins. Also returns if the game is over
	(a player reaching 7-wins) and is in need of a reset.
*/

// Returns the 3-bit equivalent of the number of wins,
// and returns game over if the number of wins is 7.
module counter (clk, reset, win, gameOver, score);
	input logic clk, reset, win;
	output gameOver;
	output logic [2:0] score;
	enum {w0, w1, w2, w3, w4, w5, w6, w7} ps, ns;
	
	always_comb begin
		case (ps)
			w0: if (win) ns = w1;
				 else ns = w0;
			w1: if (win) ns = w2;
				 else ns = w1;
			w2: if (win) ns = w3;
				 else ns = w2;
			w3: if (win) ns = w4;
				 else ns = w3;
			w4: if (win) ns = w5;
				 else ns = w4;
			w5: if (win) ns = w6;
				 else ns = w5;
			w6: if (win) ns = w7;
				 else ns = w6;
			w7: ns = w7;
		endcase
	end
	
	assign gameOver = (ps == w7);
	assign score[2] = (ps == w4 | ps == w5 | ps == w6 | ps == w7);
	assign score[1] = (ps == w2 | ps == w3 | ps == w6 | ps == w7);
	assign score[0] = (ps == w1 | ps == w3 | ps == w5 | ps == w7);
	
	always_ff @(posedge clk) begin
		if (reset)
			ps <= w0;
		else
			ps <= ns;
	end
endmodule

// Tests if the correct 3-bit value and game over value 
// is passed based on the number of wins.
module counter_testbench();
	logic clk, reset, win;
	logic gameOver;
	logic [2:0] score;
	
	counter dut (clk, reset, win, gameOver, score);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		clk <= 0;
		forever#(CLOCK_PERIOD/2)clk <= ~clk;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		reset <= 1; win <= 0; @(posedge clk);
		reset <= 0;  			 @(posedge clk);
						win <= 1; @(posedge clk);
									 @(posedge clk);						
									 @(posedge clk);
									 @(posedge clk);	
									 @(posedge clk);
									 @(posedge clk);										 
									 @(posedge clk);
									 @(posedge clk);
		reset <= 1; 			 @(posedge clk);	
									 @(posedge clk);
		$stop; // End the simulation.
	end	
endmodule

