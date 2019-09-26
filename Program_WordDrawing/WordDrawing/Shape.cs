using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	// "abstract" - you can't actually create a generic shape
	// you have to create a specific kind of shape (like Rect, etc.)
	abstract class Shape
	{
		// "protected" - can be seen by this class, and any that inherit from it
		protected PointF loc; // top right
		protected SizeF size;
		protected Color fill;
		protected Color outline;
		protected float thickness;
		// gets the x and y coordinates of the shape once selected
		public int X
		{
			get { return (int)this.loc.X; }
			set
			{
				this.loc.X = value;
			}
		}
		public int Y
		{
			get { return (int)this.loc.Y; }
			set
			{
				this.loc.Y = value;
			}
		}

		/// <summary>
		/// "Abstract" method that accompanies the class;
		/// "inheriting classes must override this method."
		/// </summary>
		/// <param name="g">graphic</param>
		public abstract void Draw(Graphics g);

		/// <summary>
		/// Constructs a shape that has the components
		/// of location, size, and color.
		/// </summary>
		/// <param name="x">leftmost x-value of shape</param>
		/// <param name="y">upmost y-value of shape</param>
		/// <param name="w">width of shape</param>
		/// <param name="h">height of shape</param>
		public Shape(float x, float y, float w, float h)
		{
			this.loc = new PointF(x, y);
			this.size = new SizeF(w, h);
			this.outline = Color.DarkGray;
			this.fill = Color.LightGray;
        }

		/// <summary>
		/// Contains a rectangle that encompasses 
		/// the shape, with no room to spare.
		/// </summary>
		public RectangleF Bounds
		{
			get { return new RectangleF(this.loc, this.size); }
		}

		/// <summary>
		/// Physically draws both boundary and resize markers.
		/// </summary>
		/// <param name="g">graphics</param>
		public virtual void DrawSelectionMarkers(Graphics g)
		{
            Rectangle boundary = Rectangle.Round(this.Bounds);	
			Resize resize = new Resize(this.loc, this.size);
			RectangleF[] resizeMarkers = resize.Markers;
			// draws the boundary and each resize marker in the array
			using (Pen p = new Pen(Brushes.DarkGray, 2))
            {
                g.DrawRectangle(p, boundary);
				foreach (RectangleF r in resizeMarkers)
				{
					Rectangle markers = Rectangle.Round(r);
					g.DrawRectangle(p, markers);
				}
            }
            using (Brush b = new SolidBrush(Color.White))
			{
				foreach (RectangleF r in resizeMarkers)
				{                
					g.FillRectangle(b, r);
				}
            }
		}

		/// <summary>
		/// Returns true if the given coordinates lie
		/// within the bounds of this shape.
		/// </summary>
		/// <param name="x">x-value of mouse location</param>
		/// <param name="y">y-value of mouse location</param>
		/// <returns>whether or not the mouse is within the shape</returns>
		public virtual bool Contains(float x, float y)
		{
			return this.Bounds.Contains(x, y);
		}

		/// <summary>
		/// Makes location changeable in other classes.
		/// </summary>
		/// <returns>location (top left) of the shape</returns>
		public PointF ShapeLocation()
		{
			return this.loc;
		}

		/// <summary>
		/// Receives the changed location and updates.
		/// </summary>
		/// <param name="x">new x-location</param>
		/// <param name="y">new y-location</param>
		public void ChangeLocation(float x, float y)
		{
			this.loc.X = x;
			this.loc.Y = y;
		}

		/// <summary>
		/// Makes the size changeable in other classes.
		/// </summary>
		/// <returns>size of shape</returns>
		public SizeF ShapeSize()
		{
			return this.size;
		}

		/// <summary>
		/// Receives the changed size and updates.
		/// </summary>
		/// <param name="w">new width</param>
		/// <param name="h">new height</param>
		public void ChangeSize(float w, float h)
		{
			this.size.Width = w;
			this.size.Height = h;
		}

		/// <summary>
		/// Sets the outline color of the shape.
		/// </summary>
		public Color OutlineColor
		{
			set
			{
				this.outline = value;
			}
		}

		/// <summary>
		/// Sets the fill color of the shape.
		/// </summary>
		public Color FillColor
		{
			set
			{
				this.fill = value;
			}
		}

		/// <summary>
		/// Sets the thickness of the shape's outline.
		/// </summary>
		public float SetThickness
		{
			set
			{
				this.thickness = value;
			}
		}

		/// <summary>
		/// Copys the shape along with all of its components.
		/// </summary>
		/// <returns>identical shape</returns>
		public virtual Shape Copy()
		{
			Shape clone = (Shape)this.MemberwiseClone();
			return clone;
		}
	}
}
