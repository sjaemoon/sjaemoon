﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Rect : Shape
	{
		/// <summary>
		/// Constructs and draws a rectangle, which is inherited from Shape.
		/// </summary>
		/// <param name="x">leftmost coordinate</param>
		/// <param name="y">upmost coordinate</param>
		/// <param name="w">width to set</param>
		/// <param name="h">height to set</param>
		public Rect(float x, float y, float w, float h) : base(x, y, w, h)
		{
		}

		/// <summary>
		/// Draws a rectangle formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
		public override void Draw(Graphics g)
		{
			using (Brush b = new SolidBrush(this.fill))
			{
				g.FillRectangle(b, new RectangleF(this.loc, this.size));
			}
			using (Pen p = new Pen(this.outline, this.thickness))
				g.DrawRectangle(p, new Rectangle(Point.Round(this.loc), Size.Round(this.size)));
		}
	}
}
