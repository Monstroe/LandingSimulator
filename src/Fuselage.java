//***************************************************************
// Class: Fuselage
// Author: Monstroe
// Modified: 4/08/2022
//
// Purpose: Control fuel level and fuel depletion rate.
//
// Attributes:  -maxFuelLevel double
//				-fuelLevel: double
//				-depletionRate: double
// 
// Methods: +lowerFuelLevel(): void
//			+resetFuelLevel(): void
//
//**************************************************************

import javafx.scene.paint.Color;

public class Fuselage extends Part {

	private double maxFuelLevel;
	private double fuelLevel;
	private double depletionRate;
	
	public Fuselage(double newMass, double newMaxFuelLevel, double newDepletionRate) {
		super(newMass);
		maxFuelLevel = newMaxFuelLevel;
		fuelLevel = maxFuelLevel;
		depletionRate = newDepletionRate;
	}
	
	public Fuselage(double newMass, double newWidth, double newHeight, String newName, Color newColor, double newMaxFuelLevel, double newDepletionRate) {
		super(newMass, newWidth, newHeight, newName, newColor);
		maxFuelLevel = newMaxFuelLevel;
		fuelLevel = maxFuelLevel;
		depletionRate = newDepletionRate;
	}
	
	public void lowerFuelLevel(double timeStep, double thrustPercent) {
		fuelLevel = Math.max(0, fuelLevel - depletionRate * timeStep * thrustPercent);
	}
	
	public void resetFuelLevel() {
		fuelLevel = maxFuelLevel;
	}
	
	//Getters/Setters
	public double getMaxFuelLevel() {
		return maxFuelLevel;
	}

	public void setMaxFuelLevel(double maxFuelLevel) {
		this.maxFuelLevel = maxFuelLevel;
	}

	public double getFuelLevel() {
		return fuelLevel;
	}

	/*public void setFuelLevel(double fuelLevel) { //NO SETTER FOR FUEL LEVEL
		this.fuelLevel = fuelLevel;
	}*/

	public double getDepletionRate() {
		return depletionRate;
	}

	public void setDepletionRate(double depletionRate) {
		this.depletionRate = depletionRate;
	}

}
