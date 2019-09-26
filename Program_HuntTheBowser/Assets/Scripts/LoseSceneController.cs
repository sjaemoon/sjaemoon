using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class LoseSceneController : MonoBehaviour 
{
    /// <summary>
    /// Allows the user to go back to the main menu.
    /// </summary>
	public void ReturnToMainMenu()
	{
		Application.LoadLevel("Main Menu");
	}
}
