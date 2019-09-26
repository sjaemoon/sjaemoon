// Brian Moon
// This program draws a visualization of the 
// Purple America data from a given file using
// DrawPanel.dll and System.IO.
//
// Features: 
//	- ability to understand the file and get relevant info
//	- scaling the latitude and longitude to fit on the drawing panel
//	- organization using the Region class
//	- reasonable order of execution


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Drawing;
using DrawPanelLibrary;

namespace PurpleAmerica
{
	class Program
	{
		// where data is stored
		const string FilePath = "Data\\";
		// single reasonable scale for all states to show
		const int Scale = 50; 
		// for handling exceptions...
		const int EarliestYear = 1960;
		const int LatestYear = 2012;
		const int ElectionPattern = 4;

		static void Main(string[] args)
		{
			Intro();
			string response;
			do
			{
                string stateName = StateName();
                int electionYear = ElectionYear(stateName);
				Region(stateName, electionYear);
				Console.Write("Do you want to draw something else? (y or n) ");
				response = Console.ReadLine();
				Console.WriteLine();
			} while (response.ToLower().StartsWith("y"));
			End();
		}

		/// <summary>
		/// Describes what the program is about, 
		/// and gives a direction to the user.
		/// </summary>
		static void Intro()
		{
			Console.WriteLine("This program draws a visualization of data from a given file. Enter");
			Console.WriteLine("	- the 2 letter abbreviation for the state");
			Console.WriteLine("	- USA for all of the US by state, or");
			Console.WriteLine("	- USA-county for all of the US by county.\n");
		}

        #region
        //static Tuple<string, string> GetFileName()
        //{
        //    Console.Write("What state would you like to see? ");
        //    string stateAbbreviation = Console.ReadLine();
        //    while (!File.Exists(FilePath + stateAbbreviation + ".txt"))
        //    {
        //        Console.Write("That state does not exist. Try again. ");
        //        stateAbbreviation = Console.ReadLine();
        //    }
        //    Console.Write("Do you want to map election data? (y or n) ");
        //    string response = Console.ReadLine();
        //    string stateAndYear;
        //    if (response.ToLower().StartsWith("y"))
        //    {
        //        Console.Write("Which presidential election data would you like to see? ");
        //        string year = Console.ReadLine();
        //        while (!File.Exists(FilePath + stateAbbreviation + year + ".txt"))
        //        {
        //            Console.Write("An election did not happen that year. Try again. ");
        //            year = Console.ReadLine();
        //        }
        //        stateAndYear = stateAbbreviation + year;
        //    }
        //    else
        //        stateAndYear = null;
        //    Tuple<string, string> fileName = new Tuple<string, string>(stateAbbreviation, stateAndYear);
        //    return fileName;
        //}
        #endregion

		/// <summary>
		/// Asks the user which state to show, and takes 
		/// the abbreviated state name to be used as a file name.
		/// All the exceptions are handled.
		/// </summary>
		/// <returns>state name</returns>
        static string StateName()
        {
            Console.Write("What state would you like to see? ");
            string stateName = Console.ReadLine();
			// checks if the file exist for that state abbreviation given
            while (!File.Exists(FilePath + stateName + ".txt"))
            {
                Console.Write("That state does not exist. Try again. ");
                stateName = Console.ReadLine();
            }
            return stateName;
        }

		/// <summary>
		/// Asks the user which election year data to display (1960-2012),
		/// and takes the year to be used as a file name.
		/// Exceptions for min/max and non-election years are handled.
		/// </summary>
		/// <param name="fileName">state name</param>
		/// <returns>election year</returns>
        static int ElectionYear(string fileName)
        {
			Console.Write("Do you want to map election data? (y or n) ");
			string response = Console.ReadLine();
			int year;
            if (response.ToLower().StartsWith("y"))
            {
				Console.Write("Which presidential election data would you like to see? ({0}-{1}) ", 
					EarliestYear, LatestYear);
                year = int.Parse(Console.ReadLine());
				// keeps asking until the election data is available for that certain year 
				// (multiple of 4 from 1960-2012)
                while ((year % ElectionPattern != 0 || year < EarliestYear || year > LatestYear))
                {
                    Console.Write("An election did not happen that year. Try again. ");
                    year = int.Parse(Console.ReadLine());
                }
            }
            else
                year = -1; // represents that the user did not want to display election result
			return year;
        }

		/// <summary>
		/// Reads the appropriate state file and converts the 
		/// coordinate information into an array to be drawn.
		/// </summary>
		/// <param name="stateName">state name</param>
		/// <param name="electionYear">election year</param>
		static void Region(string stateName, int electionYear)
		{
			using (StreamReader sr = new StreamReader(FilePath + stateName + ".txt"))
			{
				string[] min = sr.ReadLine().Split
					(new char[0], StringSplitOptions.RemoveEmptyEntries); // removes empty spaces
				float minLong = float.Parse(min[0]);
				float minLat = float.Parse(min[1]);
				string[] max = sr.ReadLine().Split
					(new char[0], StringSplitOptions.RemoveEmptyEntries);
				float maxLong = float.Parse(max[0]);
				float maxLat = float.Parse(max[1]);
				// min is scaled down to coordinate (0,0),
				// and max is scaled down to an appropriate size
				int width = (int)(Scale * (maxLong - minLong));
				int height = (int)(Scale * (maxLat - minLat));
				// initializes the drawing panel according to the scaled size
				DrawingPanel dp = new DrawingPanel(width, height);
				Graphics g = dp.GetGraphics();
				int numPolygons = int.Parse(sr.ReadLine());
				sr.ReadLine();
				while (!sr.EndOfStream)
				{
					string subregionName = sr.ReadLine();
					string regionName = sr.ReadLine();
					PointF[] pts = new PointF[int.Parse(sr.ReadLine())];
					for (int i = 0; i < pts.Length; i++)
					{
						string[] loc = sr.ReadLine().Split
							(new char[0], StringSplitOptions.RemoveEmptyEntries);
						// scale algorithm
						float x = Scale * (float.Parse(loc[0]) - minLong);
						float y = Scale * (float.Parse(loc[1]) - minLat);
						pts[i].X = x;
						pts[i].Y = height - y;
					}
					// Initilizes the Region class to the get the fill color 
					// based on the election result the user-specified year;
					// gets the color for each subregion at a time until 
					// all data for the subregions of the region are read
					Region region = new Region(regionName, subregionName, electionYear);
					Draw(dp, g, region, pts);
					sr.ReadLine();
				}
				dp.RefreshDisplay(); // updates the drawing panel
			}
		}

		/// <summary>
		/// Draws and fills the polygon using the 
		/// appropriate fill/outline colors and data points.
		/// </summary>
		/// <param name="dp">created drawing panel</param>
		/// <param name="g">created graphics</param>
		/// <param name="region">data received from the Region class</param>
		/// <param name="pts">array of points that has coordinate information</param>
		static void Draw(DrawingPanel dp, Graphics g, Region region, PointF[] pts)
		{
			using (Pen p = new Pen(region.Outline()))
			{
				using (Brush b = new SolidBrush(region.Fill()))
				{
					g.DrawPolygon(p, pts);
					g.FillPolygon(b, pts);
				}
			}
		}

		/// <summary>
		/// Uses the universal oxymoron to end the program.
		/// </summary>
		static void End()
		{
			Console.WriteLine("Press 'Enter' to Continue...");
			Console.ReadLine();
		}
	}
}
