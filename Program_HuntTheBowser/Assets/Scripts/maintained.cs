/*************************************************************************************
 * File: Maintained.cs
 * Description: Singleton class implementation to maintain data across Unity scenes.
 * This is meant to be used by UI and ONLY UI.
 * Author: Jonathan Wills
 *************************************************************************************/

using System;
using Wumpus6B;

/// <summary>
/// Uninheritable Singleton class for maintaining data across scenes in Unity.
/// </summary>
/// <remarks>
/// The reason for doing this rather than just keeping a static around is 
/// that it is only initially created if needed.
/// NOTE: Globals are not to be used lightly as they will stick around taking up 
/// memory for the life of the program. Limit how much you put in here such 
/// as only the essentials. This means I don't expect to see anything other than
/// GameControl in here.
/// </remarks>
public sealed class Maintained
{
    /// <summary>
    /// The staticly maintained instance. This is what makes it possible to switch
    /// scenes in Unity and not lose the data.
    /// </summary>
	private static Maintained instance;

    /// <summary>
    /// Gets the game control.
    /// </summary>
    /// <value>The game control.</value>
	public GameControl GameControl
	{
		get;
		private set;
	}

    /// <summary>
    /// Privately initializes a new instance of the <see cref="Maintained"/> class.
    /// This restricts anyone else from creating this class other than the class
    /// itself.
    /// </summary>
	private Maintained() 
	{
		this.GameControl = new GameControl();
	}

    /// <summary>
    /// Gets the reference to the existing SINGLE instance and if none exists creates it.
    /// </summary>
    /// <value>The instance.</value>
    /// <example>
    /// // Move player through door 0.
    /// Maintained.Instance.GameControl.MovePlayer(0)
    /// </example>
	public static Maintained Instance
	{
		get
		{	
            // If we do not have one...
			if (instance == null)
			{
                // Make one.
				instance = new Maintained();
			}

			return instance;
		}
	}

    /// <summary>
    /// Not standard for a Singleton but allows you to delete the singleton to create an entirely new single instance.
    /// </summary>
    public static void Destroy()
    {
        instance = null;
    }
}
