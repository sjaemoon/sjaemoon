/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program compares the random LFSR values with the 
	switch values to determine the move for the computer.
*/

// Returns move if the switch value is greater than the random value.
module computer (reset, random, SW, move);
	input logic reset;
	input logic [9:0] random, SW;
	output logic move;
	
	assign move = (SW > random) & ~reset;
endmodule

// Tests if the program compares the switch and the random value properly.
module computer_testbench();
	logic reset;
	logic [9:0] random, SW;
	logic move;
	
	computer dut (reset, random, SW, move);
	
	initial begin
		reset = 1; random = 9'd0; SW = 9'd0; #10;
		reset = 0; random = 9'b001110000; SW = 9'b000000010; #10;
													 SW = 9'b010000000; #10;
																			  #10;
																			  #10;
													 SW = 9'b001110000; #10;		
	end
endmodule