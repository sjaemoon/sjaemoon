using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WhosThatPokemon
{
    public partial class Form1 : Form
    {
        // Search within (001 - 720) to find Pokemon corresponding to its Pokedex Number.
        // ***** ALWAYS HAS TO BE IN THREE DIGIT FORMAT *****
        // private const string pokedexNumber = "001"; //uncomment to choose specific pokemon
        private const int minPixel = 2;
        private List<Circle> circles;
        private Circle circle;
        private PixelArray pixelArray;
        private Bitmap bitmap;
        private int doNotSplitIndex = -1;

        public Form1()
        {
            InitializeComponent();
            this.DoubleBuffered = true;
            SetFormSize();
            Random random = new Random();
            String pokedexNumber = random.Next(0, 721).ToString("000"); // comment to use specific pokemon constant above
            this.bitmap = new Bitmap((Bitmap)Image.FromFile
                ("Pokedex//" + pokedexNumber + ".png", true), this.ClientRectangle.Size);
            //this.bitmap = new Bitmap((Bitmap)Image.FromFile
            //    ("Images//", true), this.ClientRectangle.Size); // to use customized image
            this.pixelArray = new PixelArray(this.bitmap);
            this.circles = new List<Circle>();
            InitialCircle();
        }

        /// <summary>
        /// Resizes the form to match bitmap dimensions.
        /// </summary>
        private void SetFormSize()
        {
            this.Width += this.Width - this.ClientRectangle.Width;
            this.Height += this.Height - this.ClientRectangle.Height;
        }

        /// <summary>
        /// Adds the inital circle to the list to be drawn.
        /// </summary>
        private void InitialCircle()
        {
            this.circles.Add(new Circle(0, 0, this.bitmap.Width));
            this.Invalidate();
        }

        /// <summary>
        /// Draws all the circles in the list when the form is invalidated.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnPaint(PaintEventArgs e)
        {
            foreach (Circle circle in circles)
            {
                using (Brush b = new SolidBrush(this.pixelArray.GetColor
                    (circle.Margins.X, circle.Margins.Y, circle.Margins.Width)))
                {
                    e.Graphics.FillEllipse(b, circle.Margins.X, circle.Margins.Y,
                        circle.Margins.Width, circle.Margins.Height);
                }
            }
            base.OnPaint(e);
        }

        /// <summary>
        /// Responds to user's mouse movement and changes the 'split' state.
        /// </summary>
        /// <param name="sender">ultimate base class</param>
        /// <param name="e">mouse information</param>
        private void Form1_MouseMove_1(object sender, MouseEventArgs e)
        {
            Point mouseLoc = e.Location;
            bool splittable = false;
            int index = 0;
            for (int i = 0; i < this.circles.Count; i++)
            {
                // In order to be in the 'split' state...
                // checks if mouse is in any of the circle in the list,
                // checks if that circle can be splitted any further,
                // checks if the user was just on that circle
                if (this.circles[i].Contains(mouseLoc.X, mouseLoc.Y) &&
                    this.doNotSplitIndex != i && 
                    this.circles[i].Margins.Width / 2 > minPixel)
                {
                    this.circle = circles[i];
                    index = i;
                    splittable = true;
                }
            }
            if (splittable)
            {
                // split the circle and remove the inital circle
                this.circle.Split(this.circles, this.circle, index, mouseLoc.X, mouseLoc.Y);
                // knows which circle that just split, so it does not split again automatically
                this.doNotSplitIndex = this.circle.NewIndex();
                // update
                this.Invalidate();
            }
        }
    }
}
