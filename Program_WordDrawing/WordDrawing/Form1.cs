using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WordDrawing
{
	public partial class Form1 : Form
	{
		private DrawingEngine smeargle;

		public Form1()
		{
			InitializeComponent();
			this.smeargle = new DrawingEngine();
			this.toolkit.SelectedIndex = 0; // select top item in list by default
		}

		private void pictureBox1_Paint(object sender, PaintEventArgs e)
		{
			this.smeargle.Draw(e.Graphics);
		}

		private void pictureBox1_Resize(object sender, EventArgs e)
		{
			// force entire picture to redraw, any time picture box resizes
			this.pictureBox1.Invalidate();
			// above line of code marks the entire picture as being in need of redrawing
			// AND generates a Paint event for pictureBox1
		}

		private void pictureBox1_MouseDown(object sender, MouseEventArgs e)
		{
			this.smeargle.MouseDown((string)this.toolkit.SelectedItem, e.X, e.Y);
            this.toolkit.SelectedIndex = 0; // select top item in list by default
			// selection might have changed, so update information about it
			if (this.smeargle.Selected != null)
			{
				this.xSelected.Text = this.smeargle.Selected.X.ToString();
				this.ySelected.Text = this.smeargle.Selected.Y.ToString();
			}
			this.pictureBox1.Invalidate();
		}

		private void pictureBox1_MouseMove(object sender, MouseEventArgs e)
		{
			if (this.smeargle.MouseMove(e.Location))
				this.pictureBox1.Invalidate();
		}

		private void pictureBox1_MouseUp(object sender, MouseEventArgs e)
		{
			this.smeargle.MouseUp(e.Location);
		}

		private void bringToFront_Click(object sender, EventArgs e)
		{
            this.smeargle.Layout(bringToFront.Name);
			this.pictureBox1.Invalidate();
		}

		private void bringForward_Click(object sender, EventArgs e)
		{
            this.smeargle.Layout(bringForward.Name);
			this.pictureBox1.Invalidate();
		}

		private void bringToBack_Click(object sender, EventArgs e)
		{
            this.smeargle.Layout(bringToBack.Name);
			this.pictureBox1.Invalidate();
		}

		private void bringBackward_Click(object sender, EventArgs e)
		{
            this.smeargle.Layout(bringBackward.Name);
			this.pictureBox1.Invalidate();
		}

		private void delete_Click(object sender, EventArgs e)
		{
			this.smeargle.Control(delete.Name);
			this.pictureBox1.Invalidate();
		}

        private void duplicate_Click(object sender, EventArgs e)
        {
            this.smeargle.Control(duplicate.Name);
            this.pictureBox1.Invalidate();
        }

		private void outlineColor_Click(object sender, EventArgs e)
		{
			// if the color panel showed up, 
			// set the shape's outline color to the color chosen
			if (colorDialog1.ShowDialog() == DialogResult.OK)
			{
				this.smeargle.Selected.OutlineColor = colorDialog1.Color;
			}
			this.pictureBox1.Invalidate();
		}

		private void fillColor_Click(object sender, EventArgs e)
		{
			// if the color panel showed up, 
			// set the shape's fill color to the color chosen
			if (colorDialog2.ShowDialog() == DialogResult.OK)
			{
				this.smeargle.Selected.FillColor = colorDialog2.Color;
			}
			this.pictureBox1.Invalidate();
		}

		private void noOutline_Click(object sender, EventArgs e)
		{
			this.smeargle.Format(noOutline.Name);
			this.pictureBox1.Invalidate();
		}

		private void noFill_Click(object sender, EventArgs e)
		{
			this.smeargle.Format(noFill.Name);
			this.pictureBox1.Invalidate();
		}

		private void thicknesskit_SelectedIndexChanged(object sender, EventArgs e)
		{
			this.smeargle.ChangeThickness((string)this.thicknesskit.SelectedItem);
			this.pictureBox1.Invalidate();
		}
	}
}
