//***************************************************************
// Class: Part
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Base class for every part.
//
// Attributes:  -mass: double
//				-width: double
//				-height: double
//				-name: String
//				-color: Color
// 
// Methods: 
//
//**************************************************************

import javafx.scene.paint.Color;

public class Part {
	
	private double mass;
	private double width;
	private double height;
	
	private String name;
	private Color color;
	
	public Part(double newMass) {
		mass = newMass;
	}
	
	public Part(double newMass, double newWidth, double newHeight, String newName, Color newColor) {
		mass = newMass;
		width = newWidth;
		height = newHeight;
		name = newName;
		color = newColor;
	}

	//Getters/Setters
	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
