using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Square : Rect
	{
		/// <summary>
		/// Constructs and draws a square, which is inherited from Rect.
		/// </summary>
		/// <param name="x">upmost coordinate</param>
		/// <param name="y">leftmost coordinate</param>
		/// <param name="s">side length</param>
		public Square(float x, float y, float s) : base(x, y, s, s)
		{
			// if I had additional initializing for my Square, I'd put the code here
		}
	}
}
