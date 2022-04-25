//***************************************************************
// Class: Solid Engine
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Controls thrust of solid engines.
//
// Attributes:  
// 
// Methods: +deactivate(): void
//			+throttle(double): void
//
//**************************************************************

import javafx.scene.paint.Color;

public class SolidEngine extends Engine {

	public SolidEngine(double newMass, double newMaxThrust) {
		super(newMass, newMaxThrust);
	}
	
	public SolidEngine(double newMass, double newWidth, double newHeight, String newName, Color newColor, double newMaxThrust) {
		super(newMass, newWidth, newHeight, newName, newColor, newMaxThrust);
	}
	
	@Override
	public void deactivate() {
		//Do Nothing
		System.out.println("Cannot deactivate as this is a Solid Engine.");
	}
	
	@Override
	public void throttle(double changeInThrustPercent) {
		//Do Nothing
		System.out.println("Cannot change thrust percent as this is a Solid Engine");
	}
	
	

}
