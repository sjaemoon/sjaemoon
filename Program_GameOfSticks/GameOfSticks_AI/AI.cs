// The AI object contains a two dimensional array that stores
// the number of cups and the contents or chips within each cup
// and decides how many sticks AI takes and trains the AI based on the result.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameOfSticks_AI
{
	class AI
	{
		private int[,] cups;
		private int[] reinforceCups;
        private int sticks;
        private int possibleToChoose;

		/// <summary>
		/// Constructs an AI object that is based around a two-dimensional array.
        /// The array's columns represent the sticks remaining (also known as cup).
        /// The array's rows represent how many sticks you can choose
        /// given the sticks remaining (also known as chips).
		/// </summary>
		/// <param name="sticks">Number of sticks chosen by the user to start with</param>
		/// <param name="possibleToChoose">Number of sticks that can be taken by the AI</param>
		public AI(int sticks, int possibleToChoose)
		{
            this.sticks = sticks;
            this.possibleToChoose = possibleToChoose;
			this.cups = new int[this.sticks, this.possibleToChoose];
			this.reinforceCups = new int[this.sticks];
            //
			for (int cupNumber = 0; cupNumber < this.sticks; cupNumber++)
			{
                // a separate array keeps track of what the AI chose,
                // in order to reinforce or subtract (initially empty)
				this.reinforceCups[cupNumber] = 0;
                // the two-dimensional array is filled with chips (initially just 1)
				for (int chipNumber = 0; chipNumber < this.possibleToChoose; chipNumber++)
				{
					this.cups[cupNumber, chipNumber] = 1;
				}
			}
            // When there are less sticks than the number of sticks AI can take
            // the max amount of sticks AI can take is adjusted to fit the sticks left.
            for (int cupNumber = 0; cupNumber < this.possibleToChoose; cupNumber++)
            {
                for (int noChips = cupNumber + 1; noChips < this.possibleToChoose; noChips++)
                {
                    this.cups[cupNumber, noChips] = 0;
                }
            }
		}

        /// <summary>
        /// Number of chips that particular cup can choose from is added.
        /// </summary>
        /// <param name="remainingSticks">sticks remaining after some turn</param>
        /// <returns>the total number of chips in the cup</returns>
        private int ChipsInCup(int remainingSticks)
        {
            int chips = 0;
            for (int i = 0; i < this.cups.GetLength(1); i++)
            {
                chips += this.cups[remainingSticks - 1, i];
            }
            return chips;
        }

		/// <summary>
		/// Picks a random number less than or equal to the total number of content,
        /// and the range which the random number falls on decides how many sticks the AI takes.
		/// </summary>
		/// <param name="remainingSticks">sticks remaining after some turn</param>
		/// <returns>number of sticks AI takes</returns>
		public int Chosen(int remainingSticks)
		{ 
			int chipSum = ChipsInCup(remainingSticks);
			int chosen = Program.rand.Next(chipSum);
            // The upper bound is the range that is determined by the number of chips.
            // i.e. 1, 2, 3, 4, 5, 6
            //      (1-3) - take 1 stick
            //      (4) - take 2 sticks
            //      (5-6) take 3 sticks
            // If the random number is 3, the AI takes 1 stick.
			int upperBound = this.cups[remainingSticks - 1, 0];
            // Continue to search until the random number falls in a range 
            // associated with the number of sticks to take
			for (int i = 0; i < this.possibleToChoose; i++)
			{
				if (chosen < upperBound)
				{			
					this.reinforceCups[remainingSticks - 1] = i + 1;
					return i + 1;
				}
				upperBound += this.cups[remainingSticks - 1, i + 1];
			}			
			this.reinforceCups[remainingSticks - 1] = this.possibleToChoose;
            // didn't return above, so must take the max number of sticks
            return this.possibleToChoose;
		}

		/// <summary>
		/// Reinforces the chips that caused the win.
		/// </summary>
		public void AiWon()
		{
            int i = 0;
            while(i < this.sticks)
			{
                // checks in the reinforce cup to see which slot to reinforce
				if (this.reinforceCups[i] != 0)
				{
					this.cups[i, reinforceCups[i] - 1]++;
				}
                i++;
			}
            // Reinforce cups reset for the next game.
			for (int cupNumber = 0; cupNumber < this.sticks; cupNumber++)
			{
				this.reinforceCups[cupNumber] = 0;
			}
		}

		/// <summary>
		/// Subtracts the chips that caused the loss.
		/// </summary>
		public void AiLost()
		{
            int i = 0;
            while(i < this.sticks)
            {
                // checks in the reinforce cup to see which slot to subtract
                // If there is only one chip of a kind, it does not eliminate the choice completely.
				if (this.reinforceCups[i] != 0 && this.cups[i, reinforceCups[i] - 1] != 1)
				{
					this.cups[i, reinforceCups[i] - 1]--;
				}
                i++;
			}
            // Reinforce cups reset for the next game.
			for (int cupNumber = 0; cupNumber < this.sticks; cupNumber++)
			{
				this.reinforceCups[cupNumber] = 0;
			}
		}

        /// <summary>
        /// For debug purposes; checks after each Ai Vs. Ai if the reinforcement was correct.
        /// </summary>
        /// <returns>the filled cup</returns>
        public int[,] BrainDump()
        {
            for (int cupNumber = 0; cupNumber < this.sticks; cupNumber++)
            {
                for (int chipNumber = 0; chipNumber < this.possibleToChoose; chipNumber++)
                {
                    Console.Write("{0} ", this.cups[cupNumber, chipNumber]);
                }
                Console.Write("\\ ");
            }
            Console.WriteLine("\nPlease wait until the AI finishes training...");
            return cups;
        }
	}
}
