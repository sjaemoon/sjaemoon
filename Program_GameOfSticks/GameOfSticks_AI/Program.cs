// Brian Moon

// This program plays the game of "Game of Sticks"
// Certain number of sticks is chosen by the user to begin with.
// The user or the AI takes a certain number of sticks within the limit.
// The player to be forced to choose the last stick loses.
// The game can be played:
// Human VS Human
// Human VS AI
// AI VS AI

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameOfSticks_AI
{
	class Program
	{
		// These constants make the program adjustable to different settings.
		const int MinSticks = 10;
		const int MaxSticks = 100;
		const int PossibleToChoose = 3;
		const int TotalRound = 500;
		public static Random rand = new Random();

		static void Main(string[] args)
		{
			int option = Intro();
			int sticks = NumberOfSticks(option);
			// Creates two AI's that are needed for vs. AI games
			AI ai = new AI(sticks, PossibleToChoose);
			AI aiTrainer = new AI(sticks, PossibleToChoose);
			if (option == 1)
			{
				HumanVsHuman(sticks);
			}
            // Drawing Panel compatible gameplay
			if (option == 2)
			{
                DrawPanel dp = new DrawPanel(sticks, PossibleToChoose, "AI");
				do
				{
					int winner = HumanVsAi(ai, sticks);
					TrainingAI(ai, winner, dp, option);
				} 
				while (GetRange("Play again (1 = yes, 0 = no)", 0, 1) == 1);			
			}
            // Drawing Panel compatible gameplay
			if (option == 3)
			{
                DrawPanel dp1 = new DrawPanel(sticks, PossibleToChoose, "AI 1");
                DrawPanel dp2 = new DrawPanel(sticks, PossibleToChoose, "AI 2");
				ai = AiVsAi(ai, aiTrainer, dp1, dp2, sticks);
                Console.WriteLine("\nThe AI has finished its training. Let's Begin.");
				do
				{
					int winner = HumanVsAi(ai, sticks);
					TrainingAI(ai, winner, null, option);
				} 
				while (GetRange("Play again (1 = yes, 0 = no)", 0, 1) == 1);	
			}
			ExitGame();
		}

		/// <summary>
		/// Introduces the user to the game and allows 
		/// the user to choose the game mode.
        /// The game has three modes: Human vs. Human, Human vs. AI, Human vs. Trained AI.
		/// </summary>
        /// <returns>what mode the user wants to play</returns>
		static int Intro()
		{
			Console.WriteLine("Welcome to the Game of Sticks!\n");
            Console.WriteLine(
                              "\tDescription: This is a turn-based 2-player game.\n" +
                              "\tThe user chooses the number of sticks to start with.\n" +
                              "\tPlayer-1 and Player-2 alternates taking a number of sticks of their choice between 1 and 3.\n" +
                              "\tThe player that is forced to choose the last stick loses."
                              );
            Console.WriteLine("\nGame Modes:");
			Console.WriteLine("(1) Play against a friend");
			Console.WriteLine("(2) Play against the computer");
			Console.WriteLine("(3) Play against the trained computer");
			int option = GetRange("Which option do you take", 1 , 3);
            Console.WriteLine();
			return option;
		}

		/// <summary>
		/// Allows the user to pick how many sticks he/she wants to play with
		/// </summary>
		/// <returns>the number of sticks to play with</returns>
		static int NumberOfSticks(int option)
		{
            if (option != 1)
            {
                Console.Write("***If you want to see the AI train on DrawPanel, start with sticks");
                Console.WriteLine(" less than or equal to 50 for optimal experience***");
            }
            int sticks = GetRange("How many sticks are there on the table initially", MinSticks, MaxSticks);
            return sticks;
		}

		/// <summary>
		/// Asks the user how much sticks he/she wants to take.
		/// Processes the number of sticks the user chooses, 
		/// but prohibits the user from taking sticks that are outside of the game limit.
		/// </summary>
		/// <param name="sticks"></param>
		/// <returns>number of sticks the user takes</returns>
		static int StickOption(int sticks)
		{
			int taken = 0;
			if (sticks < PossibleToChoose)
				taken = GetRange("How many sticks do you take", 1, sticks);
			else
				taken = GetRange("How many sticks do you take", 1, PossibleToChoose);
			return taken;
		}

		/// <summary>
		/// Corrects the grammar between singular and plural 
		/// depending on the number of sticks left.
		/// </summary>
		/// <param name="num">the number of sticks</param>
		/// <returns>correct grammar based on the number of sticks</returns>
		static string StickPhrase(int num)
		{
			if (num > 1)
				return "are " + num + " sticks";
			else
				return "is " + num + " stick";
		}

		/// <summary>
		/// Has the aspect of the Human vs. Human gameplay:
		/// 2-player game with each taking turns to choose the number of sticks.
		/// </summary>
		/// <param name="sticks">the number of sticks the user starts with</param>
		static void HumanVsHuman(int sticks)
		{
			int player = 0;
			while (sticks >= 1)
			{
				Console.WriteLine("\nThere {0} on the board.", StickPhrase(sticks));
				Console.Write("Player {0}: ", player % 2 + 1);
				int taken = StickOption(sticks);
				// the remaining sticks algorithm:
				sticks -= taken;
				player++;
			}
			// player contains the winning player
			Console.WriteLine("\nPlayer {0}, you win.", player % 2 + 1);
		}

		/// <summary>
		/// Has the aspect of the Human vs. AI gameplay:
		/// user and the AI takes turns choosing the number of sticks until game is over.
		/// </summary>
		/// <param name="ai">untrained AI is passed to play against</param>
		/// <param name="sticks">the number of sticks the user starts with</param>
		/// <returns>the turn indicating the winning player</returns>
		static int HumanVsAi(AI ai, int sticks)
		{
			int turn = 1;
			while(sticks >= 1)
			{
				Console.WriteLine("\nThere {0} on the board.", StickPhrase(sticks));
				if (turn % 2 == 1)
					sticks -= StickOption(sticks);
				else
				{
					int takenByAi = ai.Chosen(sticks);
					Console.WriteLine("AI selects {0}", takenByAi);
					sticks -= takenByAi;
				}
				turn++;
			}
			return turn;
		}

		/// <summary>
		/// Calls the methods in AI class that either 
		/// reinforces or subtracts based on the winner.
		/// </summary>
		/// <param name="ai">the same AI the user played with</param>
		/// <param name="winner">human or AI that won the previous game</param>
        /// <param name="dp1">a drawpanel to show AI's learning progress</param>
		static void TrainingAI(AI ai, int winner, DrawPanel dp, int option)
		{
			if (winner % 2 == 1)
			{
				Console.WriteLine("Player, you win.\n");
				ai.AiLost();
			}
			else
			{
				Console.WriteLine("AI wins.\n");
				ai.AiWon();
			}
            // Only update the Drawing Panel if the user is playing Human Vs. AI
            if (option == 2)
            {
                int[,] cups = ai.BrainDump();
                Console.WriteLine();
                dp.UpdateAI(cups);
            }
		}

		/// <summary>
		/// Has the aspect of the AI vs. AI gameplay:
		/// two AIs battle thousands of times, 
		/// reinforcing and subtracting to make each other well trained.
		/// </summary>
		/// <param name="ai">the untrained AI that will play with another AI to train</param>
		/// <param name="aiTrainer">another untrained AI that will play with the other AI to train</param>
        /// <param name="dp1">a drawpanel to show AI's learning progress</param>
        /// <param name="dp2">a drawpanel to show second AI's learning progress</param>
		/// <param name="sticksInGame">the number of sticks the user starts with</param>
		/// <returns>a better trained AI that will play the user</returns>
		static AI AiVsAi(AI ai, AI aiTrainer, DrawPanel dp1, DrawPanel dp2, int sticksInGame)
		{
			int winAI = 0;
			int winAITrainer = 0;
			int turn = rand.Next(2);
			for (int round = 0; round < TotalRound; round++)
			{
				// resets the starting number of sticks each game
				int sticks = sticksInGame;
				while (sticks >= 1)
				{
					AI selectedAI;
					if (turn % 2 == 1)
						selectedAI = ai;
					else
						selectedAI = aiTrainer;
					sticks -= selectedAI.Chosen(sticks);
					turn++;
				}
				// Reinforces and subtracts the appropriate AI
				if (turn % 2 == 1)
				{
					ai.AiWon();
					aiTrainer.AiLost();
					winAI++;
				}
				else
				{
					ai.AiLost();
					aiTrainer.AiWon();
					winAITrainer++;
				}
				// For bugging purposes
				Console.Clear();
				int[,]aiCups = ai.BrainDump();
				Console.WriteLine();
				int[,]aiTrainerCups = aiTrainer.BrainDump();
                dp1.UpdateAI(aiCups);
                dp2.UpdateAI(aiTrainerCups);
			}
			// smarter AI is chosen
            if (winAI < winAITrainer)
            {
                dp1.EliminateLoser("AI 1");
                return aiTrainer;              
            }
            else
            {
                dp2.EliminateLoser("AI 2");
                return ai;
            }
		}

		/// <summary>
		/// Receives the question and see if the user's response is valid.
		/// Denies if the user is trying to enter a string or too big of a # in the response
		/// </summary>
		/// <param name="question">question that is being asked</param>
		/// <returns>valid response</returns>
		static int GetInt(string question)
		{
			while (true)
			{
				try
				{
					Console.Write(question);
					int response = int.Parse(Console.ReadLine());
					return response;
				}
				catch (FormatException)
				{
					Console.WriteLine("You must enter a valid integer");
				}
				catch (OverflowException)
				{
					Console.WriteLine("That number is either too large or " +
						"too small for my tiny computational brain");
				}
			}
		}

		/// <summary>
		/// Sets the range of the appropriate answers
		/// If user doesn't choose a valid answer, 
		/// politely asks to pick a # in the appropriate range.
		/// </summary>
		/// <param name="question">question that is being asked</param>
		/// <param name="min">minimum of valid range</param>
		/// <param name="max">maximum of valid range</param>
		/// <returns>valid response</returns>
		static int GetRange(string question, int min, int max)
		{
			question += string.Format(" ({0}-{1})? ", min, max);
			int choice = GetInt(question);
			while (choice < min || choice > max)
			{
				Console.WriteLine("\nPlease enter a number between {0} and {1}", min, max);
				choice = GetInt(question);
			}
			return choice;
		}

		/// <summary>
		/// Exits the game with a universal oxymoron.
		/// </summary>
		static void ExitGame()
		{
			Console.WriteLine("\nPress 'Enter' to Exit...");
			Console.ReadLine();
		}
	}
}
