using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class Resize
	{
		private PointF loc; // top right
		private SizeF size;
		private int index;
		private float dx;
		private float dy;

		public Resize(PointF loc, SizeF size)
		{
			this.loc = loc;
			this.size = size;
		}

		/// <summary>
		/// Contains the dimensions for 8 resize markers 
		/// along the rectangular boundary of the shape.
		/// </summary>
		public RectangleF[] Markers
		{
			get
			{
				// appropriate coordinates
				float leftX = this.loc.X - 3;
				float middleX = leftX + (this.size.Width / 2);
				float rightX = leftX + this.size.Width;
				float topY = this.loc.Y - 3;
				float middleY = topY + (this.size.Height / 2);
				float bottomY = topY + this.size.Height;
				// saves each marker as an array
				RectangleF[] markers = new RectangleF[8];
				markers[0] = new RectangleF(leftX, topY, 6, 6);
				markers[1] = new RectangleF(leftX, middleY, 6, 6);
				markers[2] = new RectangleF(leftX, bottomY, 6, 6);
				markers[3] = new RectangleF(middleX, topY, 6, 6);
				markers[4] = new RectangleF(middleX, bottomY, 6, 6);
				markers[5] = new RectangleF(rightX, topY, 6, 6);
				markers[6] = new RectangleF(rightX, middleY, 6, 6);
				markers[7] = new RectangleF(rightX, bottomY, 6, 6);
				return markers;
			}
		}

		/// <summary>
		/// Saves the index of the resize marker that is pressed.
		/// </summary>
		/// <param name="i">index of resize marker that is pressed</param>
		public void GetMarkerIndex(int i)
		{
			this.index = i;
		}

		/// <summary>
		/// Changes the width (left-right) component 
		/// based on which resize marker is pressed.
		/// </summary>
		/// <param name="mouseLoc">current location of the mouse</param>
		/// <returns>new width</returns>
		public float LeftRight(PointF mouseLoc)
		{
			// markers on the righthand side of the shape
			if (this.index == 5 || this.index == 6 || this.index == 7)
				return mouseLoc.X - this.loc.X;
			// markers on the lefthand side of the shape
			else if (this.index == 0 || this.index == 1 || this.index == 2)
			{
				this.dx = mouseLoc.X - this.loc.X;
				return this.size.Width + (-1 * this.dx);
			}
			else
				return this.size.Width;
		}

		/// <summary>
		/// Changes the height (up-down) component
		/// based on which resize marker is pressed.
		/// </summary>
		/// <param name="mouseLoc">current mouse location</param>
		/// <returns>new height</returns>
		public float UpDown(PointF mouseLoc)
		{
			// markers on the bottomhand side of the shape
			if (this.index == 2 || this.index == 4 || this.index == 7)
			{
				return mouseLoc.Y - this.loc.Y;
			}
			// markers on the tophand side of the shape
			else if (this.index == 0 || this.index == 3 || this.index == 5)
			{
				this.dy = mouseLoc.Y - this.loc.Y;
				return this.size.Height + (-1 * this.dy);
			}
			else
				return this.size.Height;
		}

		/// <summary>
		/// Returns the change in x-location.
		/// </summary>
		/// <returns>change in x-location</returns>
		public float ChangeInX()
		{
			return this.dx;
		}

		/// <summary>
		/// Returns the change in y-location.
		/// </summary>
		/// <returns>change in y-location</returns>
		public float ChangeInY()
		{
			return this.dy;
		}
	}
}
