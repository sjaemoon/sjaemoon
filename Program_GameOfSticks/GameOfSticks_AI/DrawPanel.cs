// The DrawPanel object recreates the braindump method in a visual form.
// The object receives all the necessary info 
// to create a bar graph of how much chips are in each cup.
// This object also takes care of any graphics that are required
// (i.e. margins, text, shapes, and etc).

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using DrawPanelLibrary;

namespace GameOfSticks_AI
{
    class DrawPanel
    {
        private int sticks;
        private int chips;
        private string name;
        // Since the Draw Panel uses ints, smallest value has to be 
        // initialized and then multiplied to, since division rounds
        private int subMargin = 8; // <---- optimal for 50 sticks or less, can be adjusted        
        private int margin;
        private int width;
        private int height = 300;
        private int legendHeight = 50;
        private Graphics g;
        private DrawingPanel p;

        /// <summary>
        /// Constructs a DrawPanel object that creates a window,
        /// which shows the AI's training progress on a graph.
        /// </summary>
        /// <param name="sticks">number of sticks the user choose</param>
        /// <param name="chips">number of sticks the user can choose</param>
        /// <param name="name">Name of the AI</param>
        public DrawPanel(int sticks, int chips, string name)
        {
            this.sticks = sticks;
            this.chips = chips;
            this.name = name;
            this.margin = this.subMargin * this.chips;
            this.width = this.margin * this.sticks;
            // Makes a canvas with set width and height based on calculations above
            this.p = new DrawingPanel(this.width, this.height);
            this.g = p.GetGraphics();
            FixedGraphics();
            Labels();
        }

        /// <summary>
        /// Outputs the layout of the panel, including spaces for legends and borders.
        /// </summary>
        private void FixedGraphics()
        {
            this.g.FillRectangle
                (Brushes.LightGray, 0, this.height - this.legendHeight, 
                this.width, this.legendHeight);
            this.g.DrawLine
                (Pens.Black, 0, this.height - this.legendHeight, 
                this.width, this.height - this.legendHeight);
            this.g.DrawLine
                (Pens.Black, 0, this.height - 1, 
                this.width, this.height - 1);
            // Draws a line to separate between each cup
            for (int j = this.margin; j < this.width; j += this.margin)
            {
                this.g.DrawLine
                    (Pens.Black, j, 0, j, this.height - this.legendHeight);
            }
        }

        /// <summary>
        /// Outputs all the labels, including the cup and chip numbers.
        /// </summary>
        private void Labels()
        {
            // Sets the font and font size to be used
            using (Font f = new Font("Helvetica", 9))
            {
                // Since the text is not centered, the pixel needs to be moved 5 to the left
                int pixel = 5;
                // Writes the "title" of the graph
                this.g.DrawString
                    (this.name, f, 
                    Brushes.Black, 
                    this.width / 2 - pixel, 
                    this.height - this.legendHeight / 2 - 10);
                // Labels the number of sticks along the x-axis
                for (int j = this.margin; j <= this.width; j += this.margin)
                {
                    // Calculation for creating a number of sticks in a sequence
                    int sticksLeft = j / this.margin;
                    this.g.DrawString
                        (sticksLeft.ToString(), f, 
                        Brushes.Black, 
                        j - this.margin / 2 - pixel, 
                        this.height - this.legendHeight / 2 + pixel);
                    // Rewrites 1, 2, and 3, which represents the number of chips in each cup
                    for (int k = 0; k < this.sticks * this.chips; k++)
                    {
                        // Calculation for repeating the numbers 1, 2, and 3
                        int chipNumber = k % this.chips + 1; 
                        this.g.DrawString
                            (chipNumber.ToString(), f, 
                            Brushes.Black, 
                            k * this.subMargin + subMargin / 2 - pixel, 
                            this.height - this.legendHeight);
                    }
                }
            }
        }

        /// <summary>
        /// Goes through each cup in the array and updates if any contennts were added or subtracted
        /// and shows the result on the bar graph after each game.
        /// </summary>
        /// <param name="cups">receives information about all the contents in the each cup</param>
        public void UpdateAI(int[,] cups)
        {
            int interval = 0;
            // Similar code as brain dump
            for (int cupNumber = 0; cupNumber < this.sticks; cupNumber++)
            {
                for (int chipNumber = 0; chipNumber < this.chips; chipNumber++)
                {
                    this.g.FillRectangle
                        (Brushes.DarkBlue, 
                        interval,
                        this.height - this.legendHeight - cups[cupNumber, chipNumber],
                        this.subMargin,
                        cups[cupNumber, chipNumber]);
                    interval += this.subMargin;
                }
            }
            this.p.RefreshDisplay();
        }

        /// <summary>
        /// Allows the user to clearly know, which AI lost by pausing the program with a window.
        /// </summary>
        /// <param name="s">receives the name of the loser AI</param>
        public void EliminateLoser(string s)
        {
            using (Font f = new Font("Helvetica", 20))
            {
                this.g.DrawString("Loser", f, Brushes.Black, 
                    this.width / 2 - 2 * f.Size, this.height / 2);
            }
            p.RefreshDisplay();
            // Makes a window pop up
            DrawingPanel.Pause(s + " lost. Press OK to challenge the winner.");
        }
    }
}
