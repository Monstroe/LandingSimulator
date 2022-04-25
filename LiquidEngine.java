//***************************************************************
// Class: LiquidEngine
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Controls thrust of liquid engines; has greater control versus solid engines
//
// Attributes:  
// 
// Methods: +deactivate(): void
//			+throttle(double): void
//
//**************************************************************

import javafx.scene.paint.Color;

public class LiquidEngine extends Engine {

	public LiquidEngine(double newMass, double newMaxThrust) {
		super(newMass, newMaxThrust);
	}
	
	public LiquidEngine(double newMass, double newWidth, double newHeight, String newName, Color newColor, double newMaxThrust) {
		super(newMass, newWidth, newHeight, newName, newColor, newMaxThrust);
	}
	
	@Override
	public void deactivate() {
		super.setCurrentThrust(0.0);
		super.setThrustPercent(0.0);
	}
	
	@Override
	public void throttle(double changeInThrustPercent) {
		super.setThrustPercent(Math.max(0, Math.min(1, super.getThrustPercent() + changeInThrustPercent)));
		super.setCurrentThrust(super.getMaxThrust() * super.getThrustPercent());
	}
	
}
