//***************************************************************
// Class: Body
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Contains the attributes of a planetary body.
//
// Attributes:  -mass: double
//				-radius: double
//				-surfaceGravity: double
//				-color: Color
// 
// Methods: +recalculateMass(): void
//			+recalculateRadius(): void
//			+recalculateSurfaceGravity(): void
//
//**************************************************************

import javafx.scene.paint.Color;

public class Body {
	
	final double gravitationalConstant = 6.67408 * Math.pow(10, -11);
	
	private double mass = 0;
	private double radius = 0;
	private double surfaceGravity = 0;
	
	private Color color = Color.WHITE;
	
	public void recalculateMass() {
		mass = surfaceGravity * Math.pow(radius, 2) / gravitationalConstant;
	}
	
	public void recalculateRadius() {
		radius = Math.sqrt(gravitationalConstant * mass / surfaceGravity);
	}
	
	public void recalculateSurfaceGravity() {
		surfaceGravity = gravitationalConstant * mass / Math.pow(radius, 2);
	}
	
	//Getters/Setters
	public double getMass() {
		return mass;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
		
		if(radius != 0) {
			recalculateSurfaceGravity();
		}
		else if(surfaceGravity != 0) {
			recalculateRadius();
		}
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
		
		if(mass != 0) {
			recalculateSurfaceGravity();
		}
		else if(surfaceGravity != 0) {
			recalculateMass();
		}
	}
	
	public double getSurfaceGravity() {
		return surfaceGravity;
	}
	
	public void setSurfaceGravity(double surfaceGravity) {
		this.surfaceGravity = surfaceGravity;
		
		if(mass != 0) {
			recalculateRadius();
		}
		else if(radius != 0) {
			recalculateMass();
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
