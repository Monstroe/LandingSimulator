//***************************************************************
// Class: Engine
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Base engine class
//
// Attributes:  -maxThrust: double
//				-currentThrust: double
//				-thrustPercent: double
// 
// Methods: +activate(): void
//			+deactivate(): void
//			+throttle(double): void
//			+reset(): void
//
//**************************************************************

import javafx.scene.paint.Color;

public class Engine extends Part {
	
	private double maxThrust;
	private double currentThrust;
	private double thrustPercent = 0.0;
	
	public Engine(double newMass, double newMaxThrust) {
		super(newMass);
		maxThrust = newMaxThrust;
	}
	
	public Engine(double newMass, double newWidth, double newHeight, String newName, Color newColor, double newMaxThrust) {
		super(newMass, newWidth, newHeight, newName, newColor);
		maxThrust = newMaxThrust;
	}
	
	public void activate() {
		currentThrust = maxThrust;
		thrustPercent = 1.0;
	}
	
	public void deactivate() {
		//In Children
	}
	
	public void throttle(double changeInThrustPercent) {
		//In Children
	}
	
	public void reset() { //ONLY CALL WHEN RESETTING SIMULATION
		currentThrust = 0.0;
		thrustPercent = 0.0;
	}
	
	//Getters/Setters
	public double getMaxThrust() {
		return maxThrust;
	}
	
	public void setMaxThrust(double maxThrust) {
		this.maxThrust = maxThrust;
	}
	
	public double getCurrentThrust() {
		return currentThrust;
	}
	
	protected void setCurrentThrust(double currentThrust) { //PROTECTED SETTER FOR CURRENT THRUST
		this.currentThrust = currentThrust;
	}
	
	public double getThrustPercent() {
		return thrustPercent;
	}

	protected void setThrustPercent(double thrustPercent) { //PROTECTED SETTER FOR THRUST PERCENT
		this.thrustPercent = thrustPercent;
	}
	
}
