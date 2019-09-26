using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using Wumpus6B;

public class TriviaController : MonoBehaviour 
{
	private GameControl controller;
	private bool needNewQuestion;

	public Text question;
	public Text optionA;
	public Text optionB;
	public Text optionC;
	public Text optionD;
	public Text feedback;
	public Button A;
	public Button B;
	public Button C;
	public Button D;
    public Text funFact;

	// Use this for initialization
	void Start () 
	{
        //Allows use of the same controller in every scene controllers
		this.controller = Maintained.Instance.GameControl;
        // Show appropriate question
		ShowQuestion();
	}
	
	// Update is called once per frame
	void Update () 
	{
        // Checks if the user needs more questions
		if (this.needNewQuestion)
			ShowQuestion();
		switch (this.controller.GetCurrentState())
		{
			case GameControl.State.Trivia:
				break;
			case GameControl.State.Game:
				Application.LoadLevel("Game");
				break;
			case GameControl.State.Lose:
				Application.LoadLevel("Lose");
				break;
		}
	}

    /// <summary>
    /// Shows new trivia questions as long as there is more to show.
    /// </summary>
	private void ShowQuestion()
	{
		string[] triviaData = this.controller.GetQuestion(); // reads the trivia
		this.question.text = triviaData[0]; // token 1 = question
		this.optionA.text = triviaData[1]; // token[2+] = different answers
		this.optionB.text = triviaData[2];
		this.optionC.text = triviaData[3];
		this.optionD.text = triviaData[4];
		this.needNewQuestion = false;
	}

    /// <summary>
    /// Returns if the user answered the question right or wrong.
    /// </summary>
    /// <param name="letter">the answer choice chosen by the user</param>
	public void CheckAnswer(string letter)
	{
		// check to make sure length is at least 1
		if (letter.Length >= 1)
		{
            if (this.controller.CheckAnswer(letter[0]))
            {
                // correct
                this.feedback.text = "CORRECT!";
            }
            else
            {
                // incorrect
                this.feedback.text = "WRONG!";
            }
			Invoke("QuestionDone", 1); // wait one second, and then do end-of-question clean-up
		}
	}
 
    /// <summary>
    /// Check game state - display new question or change to different scene based on that.
    /// </summary>
	private void QuestionDone()
	{
		this.feedback.text = "Next...";
		this.needNewQuestion = true;
	}
}
