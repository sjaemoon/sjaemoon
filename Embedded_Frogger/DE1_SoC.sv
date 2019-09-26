/*
 	Seungjae Moon
	bmoon9
	06/04/19
	EE271
	Final Project: Frogger
	This program maps the Frogger game design to the FPGA.
*/

// Connects the user inputs to the keys, reset button to a SWitch,
// and the corresponding ouputs to the LEDs.
module DE1_SoC (CLOCK_50, HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, KEY, LEDR, SW, GPIO_0);
	input logic CLOCK_50; // 50MHz clock.
	output logic [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
	output logic [9:0] LEDR;
	output logic [35:0] GPIO_0;
	input logic [3:0] KEY; // True when not pressed, False when pressed.
	input logic [9:0] SW;
	logic reset, completed, needReset, resetRound, survived, crashed;
	logic [3:0] score;

	// Generates clk off of CLOCK_50, whichClock picks rate.
	logic [31:0] clk;
	clock_divider cdiv (CLOCK_50, clk);
	
	assign reset = SW[9];
	
	// Preset car pattern on each row
	logic [7:0][7:0] patterns;
	assign patterns[7][7:0] = 8'b10001001;
	assign patterns[6][7:0] = 8'b01100110;
	assign patterns[5][7:0] = 8'b11110010;
	assign patterns[4][7:0] = 8'b00000000;
	assign patterns[3][7:0] = 8'b01001011;
	assign patterns[2][7:0] = 8'b01100010;
	assign patterns[1][7:0] = 8'b11000100;
	assign patterns[0][7:0] = 8'b00000000;
	// Preset car direction on each row
	logic [7:0] movingRight;
	assign movingRight[7] = 1'b0;
	assign movingRight[6] = 1'b1;
	assign movingRight[5] = 1'b1;
	assign movingRight[4] = 1'b0;
	assign movingRight[3] = 1'b0;
	assign movingRight[2] = 1'b1;
	assign movingRight[1] = 1'b0;
	assign movingRight[0] = 1'b0;
	
	// Manages the LED Matrix Driver.
	logic [7:0][7:0] red_array, green_array;
	led_matrix_driver matrix (.clk(clk[4]), .reset,
		.red_array, .green_array, 
		.red_driver(GPIO_0[27:20]), 
		.green_driver(GPIO_0[35:28]), 
		.row_sink(GPIO_0[19:12]));
	
	// Processes the user input.
	assign needReset = completed | crashed;
	logic [3:0] in, move;
	genvar g;
	generate
		for (g = 0; g < 4; g++) begin: user
			metastabilityFree mF (.clk(CLOCK_50), .reset, .in(~KEY[3-g]), .out(in[g]));
			userInput uI (.clk(CLOCK_50), .reset, .needReset, .pressed(in[g]), .move(move[g]));
			// move[0] = left, move[1] = up, move[2] = down, move[3] = right
		end
	endgenerate
	
	// Slows down the clock for the cars.
	logic [27:0] count;
	logic enable;
	always_ff @(posedge CLOCK_50) begin
		if (count == 75000000) begin
			count <= 0;
			enable <= 1;
		end
		else begin
			count = count + 1;
			enable <= 0;
		end
	end
	
	// Generates the cars.
	genvar i, j;
	generate
		for (i = 0; i < 8; i++) begin: r0
			for (j = 0; j < 8; j++) begin :c0
				// used MOD operator to wrap around the board on either side.
				cars blocked (.clk(enable), .reset, .needReset, .resetRound(survived), .pattern(patterns[i][j]), .movingRight(movingRight[i]), 
				.NL(red_array[i][(j+1)%8]), .NR(red_array[i][(j+7)%8]), .lightOn(red_array[i][j]));
			end
		end
	endgenerate
	
	// Generates the frogs.
	generate
		for (i = 0; i < 8; i++) begin: r1
			for (j = 0; j < 8; j++) begin :c1
				if (i == 0 && j == 3)
					startFrog start (.clk(CLOCK_50), .reset, .resetRound(survived), 
					.L(move[0]), .U(move[1]), .D(move[2]), .R(move[3]), 
					.NL(green_array[i][(j+1)%8]), .NU(green_array[(i+1)%8][j]), 
					.ND(1'b0), .NR(green_array[i][(j+7)%8]), .lightOn(green_array[i][j]));
					
				else if (i == 0 && j != 3)
					startRowFrog firstRow (.clk(CLOCK_50), .reset, .resetRound(survived), 
					.L(move[0]), .U(move[1]), .D(move[2]), .R(move[3]), 
					.NL(green_array[i][(j+1)%8]), .NU(green_array[(i+1)%8][j]), 
					.ND(1'b0), .NR(green_array[i][(j+7)%8]), .lightOn(green_array[i][j]));
					
				else if (i == 7)
					frog lastRow (.clk(CLOCK_50), .reset, .resetRound(survived), 
					.L(move[0]), .U(move[1]), .D(move[2]), .R(move[3]), 
					.NL(green_array[i][(j+1)%8]), .NU(1'b0), 
					.ND(green_array[(i+7)%8][j]), .NR(green_array[i][(j+7)%8]), .lightOn(green_array[i][j]));
					
				else
					frog row (.clk(CLOCK_50), .reset, .resetRound(survived), 
					.L(move[0]), .U(move[1]), .D(move[2]), .R(move[3]), 
					.NL(green_array[i][(j+1)%8]), .NU(green_array[(i+1)%8][j]), 
					.ND(green_array[(i+7)%8][j]), .NR(green_array[i][(j+7)%8]), .lightOn(green_array[i][j]));
			end
		end
	endgenerate

	// Generates the lose and win states.
	checkFrog cF (.frog(green_array), .cars(red_array), .U(move[1]), .crashed, .survived); 
	
	// Keeps track of the score and its display.
	scoreCalculator sC (.clk(CLOCK_50), .reset, .clear(survived), .gameOver(completed), .score(score));
	seg7 s (.bcd(score), .leds(HEX0));	
	assign HEX1 = 7'b1111111;
	always_comb begin
		if (crashed) begin
			HEX5 = 7'b1000111; // L
			HEX4 = 7'b1000000; // O
			HEX3 = 7'b0010010; // S
			HEX2 = 7'b0000110; // E
		end
		else if(completed) begin
			HEX5 = 7'b0000010; // G
			HEX4 = 7'b0000010; // G
			HEX3 = 7'b1111111; 
			HEX2 = 7'b1111111;	
		end	
		else begin
			HEX5 = 7'b1111111;
			HEX4 = 7'b1111111;
			HEX3 = 7'b1111111;
			HEX2 = 7'b1111111;
		end
	end
	
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


