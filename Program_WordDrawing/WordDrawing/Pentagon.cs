using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Pentagon : Shape
	{
		/// <summary>
		/// Constructs and draws a pentagon, which is inherited from Shape.
		/// </summary>
		/// <param name="x">leftmost coordinate</param>
		/// <param name="y">upmost coordinate</param>
		/// <param name="w">width of boundary</param>
		/// <param name="h">height of boundary</param>
		public Pentagon(float x, float y, float w, float h) : base (x, y, w, h)
		{
		}

		/// <summary>
		/// Converts degress to radians for trig functions.
		/// </summary>
		/// <param name="angle">angle in degrees</param>
		/// <returns>radians</returns>
		public double RadianConversion(double angle)
		{
			double rad = angle * (Math.PI / 180);
			return rad;
		}

		/// <summary>
		/// Draws a pentagon formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
		public override void Draw(Graphics g)
		{
			// distance from the y-value on the left and right
			float ratio1 = (Bounds.Width / 2) * (float)Math.Tan(RadianConversion(38));
			// distance from the x-value on the bottom
			float ratio2 = (Bounds.Height - ratio1) * (float)Math.Tan(RadianConversion(18));
			PointF[] points = new PointF[] { 
				new PointF(Bounds.Left + (Bounds.Width / 2), Bounds.Top),
				new PointF(Bounds.Right, Bounds.Top + ratio1),
				new PointF(Bounds.Right - ratio2, Bounds.Bottom),
				new PointF(Bounds.Left + ratio2, Bounds.Bottom),
				new PointF(Bounds.Left, Bounds.Top + ratio1)};
			using (Brush b = new SolidBrush(this.fill))
				g.FillPolygon(b, points);
			using (Pen p = new Pen(this.outline, this.thickness))
				g.DrawPolygon(p, points);
		}
	}
}
