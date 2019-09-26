// Brian Moon
// This program emulates the shape ability of Microsoft Word (not fully optimized)
// Features:
//		- Windows Form Application
//		- Inheritance and Object-based programming
//		- Systems.Drawing
//		- Abstract class
//		- Virtual
//		- Other algorithms and logic

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WordDrawing
{
	static class Program
	{
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main()
		{
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new Form1());
		}
	}
}
