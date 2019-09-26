using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace WhosThatPokemon
{
    class PixelArray
    {
        private Color[,] colorMap;

        /// <summary>
        /// Stores all the pixel in an array.
        /// </summary>
        /// <param name="bitmap">information about the image</param>
        public PixelArray(Bitmap bitmap)
        {
            // GetPixel(); is slow, so it only executes once when the image is first loaded
            this.colorMap = new Color[bitmap.Width, bitmap.Height];
            for (int x = 0; x < bitmap.Width; x++)
                for (int y = 0; y < bitmap.Height; y++)
                    this.colorMap[x, y] = bitmap.GetPixel(x, y);
        }

        /// <summary>
        /// Gets the average color bounded by the circle
        /// </summary>
        /// <param name="initialX">top-left x-value</param>
        /// <param name="initialY">top-left y-value</param>
        /// <param name="s">size of the boundary</param>
        /// <returns></returns>
        public Color GetColor(int initialX, int initialY, int s)
        {
            int r = 0;
            int g = 0;
            int b = 0;
            int totalPixel = s * s;
            for (int x = initialX; x < initialX + s; x++)
            {
                for (int y = initialY; y < initialY + s; y++)
                {
                    // sum of all color component
                    r += this.colorMap[x, y].R;
                    g += this.colorMap[x, y].G;
                    b += this.colorMap[x, y].B;
                }
            }
            return Color.FromArgb(r / totalPixel, g / totalPixel, b / totalPixel);
        }
    }
}
