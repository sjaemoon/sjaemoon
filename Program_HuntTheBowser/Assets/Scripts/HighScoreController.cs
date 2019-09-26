using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class HighScoreController : MonoBehaviour 
{
	private GameControl controller;
    public Text rank;

	// Use this for initialization
	void Start () 
	{
		this.controller = Maintained.Instance.GameControl;
        HighScoreData();
	}

    /// <summary>
    /// Gets the information about the highscore, then outputs the text appropriately for the screen.
    /// </summary>
    private void HighScoreData()
    {
        string[] Rank = this.controller.GetHighScore();
        this.rank.text = string.Format("{0}\n{1}\n{2}\n{3}\n{4}\n{5}\n{6}\n{7}\n{8}\n{9}",
            Rank[0], Rank[1], Rank[2], Rank[3], Rank[4], Rank[5], Rank[6], Rank[7], Rank[8], Rank[9]);
    }

    /// <summary>
    /// Allows the user to go the main menu.
    /// </summary>
	public void ReturnToMenuClick()
	{
		this.controller.ViewTitleScreen();
		Application.LoadLevel("Main Menu");
	}
}
