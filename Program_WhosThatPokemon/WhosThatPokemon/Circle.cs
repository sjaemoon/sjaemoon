using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WhosThatPokemon
{
    class Circle
    {
        private Point loc;
        private int s;
        private int newIndex;

        /// <summary>
        /// Constructs a circle that has the
        /// components of location and size.
        /// </summary>
        /// <param name="x">leftmost x-value of circle</param>
        /// <param name="y">upmost y-value of circle</param>
        /// <param name="s">size of circle</param>
        public Circle(int x, int y, int s)
        {
            this.loc.X = x;
            this.loc.Y = y;
            this.s = s;
        }

        /// <summary>
        /// Contains a rectangle that encompasses 
        /// the shape, with no room to spare.
        /// </summary>
        public Rectangle Margins
        {
            get { return new Rectangle(this.loc.X, this.loc.Y, this.s, this.s); }
        }

        /// <summary>
        /// Returns true if the given coordinates lie
        /// within the bounds of the circle.
        /// </summary>
        /// <param name="x">x-value of mouse location</param>
        /// <param name="y">y-value of mouse location</param>
        /// <returns>whether or not the mouse is within the shape</returns>
        public virtual bool Contains(int x, int y)
        {
            return this.Margins.Contains(x, y);
        }

        /// <summary>
        /// Splits the circle by adding 4 more circles that 
        /// fit side-by-side in a square-boundary.
        /// </summary>
        /// <param name="circles">list of circles</param>
        /// <param name="circle">circle to split</param>
        public void Split(List<Circle> circles, Circle circle, 
            int index, int mouseLocX, int mouseLocY)
        {
            int x = circle.Margins.X;
            int y = circle.Margins.Y;
            int s = circle.Margins.Width / 2;
            circles.Remove(circles[index]);
            circles.Add(new Circle(x, y, s));
            circles.Add(new Circle(x + s, y, s));
            circles.Add(new Circle(x, y + s, s));
            circles.Add(new Circle(x + s, y + s, s));
            for (int i = 0; i < circles.Count; i++)
            {
                if (circles[i].Contains(mouseLocX, mouseLocY))
                {
                    this.newIndex = i;
                }
            }
        }

        /// <summary>
        /// Returns index of circle that just split and the mouse is on.
        /// </summary>
        /// <returns>circle that just split</returns>
        public int NewIndex()
        {
            return newIndex;
        }
    }
}
