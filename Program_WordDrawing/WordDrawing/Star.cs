// Work in progres...
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Star : Shape
	{
		/// <summary>
		/// Constructs and draws a star, which is inherited from Shape.
		/// </summary>
		/// <param name="x">leftmost coordinate</param>
		/// <param name="y">upmost coordinate</param>
		/// <param name="w">width of boundary</param>
		/// <param name="h">height of boundary</param>
		public Star(float x, float y, float w, float h) : base (x, y, w, h)
		{
		}

		/// <summary>
		/// Draws a star formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
		public double RadianConversion(double angle)
		{
			double rad = angle * (Math.PI / 180);
			return rad;
		}

		/// <summary>
		/// Converts degrees to radians for trig functions.
		/// </summary>
		/// <param name="angle">angle in degrees</param>
		/// <returns>radians</returns>
		public double PythagoreanTheorem(double a, double b)
		{
			double c = Math.Sqrt(Math.Pow(a, 2) + Math.Pow(b, 2));
			return c;
		}

		/// <summary>
		/// Draws a star formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
		public override void Draw(Graphics g)
		{
			float ratio1 = (Bounds.Width / 2) * (float)Math.Tan(RadianConversion(36));
			float ratio2 = (Bounds.Height - ratio1) * (float)Math.Tan(RadianConversion(18));
			float length = (float)PythagoreanTheorem(ratio1, ratio2);
			float ratio3 = ((float)Math.Sin(RadianConversion(36)) * length) / (float)Math.Sin(RadianConversion(108));
			PointF[] points = new PointF[] { 
				new PointF(Bounds.Left + (Bounds.Width / 2), Bounds.Top),
				new PointF(Bounds.Right - ratio3, Bounds.Top + ratio1),
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
