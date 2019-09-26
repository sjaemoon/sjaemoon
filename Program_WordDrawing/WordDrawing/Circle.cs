using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Circle : Oval
	{
		/// <summary>
		/// Constructs and draws an oval, which is inherited from Circle.
		/// </summary>
		/// <param name="x">upmost coordinate</param>
		/// <param name="y">leftmost coordinate</param>
		/// <param name="s">radius</param>
		public Circle(float x, float y, float s) : base(x, y, s, s)
		{
		}
	}
}
