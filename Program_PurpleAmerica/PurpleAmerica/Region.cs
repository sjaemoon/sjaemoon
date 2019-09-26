// This class is executed after getting the coordinate points 
// for each subregion to get its election result (represented by a color).
// Then both outline and fill are drawn, and the steps are repeated until 
// all subregions' colors for the region are filled.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.IO;

namespace PurpleAmerica
{
	class Region
	{
		private const double ratio = 255.0; // max ARGB value
		private string region;
		private string subregion;
		private int electionYear;
		private Color fill;
		private Color outline;

		/// <summary>
		/// Constructs the Region class and declares the method
		/// to get the fill color if the user wanted to.
		/// </summary>
		/// <param name="region">region name</param>
		/// <param name="subregion">subregion (county) name</param>
		/// <param name="electionYear">election year</param>
		public Region(string region, string subregion, int electionYear)
		{
			this.region = region;
			this.subregion = subregion;
			this.electionYear = electionYear;
			// initally...
			this.fill = Color.Empty;
			this.outline = Color.Black;
			// if fill color is to exist, outline color is white because it looks better
			if (electionYear != -1)
			{
				FillColor();
				this.outline = Color.White;
			}
		}

		/// <summary>
		/// Reads the election result data and converts it to a color.
		/// </summary>
		private void FillColor()
		{
			using (StreamReader sr = new StreamReader
				("Data\\" + this.region + this.electionYear + ".txt"))
			{
				// possibly needed for later file reading
				string[] candidates = sr.ReadLine().Split(','); 
				while (!sr.EndOfStream)
				{
					string[] results = sr.ReadLine().Split(',');
					string subregion = results[0];
					// gets the color for the region
					if (this.subregion == subregion)
					{
						int rep = int.Parse(results[1]);
						int dem = int.Parse(results[2]);
						int ind = int.Parse(results[3]);
						// converts the data in proportion with 0-225 ARGB scale
						// double is more accurate than int while calculating...
						double red = ratio * rep / (rep + dem + ind);
						double green = ratio * ind / (rep + dem + ind);
						double blue = ratio * dem / (rep + dem + ind);
						this.fill = Color.FromArgb((int)red, (int)green, (int)blue);
					}
				}
			}
		}

		/// <summary>
		/// Returns the fill color for the region 
		/// based on the election data.
		/// </summary>
		/// <returns>fill</returns>
		public Color Fill()
		{
			return fill;
		}

		/// <summary>
		/// Returns the outline color for the region
		/// based on whether the user wants to show the election data.
		/// </summary>
		/// <returns>outline</returns>
		public Color Outline()
		{
			return outline;
		}

		// Possible approaches for later:
		// draw outline, given Graphics
		// fill region based on election data
		// Load/Add election data (returns, having done nothing, if election data already loaded
		// or... you have a way of seeing if election data loaded (check for null somewhere)
	}
}
