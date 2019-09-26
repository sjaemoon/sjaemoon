using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
    class Triangle : Shape
    {
		/// <summary>
		/// Constructs and draws a triangle, which is inherited from Shape.
		/// </summary>
		/// <param name="x">leftmost coordinate</param>
		/// <param name="y">upmost coordinate</param>
		/// <param name="w">width to set</param>
		/// <param name="h">height to set</param>
        public Triangle(float x, float y, float w, float h) : base(x, y, w, h)
		{
		}

		/// <summary>
		/// Draws a triagle formed with updatable components.
		/// </summary>
		/// <param name="g">graphics</param>
        public override void Draw(Graphics g)
        {
			PointF[] points = { 
				new PointF(Bounds.Left + (Bounds.Width / 2), Bounds.Top), // top middle
				new PointF(Bounds.Left, Bounds.Bottom), // bottom left
				new PointF(Bounds.Left + Bounds.Width, Bounds.Bottom) }; // bottom right
            using (Brush b = new SolidBrush(this.fill))
                g.FillPolygon(b, points);
			using (Pen p = new Pen(this.outline, this.thickness))
				g.DrawPolygon(p, points);
        }
    }
}
