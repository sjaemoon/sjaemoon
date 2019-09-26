using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Octagon : Shape
	{
		/// <summary>
		/// Constructs and draws an octagon, which is inherited from Shape.
		/// </summary>
		/// <param name="x">leftmost coordinate</param>
		/// <param name="y">upmost coordinate</param>
		/// <param name="w">width of bounadry</param>
		/// <param name="h">height of boundary</param>
		public Octagon(float x, float y, float w, float h) : base (x, y, w, h)
		{
		}

		/// <summary>
		/// Draws an octagon formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
		public override void Draw(Graphics g)
		{
			// Each side can be divided into a third of the boundary's width and height
			PointF[] points = new PointF[] { 
				new PointF(Bounds.Left + (Bounds.Width / 3), Bounds.Top),
				new PointF(Bounds.Right - (Bounds.Width / 3), Bounds.Top),
 				new PointF(Bounds.Right, Bounds.Top + (Bounds.Height / 3)),
				new PointF(Bounds.Right, Bounds.Bottom - (Bounds.Height / 3)),
				new PointF(Bounds.Right - (Bounds.Width / 3), Bounds.Bottom),
				new PointF(Bounds.Left + (Bounds.Width / 3), Bounds.Bottom),
				new PointF(Bounds.Left, Bounds.Bottom - (Bounds.Height / 3)),
				new PointF(Bounds.Left, Bounds.Top + (Bounds.Height / 3))};
			using (Brush b = new SolidBrush(this.fill))
				g.FillPolygon(b, points);
			using (Pen p = new Pen(this.outline, this.thickness))
				g.DrawPolygon(p, points);
		}
	}
}
