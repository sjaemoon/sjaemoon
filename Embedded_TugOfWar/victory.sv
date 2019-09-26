/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program returns the winner,
		- Player 1 (0): if the right button is pressed when the rightmost light is ON.
		- Player 2 (1): wins if the left button is pressed when the leftmost light is ON.
*/

// Returns the winner based on what is happening to the user inputs, 
// leftmost and rightmost lights, and the reset.
module victory (left, right, winCond, player, winner);
	input logic left, right, winCond, player;
	output logic winner;
	
	assign winner = (right & ~left & winCond & ~player) | (left & ~right & winCond & player);
endmodule

// Tests if the correct winner or no winner is chosen at the
// given state of user inputs, leftmost and rightmost lights, and reset.
module victory_testbench();
	logic left, right, winCond, player;
	logic winner;
	
	victory dut (left, right, winCond, player, winner);
	
	initial begin
	  	left = 0; right = 0; winCond = 0; player = 0; #10; // no winner
									winCond = 1;				 #10; // stays none
		left = 1;												 #10; 
		left = 0; right = 1; 								 #10; // P1 winner
													 player = 1; #10; // no winner
					 right = 0; winCond = 0; 			 	 #10; // stays none									  
		left = 1;							  					 #10; // 
									winCond = 1;				 #10; // P2 winner
	end
endmodule