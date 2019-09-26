using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class PlayerInfoController : MonoBehaviour 
{
	private GameControl controller;

	public Button StartButton; 
	public Text playerNameText;
	public Text caveNumberText;
	public Text errorMessageText;

	// Use this for initialization
	void Start () 
	{
        //Allows use of the same controller in every scene controllers
		this.controller = Maintained.Instance.GameControl;
	}
	
	// Update is called once per frame
	void Update () 
	{
		CheckNameAndCave(); // Checks if the player name and cave are available
	}

    /// <summary>
    /// Checks if the player name and cave  are available.
    /// </summary>
	public void CheckNameAndCave()
	{
        // user has to type something in the player name input field
		bool validName = this.playerNameText.GetComponentInChildren<Text>().text.Length >= 1;
        // user has to type available cave as an integer (1 to 5) in the cave input field
		int caveNum;
		bool validCave = int.TryParse(this.caveNumberText.GetComponentInChildren<Text>().text, out caveNum)
			&& caveNum > 0 && caveNum < 6;
        // show error message if the cave does not exist
		if (!validCave)
		{
			errorMessageText.text = "*Enter appropriate cave number...";
		}
        // Unless the user types in the appropriate field, user cannot access the actual game
		this.StartButton.interactable = (validName && validCave);
	}

    /// <summary>
    /// Allows the user to return to main menu on click.
    /// </summary>
    public void ReturnToMenuClick()
    {
        Application.LoadLevel("Main Menu");
    }

    /// <summary>
    /// Allows the user to start the game.
    /// </summary>
	public void StartGameClick()
	{
		string playerName = this.playerNameText.GetComponentInChildren<Text>().text; // saves player name
		int caveNumber = int.Parse(this.caveNumberText.GetComponentInChildren<Text>().text); // saves cave number
		this.controller.StartGame(caveNumber, playerName); // controller is able to use the info for later
		Application.LoadLevel("Game");
	}
}
