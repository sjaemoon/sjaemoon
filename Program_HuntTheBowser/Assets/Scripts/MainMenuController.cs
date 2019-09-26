// Brian Moon
// Game - Hunt the Bowser
// This program will...
//      - draw all the graphics required for the hunt the wumpus game
//      - follow the specs for the wumpus game to have game objects work appropriately
//      - have different scripts for the different scenes required in the game
//      - use all the C# and Unity style and syntax
//      - use the dll created by the game controller and other members to get and return info
using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class MainMenuController : MonoBehaviour 
{
	public Button PlayGame;
	public Button HighScore;
	public Button ExitGame;

    /// <summary>
    /// Starts the game when "Play Game" button is clicked.
    /// </summary>
	public void PlayGameClick()
	{
        Maintained.Destroy();
		Application.LoadLevel("Player Info");
	}

    
    /// <summary>
    /// Shows the highscore screen when representative button is clicked.
    /// </summary>
	public void HighScoreClick()
	{
		Application.LoadLevel("High Score");
	}

     
    /// <summary>
    /// Arbitrary button for exiting the game, but it is not possible to close the program itself.
    /// </summary>
	public void ExitGameClick()
	{
		Application.Quit();
	}

}
