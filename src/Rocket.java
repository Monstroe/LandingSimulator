//***************************************************************
// Class: Rocket
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Math model of the rocket
//
// Attributes:  -currentBody: Body,
//				-commandModule: CommandModule,
//				-fuselage: Fuselage,
//				-engine: Engine,
//				-startingAltitude: int,
//				-xPosition: double,
//				-yPosition: double,
//				-xVelocity: double,
//				-yVelocity: double,
//				-rotationAngleInDegrees: double,
//				-queuedRotationAngleInDegrees: double
// 
// Methods: +calculateGravitationalAcceleration(): double,
//			+calculateCircularOrbitSpeed(): double,
//			+calculateThrustAcceleration(double): double,
//			+calculateVelocity(double): void,
//			+updatePosition(double): void,
//			+updateRotation(): void,
//			+calculateDistanceToSurface(): double,
//			+calculateSpeed(): double,
//			+hasCollidedWithSurface(): boolean,
//			+checkCollisionWithSurface(): double,
//			+calculateAngleBetweenGravityAndHorizontal(): double,
//			+rotate(double): void
//			+clampRotation(double): double,
//			+calculateColliderPoints(): double[][]
//
//**************************************************************

public class Rocket {
	
	private Body currentBody;
	
	private CommandModule commandModule;
	private Fuselage fuselage;
	private Engine engine;
	
	private int startingAltitude = 250;
	
	private double xPosition, yPosition;
	private double xVelocity, yVelocity;
	
	private double rotationAngleInDegrees = 0.0; //Zero when horizontal and facing to the right (like unit circle)
	private double queuedRotationAngleInDegrees = 0.0; //Rotation which the user has inputed
	
	public double calculateGravitationalAcceleration() {
		return currentBody.gravitationalConstant * currentBody.getMass() / Math.pow(calculateDistanceToSurfaceOfBody() + currentBody.getRadius(), 2);
	}
	
	public double calculateCircularOrbitSpeed() {
		return Math.sqrt(currentBody.gravitationalConstant * currentBody.getMass() / (calculateDistanceToSurfaceOfBody() + currentBody.getRadius()));  
	}
	
	public double calculateThrustAcceleration(double timeStep) {
		double acceleration = 0;
		if(fuselage.getFuelLevel() > 0) {
			acceleration = engine.getCurrentThrust() / (commandModule.getMass() + fuselage.getMass() + engine.getMass());
		}
		fuselage.lowerFuelLevel(timeStep, engine.getThrustPercent()); //Only lowers if thrustPercent != 0.0
		
		return acceleration;
	}
	
	public void calculateVelocity(double timeStep) { //CHANGE TO 'updateVelocity()'
		xVelocity -= (calculateGravitationalAcceleration() * Math.cos(Math.toRadians(calculateAngleBetweenGravityAndHorizontal()))
				- calculateThrustAcceleration(timeStep) * Math.cos(Math.toRadians(rotationAngleInDegrees))) * timeStep;
		yVelocity -= (calculateGravitationalAcceleration() * Math.sin(Math.toRadians(calculateAngleBetweenGravityAndHorizontal()))
				- calculateThrustAcceleration(timeStep) * Math.sin(Math.toRadians(rotationAngleInDegrees))) * timeStep;
	}
	
	public void updatePosition(double timeStep) {
		calculateVelocity(timeStep);
		
		xPosition += xVelocity * timeStep;
		yPosition += yVelocity * timeStep;
	}
	
	public void updateRotation() {
		rotationAngleInDegrees = clampRotation(queuedRotationAngleInDegrees + calculateAngleBetweenGravityAndHorizontal() - 90);
	}
	
	public double calculateDistanceToSurfaceOfBody() {
		return Math.sqrt(xPosition * xPosition + yPosition * yPosition) - currentBody.getRadius();
	}
	
	public double calculateSpeed() {
		return Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);  
	}
	
	public boolean hasCollidedWithSurface() {
		boolean collided = false;
		double colliderPoints[][] = calculateColliderPoints();
		
		for(int i = 0; i < colliderPoints.length; i++) {
			double magnitude = Math.sqrt((xPosition + colliderPoints[i][0])*(xPosition + colliderPoints[i][0]) + (yPosition + colliderPoints[i][1])*(yPosition + colliderPoints[i][1]));
			
			if(magnitude <= currentBody.getRadius()) {
				collided = true;
				//System.out.println("Collider Point: " + i); //DEBUG
				break;
			}
		}
		
		return collided;
	}
	
	private double calculateAngleBetweenGravityAndHorizontal() {
		double insideAngle = Math.toDegrees(Math.atan(yPosition / xPosition));
		double angle = -1;
		
		if(yPosition >= 0) {
			if(xPosition >= 0) { //Quadrant 1
				angle = insideAngle;
			}
			else { //Quadrant 2
				angle = 180 + insideAngle;
			}
		}
		else {
			if(xPosition < 0) { //Quadrant 3
				angle = 180 + insideAngle;
			}
			else { //Quadrant 4
				angle = 360 + insideAngle;
			}
		}
		return angle;
	}
	
	public void rotate(double angleInDegrees) {
		queuedRotationAngleInDegrees = clampRotation(queuedRotationAngleInDegrees + angleInDegrees);
	}
	
	private double clampRotation(double rotationAngleInDegrees) {
		if(rotationAngleInDegrees >= 360) {
			rotationAngleInDegrees = rotationAngleInDegrees - 360;
		}
		else if(rotationAngleInDegrees <= 0) {
			rotationAngleInDegrees = 360 + rotationAngleInDegrees;
		}
		
		return rotationAngleInDegrees;
	}
	
	public double[][] calculateColliderPoints() {
		double[][] colliderPoints = new double[6][2];
		
		//0th position checks the center
		
		//Fuselage Front Upper
		colliderPoints[1][0] = fuselage.getHeight()/2 * Math.cos(Math.toRadians(queuedRotationAngleInDegrees));
		colliderPoints[1][1] = Math.sqrt((fuselage.getHeight()/2)*(fuselage.getHeight()/2) + (fuselage.getWidth()/2)*(fuselage.getWidth()/2)) * Math.sin(Math.atan(fuselage.getWidth()/fuselage.getHeight()) + Math.toRadians(queuedRotationAngleInDegrees));
		
		//Fuselage Front Lower
		colliderPoints[2][0] = fuselage.getHeight()/2 * Math.cos(Math.toRadians(queuedRotationAngleInDegrees));
		colliderPoints[2][1] = -Math.sqrt((fuselage.getHeight()/2)*(fuselage.getHeight()/2) + (fuselage.getWidth()/2)*(fuselage.getWidth()/2)) * Math.sin(Math.atan(fuselage.getWidth()/fuselage.getHeight()) - Math.toRadians(queuedRotationAngleInDegrees));
		
		//Fuselage Back Lower
		colliderPoints[3][0] = -(fuselage.getHeight()/2 * Math.cos(Math.toRadians(queuedRotationAngleInDegrees)));
		colliderPoints[3][1] = -Math.sqrt((fuselage.getHeight()/2)*(fuselage.getHeight()/2) + (fuselage.getWidth()/2)*(fuselage.getWidth()/2)) * Math.sin(Math.atan(fuselage.getWidth()/fuselage.getHeight()) + Math.toRadians(queuedRotationAngleInDegrees));
				
		//Fuselage Back Upper
		colliderPoints[4][0] = -(fuselage.getHeight()/2 * Math.cos(Math.toRadians(queuedRotationAngleInDegrees)));
		colliderPoints[4][1] = Math.sqrt((fuselage.getHeight()/2)*(fuselage.getHeight()/2) + (fuselage.getWidth()/2)*(fuselage.getWidth()/2)) * Math.sin(Math.atan(fuselage.getWidth()/fuselage.getHeight()) - Math.toRadians(queuedRotationAngleInDegrees));
		
		//Engine Back
		colliderPoints[5][0] = -(engine.getHeight() + fuselage.getHeight()/2) * Math.cos(Math.toRadians(queuedRotationAngleInDegrees));
		colliderPoints[5][1] = -(engine.getHeight() + fuselage.getHeight()/2) * Math.sin(Math.toRadians(queuedRotationAngleInDegrees));
		
		return colliderPoints;
	}

	//Getters/Setters
	public Body getCurrentBody() {
		return currentBody;
	}

	public void setCurrentBody(Body currentBody) {
		this.currentBody = currentBody;
	}

	public CommandModule getCommandModule() {
		return commandModule;
	}

	public void setCommandModule(CommandModule commandModule) {
		this.commandModule = commandModule;
	}

	public Fuselage getFuselage() {
		return fuselage;
	}

	public void setFuselage(Fuselage fuselage) {
		this.fuselage = fuselage;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public double getXPosition() {
		return xPosition;
	}

	public void setXPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	public double getYPosition() {
		return yPosition;
	}

	public void setYPosition(double yPosition) {
		this.yPosition = yPosition;
	}

	public double getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}
	
	public int getStartingAltitude() {
		return startingAltitude;
	}

	public void setStartingAltitude(int startingAltitude) {
		this.startingAltitude = startingAltitude;
	}

	public double getRotationAngleInDegrees() {
		return rotationAngleInDegrees;
	}

	public void setRotationAngleInDegrees(double rotationAngleInDegrees) {
		this.rotationAngleInDegrees = rotationAngleInDegrees;
	}

	public double getQueuedRotationAngleInDegrees() {
		return queuedRotationAngleInDegrees;
	}

	public void setQueuedRotationAngleInDegrees(double queuedRotationAngleInDegrees) {
		this.queuedRotationAngleInDegrees = queuedRotationAngleInDegrees;
	}

}
