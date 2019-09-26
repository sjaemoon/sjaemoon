// Required (done):
//		- dragging
//		- format (outline color, fill color)
//		- layout (bring to front, bring to back, etc...)
//		- control (delete)
//		- triangle

// Digging deeper (done): 
//		- all 8 resize markers functioning
//		- outline thickness
//		- duplicate
//		- regular polygons (pentagon, hexagon, octagon)

// Digging deeper (in progress or in the future):
//		- star
//		- drag to create
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WordDrawing
{
	class DrawingEngine
	{
		public static Random rand = new Random();
		private const int RegularSize = 100;

		// currently selected shape
		public Shape Selected { get; private set; }
		// list of shapes
		private List<Shape> shapes;
		private Resize resizeMarkers;
        // states:
		private bool dragging;
		private bool resizing;
		// info about the shape
		private PointF loc;
        private float startX;
        private float startY;
		private SizeF size;
		private int index;

		/// <summary>
		/// Constructor that creates a new list of shapes
		/// (empty initally) to save the shapes in order when drawn.
		/// </summary>
		public DrawingEngine()
		{
			// nothing is selected at first
			this.Selected = null;
			this.shapes = new List<Shape>();
		}

		/// <summary>
		/// Draw all shapes that were added on the list, and
		/// if a shape is selected, draw the selection markers around it.
		/// </summary>
		/// <param name="g"></param>
		public void Draw(Graphics g)
		{
			foreach (Shape drawMe in this.shapes)
			{
				drawMe.Draw(g);
				this.Selected = shapes[shapes.Count - 1];
			}
			if (this.Selected != null)
				this.Selected.DrawSelectionMarkers(g);
		}

		/// <summary>
		/// Select a shape, then add/create a shape.
		/// </summary>
		/// <param name="tool">chosen option in the combo box</param>
		/// <param name="x">x-value of mouse location</param>
		/// <param name="y">y-value of mouse location</param>
		public void MouseDown(string tool, float x, float y)
		{
			switch (tool)
			{
				case "Selection Tool":
					this.ChangeSelection(x, y);
					// if the mouse is on a resize marker, 
					// then change the state to resizing
					if (this.Selected != null && ContainsMarker(x, y))
						this.resizing = true;
					// if the mouse is pressed on the shape,
					// then change the state to dragging
					else if (this.Selected != null)
						this.dragging = true;
					// remember the initial mouse location for offset later
					this.startX = x;
					this.startY = y;
					break;
				// initally draws all the shape as a regular or ideal polygon
				case "Rectangle":
					this.shapes.Add(new Rect(x, y, RegularSize, RegularSize / 2));
					break;
				case "Square":
					this.shapes.Add(new Square(x, y, RegularSize));
					break;
				case "Oval":
					this.shapes.Add(new Oval(x, y, RegularSize / 2, RegularSize));
					break;
				case "Circle":
					this.shapes.Add(new Circle(x, y, RegularSize));
					break;
				case "Triangle":
					this.shapes.Add(new Triangle(x, y, RegularSize, RegularSize));
					break;
				case "Pentagon":
					this.shapes.Add(new Pentagon(x, y, RegularSize, RegularSize));
					break;
				case "Hexagon":
					this.shapes.Add
						(new Hexagon(x, y, (RegularSize * 2) / (float)Math.Sqrt(3), RegularSize));
					break;
				case "Octagon":
					this.shapes.Add(new Octagon(x, y, RegularSize, RegularSize));
					break;
				case "Star": // work in progress...
					this.shapes.Add(new Star(x, y, RegularSize, RegularSize));
					break;
				default: // optional - tool doesn't match any label above
					throw new NotImplementedException();
			}
		}

		/// <summary>
		/// Checks if the mouse is on any of the resize markers,
		/// and passes which resize marker is pressed to the Resize class.
		/// </summary>
		/// <param name="x">x-value of mouse location</param>
		/// <param name="y">y-value of mouse location</param>
		/// <returns>whether or not a resize marker is clicked</returns>
		private bool ContainsMarker(float x, float y)
		{
			this.resizeMarkers = new Resize(this.loc, this.size);
			for (int i = 0; i < resizeMarkers.Markers.Length; i++)
			{
				if (resizeMarkers.Markers[i].Contains(x, y))
				{
					resizeMarkers.GetMarkerIndex(i);
					return true;
				}
			}
			return false;
		}

		/// <summary>
		/// Checks if the mouse is pressed and on the shape
		/// and if it is, shape is considered to be selected.
		/// </summary>
		/// <param name="x">x-value of mouse location</param>
		/// <param name="y">y-value of mouse location</param>
		private void ChangeSelection(float x, float y)
		{
			// see if given point is inside one of our shapes
			for (int i = this.shapes.Count - 1; i >= 0; i--)
			{
				Shape s = this.shapes[i];
				// remember location and size of shape to be altered
				this.loc = this.shapes[i].ShapeLocation(); 
				this.size = this.shapes[i].ShapeSize();
				if (s.Contains(x, y))
				{
					// remember which shape is now selected
					this.Selected = s;
					// remember the index of the selected shape for possible layout change
					this.index = i;
					return;
				}
				// given coordinates do not lie inside any of our shapes
				// clear selection!
				this.Selected = null;
			}
		}

		/// <summary>
		/// Depending on the state, properly drags or resizes the shape.
		/// </summary>
		/// <param name="mouseLoc">location of the mouse</param>
		/// <returns>a message whether to refresh or not based on activity</returns>
		public bool MouseMove(Point mouseLoc)
		{
			bool didSomething = false;
			if (this.resizing)
			{
				System.Diagnostics.Debug.Assert(this.Selected != null);
				// dimension calculation in Resize class
				float w = resizeMarkers.LeftRight(mouseLoc);
				float h = resizeMarkers.UpDown(mouseLoc);
				float dx = resizeMarkers.ChangeInX();
				float dy = resizeMarkers.ChangeInY();
				// disables the user from making the shape unselectable by reflceting the shape
				if (w < 10)
					w = 10;
				if (h < 10)
					h = 10;
				// expand the dimensions to how much the mouse moved
				this.Selected.ChangeSize(w, h);
				this.Selected.ChangeLocation(this.loc.X + dx, this.loc.Y + dy);
				didSomething = true;
			}
			else if (this.dragging)
			{
				// move currently selected shape (no way it should be null!)
				System.Diagnostics.Debug.Assert(this.Selected != null);
				// offset calculation
                float dx = mouseLoc.X - this.startX;
                float dy = mouseLoc.Y - this.startY;
				// keep the mouse location true to its location, not the shape's location
                this.Selected.ChangeLocation(this.loc.X + dx, this.loc.Y + dy);
				didSomething = true;
			}
			return didSomething;
		}

		/// <summary>
		/// Once the user lifts the mouse, signifying end of
		/// dragging or resizing, change the state to rest state.
		/// </summary>
		/// <param name="mouseLoc"></param>
		public void MouseUp(Point mouseLoc)
		{
			this.dragging = false;
			this.resizing = false;
		}

		/// <summary>
		/// Changes the layout of the shape based on what the user wants.
		/// </summary>
		/// <param name="clicked">clicked layout button</param>
		public void Layout(string clicked)
		{
			// if the following button is pressed:
			// AND shape is not already in the very front
            if (clicked == "bringToFront" && this.index != this.shapes.Count - 1)
            {
				// add the same shape to the very end (very front) 
				// and delete the original shape
                this.shapes.Add(this.Selected);
                this.shapes.RemoveAt(this.index);
            }
			// AND shape is not already in the very front
            else if (clicked == "bringForward" && this.index != this.shapes.Count - 1)
            {
				// add the same shape after the next index (one infront)
				// and delete the original shape
                this.shapes.Insert(this.index + 2, this.Selected);
                this.shapes.RemoveAt(this.index);
                index += 1;
            }
			// AND shape is not already in the very back
            else if (clicked == "bringToBack" && this.index != 0)
            {
				// add the same shape to the very beginning (back) 
				// and delete the original shape, which is pushed one index up.
                this.shapes.Insert(0, this.Selected);
                this.shapes.RemoveAt(this.index + 1);
            }
			// AND shape is not already in the very back
            else if (clicked == "bringBackward" && this.index != 0)
            {
				// add the same shape before the previous index (one backward) 
				// and delete the original shape, which is pushed one index up.
                this.shapes.Insert(this.index - 1, this.Selected);
                this.shapes.RemoveAt(this.index + 1);
                index -= 1;
            }
		}

		/// <summary>
		/// Sets either the fill and the outline color to transparent.
		/// </summary>
		/// <param name="clicked">clicked format button</param>
		public void Format(string clicked)
		{
			if (clicked == "noOutline")
				this.Selected.OutlineColor = Color.Transparent;
			if (clicked == "noFill")
				this.Selected.FillColor = Color.Transparent;
		}

		/// <summary>
		/// Executes all the controls the user can do
		/// such as deleting and duplicating.
		/// </summary>
		/// <param name="command">chosen command to execute</param>
		public void Control(string command)
		{
            if (command == "delete")
            {
				// remove the shape at that index, and deselect the item
                this.shapes.RemoveAt(this.index);
                this.Selected = null;
            }
            if (command == "duplicate")
            {
				// adds the clone that cotains all the info of the original shape
				Shape clone = this.Selected.Copy();
				this.shapes.Add(clone);
				// so duplicated shape is both visible and selected when created...
				clone.X += 10;
				clone.Y += 10;
				this.Selected = clone;
            }
		}

		/// <summary>
		/// Changes the thickness of the shape's outline
		/// based on what the user chooses in the combo box.
		/// </summary>
		/// <param name="lead">chosen option in the combo box</param>
		public void ChangeThickness(string lead)
		{
			switch (lead)
			{
				case "1":
					this.Selected.SetThickness = 1;
					break;
				case "2":
					this.Selected.SetThickness = 2;
					break;
				case "4":
					this.Selected.SetThickness = 4;
					break;
				case "8":
					this.Selected.SetThickness = 8;
					break;
				case "10":
					this.Selected.SetThickness = 10;
					break;
				case "14":
					this.Selected.SetThickness = 14;
					break;
				case "18":
					this.Selected.SetThickness = 18;
					break;
				default:
					throw new NotImplementedException();
			}
		}
	}
}
