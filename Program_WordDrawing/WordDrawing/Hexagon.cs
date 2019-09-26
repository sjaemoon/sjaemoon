using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Hexagon : Shape
	{
		/// <summary>
		/// Constructs and draws a hexagon, which is inherited from Shape.
		/// </summary>
		/// <param name="x">leftmost coordinate</param>
		/// <param name="y">upmost coordinate</param>
		/// <param name="w">width of boundary</param>
		/// <param name="h">height of boundary</param>
		public Hexagon(float x, float y, float w, float h) : base (x, y, w, h)
		{
		}

		/// <summary>
		/// Draws a hexagon formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
		public override void Draw(Graphics g)
		{
			// calculates how far the top and bottom sides are 
			// from the x-axis using 30-60-90 triangle ratio
			float ratio = (Bounds.Height / 2) / (float)Math.Sqrt(3);
			PointF[] points = new PointF[] { 
				new PointF(Bounds.Left + ratio, Bounds.Top),
				new PointF(Bounds.Right - ratio, Bounds.Top),
				new PointF(Bounds.Right, Bounds.Top + Bounds.Height / 2),
				new PointF(Bounds.Right - ratio, Bounds.Bottom),
				new PointF(Bounds.Left + ratio, Bounds.Bottom),
				new PointF(Bounds.Left, Bounds.Top + Bounds.Height / 2)};
			using (Brush b = new SolidBrush(this.fill))
				g.FillPolygon(b, points);
			using (Pen p = new Pen(this.outline, this.thickness))
				g.DrawPolygon(p, points);
		}
	}
}
