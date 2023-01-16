//***************************************************************
// Class: CommandModule
// Author: Monstroe
// Modified: 4/08/2022
//
// Purpose: Controls the rotation speed of the rocket
//
// Attributes:  -rotationSpeed: double
// 
// Methods: 
//
//**************************************************************

import javafx.scene.paint.Color;

public class CommandModule extends Part {

	private double rotationSpeed;
	
	public CommandModule(double newMass, double newRotationSpeed) {
		super(newMass);
		rotationSpeed = newRotationSpeed;
	}
	
	public CommandModule(double newMass, double newWidth, double newHeight, String newName, Color newColor, double newRotationSpeed) {
		super(newMass, newWidth, newHeight, newName, newColor);
		rotationSpeed = newRotationSpeed;
	}

	//Getters/Setters
	public double getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

}
