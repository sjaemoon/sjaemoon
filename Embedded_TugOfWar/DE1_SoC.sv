/*
 	Seungjae Moon
	bmoon9
	05/20/19
	EE271
	Lab 5
	This program maps the tug-of-war game design to the FPGA.
	The game consists of human player vs. different difficulty computer player.
*/

// Connects the human inputs to the keys, reset and difficulty modes to the switches,
// and the corresponding game ouputs to the LEDs. The computer input is managed within the program.
module DE1_SoC (CLOCK_50, HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, KEY, LEDR, SW);
	input logic CLOCK_50; // 50MHz clock.
	output logic [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
	output logic [9:0] LEDR;
	input logic [3:0] KEY; // True when not pressed, False when pressed.
	input logic [9:0] SW;
	logic reset, resetRound, in0, in1, p1, p2, left, right, humanWin, compWin, humanWins, compWins;
	logic [9:0] random;
	logic [2:0] humanScore, compScore;
	
	// Generate clk off of CLOCK_50, whichClock picks rate.
	logic [31:0] clk;
	parameter whichClock = 15;
	clock_divider cdiv (CLOCK_50, clk);
	
	assign reset = SW[9];
	assign in0 = ~KEY[0];
	assign HEX1 = 7'b1111111; 
	assign HEX2 = 7'b1111111;
	assign HEX3 = 7'b1111111;
	assign HEX4 = 7'b1111111;
	
	assign needReset = (humanWins | compWins);
	LFSR ran (.clk(clk[whichClock]), .reset, .out(random));
	computer comp (.reset, .random, .SW({1'b0, SW[8:0]}), .move(in1));
	metastabilityFree mF1 (.clk(clk[whichClock]), .reset, .in(in0), .out(p1));
	metastabilityFree mF2 (.clk(clk[whichClock]), .reset, .in(in1), .out(p2));
	userInput uI1 (.clk(clk[whichClock]), .reset, .needReset, .pressed(p1), .move(right));
	userInput uI2 (.clk(clk[whichClock]), .reset, .needReset, .pressed(p2), .move(left));
	
	
	assign resetRound = (humanWin | compWin);
	normalLight nL1 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[2]), .NR(1'b0), .lightOn(LEDR[1])); //rightmost
	normalLight nL2 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[3]), .NR(LEDR[1]), .lightOn(LEDR[2]));
	normalLight nL3 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[4]), .NR(LEDR[2]), .lightOn(LEDR[3]));
	normalLight nL4 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[5]), .NR(LEDR[3]), .lightOn(LEDR[4]));
	centerLight cL (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[6]), .NR(LEDR[4]), .lightOn(LEDR[5])); //center
	normalLight nL6 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[7]), .NR(LEDR[5]), .lightOn(LEDR[6]));
	normalLight nL7 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[8]), .NR(LEDR[6]), .lightOn(LEDR[7]));
	normalLight nL8 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(LEDR[9]), .NR(LEDR[7]), .lightOn(LEDR[8]));
	normalLight nL9 (.clk(clk[whichClock]), .reset, .resetRound(resetRound), .L(left), .R(right), .NL(1'b0), .NR(LEDR[8]), .lightOn(LEDR[9])); // leftmost
	
	victory v1 (.left, .right, .winCond(LEDR[1]), .player(1'b0), .winner(humanWin));
	victory v2 (.left, .right, .winCond(LEDR[9]), .player(1'b1), .winner(compWin));
	counter c1 (.clk(clk[whichClock]), .reset, .win(humanWin), .gameOver(humanWins), .score(humanScore));
	counter c2 (.clk(clk[whichClock]), .reset, .win(compWin), .gameOver(compWins), .score(compScore));
	seg7 s1 (.bcd(humanScore), .leds(HEX0));
	seg7 s2 (.bcd(compScore), .leds(HEX5));

endmodule

// divided_clocks[0] = 25MHz, [1] = 12.5Mhz, ...
// [23] = 3Hz, [24] = 1.5Hz, [25] = 0.75Hz, ...
module clock_divider (clock, divided_clocks);
	input logic clock;
	output logic [31:0] divided_clocks;
	
	initial begin
		divided_clocks <= 0;
	end
	
	always_ff @(posedge clock) begin
		divided_clocks <= divided_clocks + 1;
	end
endmodule

// Tests the connection between the input/output 
// of the tug-of-war game and the hardware with 
// input combinations that represent a typical game.
module DE1_SoC_testbench();
	logic CLOCK_50;
	logic [6:0] KEY;
	logic [9:0] SW;
	logic HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, LEDR;
	
	DE1_SoC dut (CLOCK_50, HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, KEY, LEDR, SW);
	
	// Set up the clock.
	parameter CLOCK_PERIOD = 100;
	initial begin
		CLOCK_50 <= 0;
		forever#(CLOCK_PERIOD/2)CLOCK_50 <= ~CLOCK_50;
	end
	
	// Set up the inputs to the design. Each line is a clock cycle.
	initial begin
		SW[9] <= 1;	KEY[0] <= 1; SW[8:0] <= 10'd0; @(posedge CLOCK_50); // 
		SW[9] <= 0;	SW[8:0] <= 10'd511;		  	 	 @(posedge CLOCK_50); // unbeatable computer
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 	
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); // approx. beats the human
																 @(posedge CLOCK_50); // moves on to the next round immediately	
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 		
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50); 
																 @(posedge CLOCK_50);
																 @(posedge CLOCK_50);
						SW[8:0] <= 10'd1;      			 @(posedge CLOCK_50); // beatable computer, computer moves
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right
																 @(posedge CLOCK_50); // hold-human doesn't move, computer moves
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right
						KEY[0] <= 1; 				    	 @(posedge CLOCK_50); 
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right
						KEY[0] <= 1; 				    	 @(posedge CLOCK_50);	
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right
						KEY[0] <= 1; 				    	 @(posedge CLOCK_50);	
						KEY[0] <= 0; 				   	 @(posedge CLOCK_50); // move right
						KEY[0] <= 1; 				     	 @(posedge CLOCK_50);	
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right
						KEY[0] <= 1; 				    	 @(posedge CLOCK_50);	
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right, human wins
						KEY[0] <= 1; 				    	 @(posedge CLOCK_50); // moves on to next round immediately	
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); 
						KEY[0] <= 1; 				     	 @(posedge CLOCK_50);	
						KEY[0] <= 0; 				    	 @(posedge CLOCK_50); // move right						
						KEY[0] <= 1;						 @(posedge CLOCK_50);
		SW[9] <= 1;											 @(posedge CLOCK_50);

		$stop; // End the simulation.
	end
endmodule


