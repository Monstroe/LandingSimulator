//***************************************************************
// Class: GameManager
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Manages the simulation
//
// Attributes:  -player: Rocket
//				-timeStep: double
//				-simulationPane: SimulationPane
// 
// Methods: +simulate(): void
//			+resetMathModel(): void
//			+hasWon(double, double, double): boolean
//
//**************************************************************

public class GameManager {
	
	public final double timeStep; //Public since it can't be changed
	private SimulationPane simulationPane;
	
	private Rocket player;
	
	public GameManager(SimulationPane simPane) {
		simulationPane = simPane;
		timeStep = 1 / (double)simulationPane.framesPerSecond;
		
		player = new Rocket();
	}

	public void simulate() {
		player.updatePosition(timeStep);
		player.updateRotation();
		
		simulationPane.displayPosition();
		simulationPane.displayRotation();
		simulationPane.displaySpeed();
		simulationPane.displayAltitude();
		simulationPane.displayThrustPercent();
		simulationPane.updateFuelBar();
		
		if(player.hasCollidedWithSurface()) {
			simulationPane.stopSimulation();
		}
		
		//DEBUG
		//System.out.println("xPos: " + player.getXPosition() + ", yPos: " + player.getYPosition());
		//System.out.println("xVel: " + player.getXVelocity() + ", yVel: " + player.getYVelocity());
		//System.out.println("Accel: " + player.calculateGravitationalAcceleration() + "Dist: " + player.calculateDistanceToSurfaceOfBody());
		//System.out.println("Speed: " + player.calculateSpeed());
		//System.out.println("Rotation: " + player.getRotationAngleInDegrees() + ", Queued Rotation: " + player.getQueuedRotationAngleInDegrees());
	}
	
	public void resetMathModel() {
		player.setXPosition(0);
		player.setYPosition(player.getStartingAltitude() + player.getCurrentBody().getRadius());
		player.setXVelocity(-player.calculateCircularOrbitSpeed());
		player.setYVelocity(0);
		player.setRotationAngleInDegrees(0);
		player.setQueuedRotationAngleInDegrees(0);
		
		player.getFuselage().resetFuelLevel();
		player.getEngine().reset();
	}
	
	public boolean hasWon(double minRotate, double maxRotate, double maxSpeed) {
		boolean won = false;
		if(player.getRotationAngleInDegrees() > minRotate && player.getRotationAngleInDegrees() < maxRotate) {
			if(player.calculateSpeed() < maxSpeed) {
				won = true;
			}
		}
		return won;
	}

	//Getters/Setters
	public Rocket getPlayer() {
		return player;
	}

	public void setPlayer(Rocket player) {
		this.player = player;
	}

}
