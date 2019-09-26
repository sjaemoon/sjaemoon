using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class GameSceneController : MonoBehaviour
{
	private GameControl controller;

	public Button[] doors;
	public GameObject door;
	public Text currentRoom;
	public Text turnNumber;
	public Text fireballNumber;
	public Text coinNumber;
	public Text warning;
	public Text comment;
	public Button shootArrow;	
	private bool IsShooting;
    public Text selectRoomToShoot;
	public Button buyArrow;	
	public Button buySecret;
	public GameObject bowser;
	public GameObject turtlebat;
	public GameObject bananapit;
	public GameObject bowserComponent;
	public GameObject turtleComponent;
	public GameObject bananaComponent;
	public GameObject playerOption;
	public Text cheat; // for checking code only

	// Use this for initialization
	void Start()
	{
        //Allows use of the same controller in every scene controllers
		this.controller = Maintained.Instance.GameControl;

        // Hides unnecessary objects, and shows necessary objects
        SetScene(); 
        // Shows the labels for the player options
		PlayerOption();
        // Starts with the beginning information for turn #, fireball #, and coin#
        Information();
        // Shows which doors are available in the first room
		PossibleExits(); 
	}

	// Update is called once per frame
	void Update()
	{
        // Updates the information according to the room and player action
        Information();
        //Shows which doors are available in the scene
        PossibleExits();
        // Shows hazard or wumpus and directs the user to 	
		Danger();	

        /* Shows where the bowser and the hazards are, in order to 
        quickly check for the correctness of the code */
		this.cheat.text = this.controller.Cheat();

        // Checks for game state
		switch (this.controller.GetCurrentState())
		{
			case GameControl.State.Game:
				break;
			case GameControl.State.TitleScreen:
				Application.LoadLevel("Main Menu");
				break;
			case GameControl.State.Trivia:
				Application.LoadLevel("Trivia");
				break;
			case GameControl.State.Lose:
				Application.LoadLevel("Lose");
				break;
			case GameControl.State.Win:
				Application.LoadLevel("Win");
				break;
		}
	}

    /// <summary>
    /// Sets the scene with every component showing, except for the bowser's and the hazard's components.
    /// </summary>
	private void SetScene()
	{
        this.selectRoomToShoot.gameObject.SetActive(false);
		this.bowserComponent.SetActive(false); 
		this.turtleComponent.SetActive(false);
		this.bananaComponent.SetActive(false);
	}

    /// <summary>
    /// Labels the button objects, and ables them to hide or show.
    /// </summary>
	private void PlayerOption()
	{
		// button for buying fireball
		this.buyArrow.gameObject.GetComponentInChildren<Text>().text = "Buy Fireball";
		// button for buying secret
		this.buySecret.gameObject.GetComponentInChildren<Text>().text = "Buy Secret";
		// button for throwing fireball
		this.shootArrow.gameObject.GetComponentInChildren<Text>().text = "Throw Fireball";
	}
    
    /// <summary>
    /// Updates the information according to the room and player action.
    /// </summary>
	private void Information()
	{
        // updates current room #
        this.currentRoom.text = string.Format("{0}", this.controller.GetRoomNumber());

        // below belong in the info board in the top left of the game scene
        // updates # of turns the user took
		this.turnNumber.text = string.Format("Turn #: {0}", this.controller.Turns());
        // udpates the # of arrows available
		this.fireballNumber.text = string.Format("Number of Fireballs: {0}", this.controller.Arrows()); 
        // updates the # of coins available
		this.coinNumber.text = string.Format("Number of Coins: {0}", this.controller.Coins()); 

        // shows the warning if bowser or hazards are in the nearby room
		string[] warning = this.controller.GetWarning();
        // shows the secret after the user buys the secret and successfully solves trivia
		string secret = this.controller.GetMessage();
		this.warning.text = string.Format("Hint(s): {0}\n{1}\n{2}\n{3}",
			warning[0], warning[1], warning[2], secret);
	}


    /// <summary>
    /// Gets the exits from the game controller depending on the room.
    /// </summary>
    /// <returns>exits that are exitable</returns>
	private int[] GetDoorsFromController()
	{
        int[] exits = this.controller.GetExits();
		return exits;
	}

    /// <summary>
    /// Sorts which exits are available and unavailable
    /// </summary>
	private void PossibleExits()
	{
		int[] exits = GetDoorsFromController();
		for (int i = 0; i < exits.Length; i++)
		{
			if (exits[i] == 0) // designates unavailbe door and hides them
			{
				this.doors[i].gameObject.SetActive(false);
			}
			else // otherwise show the possible rooms to move to
			{
				this.doors[i].gameObject.SetActive(true);
				this.doors[i].gameObject.GetComponentInChildren<Text>().text = 
                    string.Format("{0}", exits[i]);
			}
		}
	}


	bool waitingOnInvoke = false; // disables update, so the invoke does not update.
    /// <summary>
    ///  When danger shows up, all buttons are deactivated, and appropriate messages and pictures show up.
    ///  The message invokes (gets put on pause), so the user can read the message before proceeding.
    /// </summary>
	private void Danger()
	{
		if (!waitingOnInvoke)
		{
            // Order of priority: Bowser(Wumpus), then Turtle(Bat), then Banana(Pit)
            // If bowser is in the room...
			if (this.controller.GetHazard() == "BOWSER")
			{
				this.bowserComponent.SetActive(true); // show bowser
				this.door.gameObject.SetActive(false); // hide door
				this.playerOption.SetActive(false); // hide player option
				this.comment.text = "Bowser has appeared, proceeding to Trivia..."; // show message to warn user
				waitingOnInvoke = true;
				Invoke("MoveOnWithTrivia", 3); // go to trivia after 3 seconds
			}
            // If turtle is in the room...
			else if (this.controller.GetHazard() == "TURTLE")
			{
				this.turtleComponent.SetActive(true); // shows turtle
				this.door.gameObject.SetActive(false);
				this.playerOption.SetActive(false);
				this.comment.text = "Flying turtle has captured you, moving to a random room...";
				waitingOnInvoke = true;
				Invoke("MoveOnNoTrivia", 3); // move to a random room after 3 seconds
			}
            // If banana is in the room...
			else if (this.controller.GetHazard() == "BANANA")
			{
				this.bananaComponent.SetActive(true); // shows banana
				this.door.gameObject.SetActive(false);
				this.playerOption.SetActive(false);
				this.comment.text = "You are slipping on the banana, proceeding to Trivia...";
				waitingOnInvoke = true;
				Invoke("MoveOnWithTrivia", 3);
			}
		}
	}

    /// <summary>
    /// Invoke message for Bowser and Pit.
    /// </summary>
	private void MoveOnWithTrivia()
	{
		this.comment.text = "Moving...";
		ProceedToTrivia(); // Calls the action to actually advance
		this.waitingOnInvoke = false;
	}

    /// <summary>
    /// Invoke message for Turtle.
    /// </summary>
	private void MoveOnNoTrivia()
	{
		this.comment.text = "Moving...";
		RandomlyLand(); // Calls the action to actually advance
		this.waitingOnInvoke = false;
	}


    /// <summary>
    /// After invoke takes place, make all the buttons reappear again, and goes to the "Trivia" scene.
    /// </summary>
	private void ProceedToTrivia()
	{
		this.controller.Proceed(); // tell gamecontroller that the bowser or hazard has appeared
		this.bowserComponent.SetActive(false); // hide bowser again
		this.bananaComponent.SetActive(false); // hide banana again
		this.door.SetActive(true); // show door again
		this.playerOption.SetActive(true); // show player option again
		this.comment.text = ""; // hide the notification message
		Application.LoadLevel("Trivia"); // move scene
	}

    /// <summary>
    /// After invoke takes place, make all the buttons reappear again, and goes to a random move.
    /// </summary>
	private void RandomlyLand()
	{
		this.controller.Proceed(); 
		this.turtleComponent.SetActive(false); // hides turtle again
		this.door.SetActive(true);
		this.playerOption.SetActive(true);
		this.comment.text = "";
		this.playerOption.SetActive(true);
	}

    /// <summary>
    /// Has the "shooting fireball" on standby, and activates when button is pressed.
    /// </summary>
	public void StartShooting()
	{
        this.IsShooting = true;
        this.selectRoomToShoot.gameObject.SetActive(true); // show "select room to shoot to"
	}

    /// <summary>
    /// If the "shoot fireball" button is pressed, the door click chooses which door to shoot the arrow to.
    /// If the door button is pressed without pressing the "shoot fireball," it moves the player into that room.
    /// </summary>
    /// <param name="whichDoor">door chosen by the user</param>
	public void DoorClick(int whichDoor)
	{
		if (this.IsShooting) // when shooting
		{
			this.controller.ShootArrow(whichDoor);
            this.selectRoomToShoot.gameObject.SetActive(false); // hide
			this.IsShooting = false; // player is not shooting anymore
		}
		else // when moving
		{
			this.controller.ChangeRooms(whichDoor);
		}
	}

    /// <summary>
    /// Activates Trivia when attempting to purchase fireball
    /// </summary>
	public void BuyFireballClick()
	{
		this.controller.PurchaseArrow();
		Application.LoadLevel("Trivia");
	}

    /// <summary>
    /// Activates Trivia when attempting to purchase secret
    /// </summary>
	public void BuySecretClick()
	{
		this.controller.PurchaseSecret();
		Application.LoadLevel("Trivia");
	}

    /// <summary>
    /// Allows the player to quit the game, and go to the main menu during the game
    /// </summary>
	public void ReturnToMenuClick()
	{
		Application.LoadLevel("Main Menu");
	}
}