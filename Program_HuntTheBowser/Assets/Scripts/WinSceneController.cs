using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class WinSceneController : MonoBehaviour 
{
    /// <summary>
    /// Allows the user to view their score for their game on click.
    /// </summary>
    public void ReturnToMainMenu()
    {
        Application.LoadLevel("High Score");
    }
}
