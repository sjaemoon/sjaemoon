/*
 	Seungjae Moon
	bmoon9
	06/04/19
	EE271
	Final Project: Frogger
	This program keeps track of the entire array to check 
	for any collision or win state of the frog.
*/

// Checks for the frog and car position to output the value for collision.
// Checks for when the frog is on the top row, "up" is pressed, AND
// the frog did not crash simultaneously to output survived.
module checkFrog (frog, cars, U, crashed, survived);
	input logic U;
	input logic [7:0][7:0] frog, cars;
	output logic crashed, survived;
	
	logic [7:0][7:0] checkCrash;
	logic [7:0] checkSurvive, upExtend;
	
	always_comb begin
		if (U)
			upExtend = 8'b11111111; // extend to check with the 8-bit top row
		else
			upExtend = 8'b00000000;
	end
	assign checkCrash = frog & cars; // bit-wise AND on both 64-bits
	assign checkSurvive = frog[7][7:0] & upExtend;
	assign crashed = |checkCrash; // checks all 64-bits
	assign survived = |checkSurvive;

endmodule

// Checks if the correct state for the frog 
// is given based on the board condition.
module checkFrog_testbench();
	logic [7:0][7:0] frog, cars;
	logic U;
	logic crashed, survived;
	
	checkFrog dut (frog, cars, U, crashed, survived);
	
	initial begin
		frog[7][7:0] = 8'b00000000; cars[7][7:0] = 8'b11001001; 
		frog[1][7:0] = 8'b00000000; cars[1][7:0] = 8'b00000000; U = 0; #10;U = 0; #10; // nothing
		
		frog[7][7:0] = 8'b00000010; cars[7][7:0] = 8'b11001001; #10; // nothing
		frog[7][7:0] = 8'b00000010; cars[7][7:0] = 8'b11001001; U = 1; #10; // win
		frog[7][7:0] = 8'b00001000; cars[7][7:0] = 8'b11001001; U = 0; #10; // crash
		frog[7][7:0] = 8'b00001000; cars[7][7:0] = 8'b11001001; U = 1; #10; // crash, regardless of win condition
		
		frog[7][7:0] = 8'b00000000; cars[7][7:0] = 8'b00000000; 
		frog[1][7:0] = 8'b00100000; cars[1][7:0] = 8'b11000100; U = 0; #10; // other row, nothing
		frog[1][7:0] = 8'b10000000; cars[1][7:0] = 8'b11000100; U = 0; #10; // crash
	end
endmodule