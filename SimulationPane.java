//***************************************************************
// Class: SimulationPane
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Pane responsible for running the simulation
//
// Attributes:  -framesPerSecond: int
//				-landingSim: LSGUI
//				-rocketDisplayScale: double
//				-bodyDisplayDownscale: double
// 
// Methods: +startSimulation(): void,
//			+stopSimulation(): void,
//			+resetDisplay(): void,
//			+displayPosition(): void,
//			+displayRotation(): void,
//			+displaySpeed(): void,
//			+displayAltitude(): void,
//			+displayThrustPercent(): void,
//			+updateFuelBar(): void,
//			+setActivePane(ActivePane): void,
//			+initialize(): void
//
//**************************************************************

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SimulationPane extends BorderPane {
	
	public final int framesPerSecond = 60; //Public since it can't be changed
	private LSGUI landingSim;
	private double rocketDisplayScale = 1;
	private double bodyDisplayDownscale = 1;
	
	private Circle body;
	
	private HBox rotateAxis;
	private Rectangle fuselage;
	private Polygon commandModule;
	private Polygon engine;
	
	private Timeline paneUpdate;
	
	private HBox statusBar;
	private HBox winBar;
	private Group sideBar;
	
	private Label lblSpeed;
	private Label lblThrustPercent;
	private Label lblAltitude;
	private Label lblWin;
	private Rectangle fuelBarOutline;
	private Rectangle fuelBar;
	
	public SimulationPane(LSGUI lsGUI) {
		landingSim = lsGUI;
		
		setStyle("-fx-background-color: #000000");
	}
	
	public void startSimulation() {
		landingSim.getGameManager().resetMathModel();
		resetDisplay();
		
		initializeDisplayRocket();
		initializeDisplayBody();
		initializeUserInterface();
		
		paneUpdate.play();
		
		//DEBUG
		//System.out.println("Mass: " + landingSim.getGameManager().getPlayer().getCurrentBody().getMass());
		//System.out.println("Radius: " + landingSim.getGameManager().getPlayer().getCurrentBody().getRadius());
		//System.out.println("Surface Gravity: " + landingSim.getGameManager().getPlayer().getCurrentBody().getSurfaceGravity());
	}
	
	public void stopSimulation() {
		paneUpdate.stop();
		if(landingSim.getGameManager().hasWon(70, 110, 10)) {
			lblWin.setText("You Won!");
		}
		else {
			lblWin.setText("You Lost!");
		}
		winBar.setVisible(true);
	}
	
	public void resetDisplay() {
		this.getChildren().clear();
	}
	
	//This is the position method that will be used in the finished project
	public void displayPosition() { //CAMERA CENTERED AROUND ROCKET AND ROTATES WITH ROCKET
		//debugPosition();
		//debugPosition2();
		
		body.setCenterX(getWidth()/2);
		body.setCenterY(getHeight()/2 + (landingSim.getGameManager().getPlayer().calculateDistanceToSurfaceOfBody() + landingSim.getGameManager().getPlayer().getCurrentBody().getRadius())/bodyDisplayDownscale);
		
		rotateAxis.setLayoutX(getWidth()/2 - fuselage.getWidth()/2);
		rotateAxis.setLayoutY(getHeight()/2 - fuselage.getHeight()/2);
	}
	
	//NOT USED
	//This position method is for testing
	public void debugPosition() { //CAMERA CENTERED AROUND ROCKET
		body.setCenterX(getWidth()/2 - landingSim.getGameManager().getPlayer().getXPosition()/bodyDisplayDownscale);
		body.setCenterY(getHeight()/2 + landingSim.getGameManager().getPlayer().getYPosition()/bodyDisplayDownscale);
		
		rotateAxis.setLayoutX(getWidth()/2 - fuselage.getWidth()/2);
		rotateAxis.setLayoutY(getHeight()/2 - fuselage.getHeight()/2);
	}
	
	//NOT USED
	//This position method is for testing
	public void debugPosition2() { //CAMERA CENTERED AROUND BODY
		body.setCenterX(getWidth()/2);
		body.setCenterY(getHeight()/2);
		
		rotateAxis.setLayoutX(getWidth()/2 + landingSim.getGameManager().getPlayer().getXPosition()/10000 - fuselage.getWidth()/2);
		rotateAxis.setLayoutY(getHeight()/2 - landingSim.getGameManager().getPlayer().getYPosition()/10000 - fuselage.getHeight()/2);
	}
	
	//This is the rotation method that is used in the finished project
	public void displayRotation() { //ROTATION WHEN CAMERA IS CENTERED ON ROCKET AND ROTATES WITH ROCKET
		//debugRotation();
		rotateAxis.setRotate(-landingSim.getGameManager().getPlayer().getQueuedRotationAngleInDegrees());
	}
	
	//NOT USED
	//This rotation method is for testing
	public void debugRotation() { //NORMAL ROTATION (Only used with both debugPosition methods)
		rotateAxis.setRotate(-landingSim.getGameManager().getPlayer().getRotationAngleInDegrees());
	}
	
	public void displaySpeed() {
		lblSpeed.setText("Speed: " + (int)landingSim.getGameManager().getPlayer().calculateSpeed() + " m/s");
	}
	
	public void displayAltitude() {
		lblAltitude.setText("Altitude: " + (int)(landingSim.getGameManager().getPlayer().calculateDistanceToSurfaceOfBody()) + " m");
	}
	
	public void displayThrustPercent() {
		lblThrustPercent.setText("Thrust Percet: " + (int)(landingSim.getGameManager().getPlayer().getEngine().getThrustPercent() * 100) + "%");
	}
	
	public void updateFuelBar() {
		fuelBar.setHeight(fuelBarOutline.getHeight() * landingSim.getGameManager().getPlayer().getFuselage().getFuelLevel() / landingSim.getGameManager().getPlayer().getFuselage().getMaxFuelLevel());
	}
	
	//Initialization Methods
	public void initialize() {
		initializeTimeline();
		initializeControls();
	}

	private void initializeDisplayRocket() {
		rotateAxis = new HBox(0);
		rotateAxis.setAlignment(Pos.CENTER);
		
		fuselage = new Rectangle();
		commandModule = new Polygon();
		engine = new Polygon();
		
		fuselage.setWidth(landingSim.getGameManager().getPlayer().getFuselage().getHeight() * rocketDisplayScale);
		fuselage.setHeight(landingSim.getGameManager().getPlayer().getFuselage().getWidth() * rocketDisplayScale);
		fuselage.setFill(landingSim.getGameManager().getPlayer().getFuselage().getColor());
		
		commandModule.getPoints().addAll(new Double[] {
				0.0, 0.0,
				landingSim.getGameManager().getPlayer().getCommandModule().getHeight() * rocketDisplayScale, landingSim.getGameManager().getPlayer().getCommandModule().getWidth()/2 * rocketDisplayScale,
				0.0, landingSim.getGameManager().getPlayer().getCommandModule().getWidth() * rocketDisplayScale
		});
		commandModule.setFill(landingSim.getGameManager().getPlayer().getCommandModule().getColor());
		
		engine.getPoints().addAll(new Double[] {
				0.0, 0.0,
				landingSim.getGameManager().getPlayer().getEngine().getHeight() * rocketDisplayScale, landingSim.getGameManager().getPlayer().getEngine().getWidth()/2 * rocketDisplayScale,
				0.0, landingSim.getGameManager().getPlayer().getEngine().getWidth() * rocketDisplayScale
		});
		engine.setFill(landingSim.getGameManager().getPlayer().getEngine().getColor());		
		
		rotateAxis.getChildren().add(engine);
		rotateAxis.getChildren().add(fuselage);
		rotateAxis.getChildren().add(commandModule);
		
		this.getChildren().add(rotateAxis);
	}
	
	private void initializeDisplayBody() {
		body = new Circle(landingSim.getGameManager().getPlayer().getCurrentBody().getRadius() / bodyDisplayDownscale);
		body.setFill(landingSim.getGameManager().getPlayer().getCurrentBody().getColor());
		
		this.getChildren().add(body);
	}

	private void initializeUserInterface() {
		initializeStatusBar();
		initializeSideBar();
		initializeWinBar();
	}
	
	private void initializeWinBar() {
		winBar = new HBox(60);
		winBar.setAlignment(Pos.CENTER);
		winBar.setPadding(new Insets(10, 10, 10, 10));
		
		lblWin = new Label("You !");
		lblWin.setStyle("-fx-font: 18 Roboto; -fx-text-fill: black");
		Button btnPlayAgain = new Button("Play Again");
		Button btnMainMenu = new Button("Main Menu");
		btnPlayAgain.setOnAction(e -> {
			landingSim.setActivePane(ActivePane.SIMULATIONPANE);
			startSimulation();
		});
		btnMainMenu.setOnAction(e -> {
			landingSim.setActivePane(ActivePane.MENUPANE);
		});
		
		winBar.getChildren().add(btnPlayAgain);
		winBar.getChildren().add(lblWin);
		winBar.getChildren().add(btnMainMenu);
		
		winBar.setVisible(false);
		this.setBottom(winBar);
	}

	private void initializeSideBar() {
		fuelBar = new Rectangle(0, 0, 40, 160);
		fuelBar.setFill(Color.RED);
		
		fuelBarOutline = new Rectangle(fuelBar.getX(), fuelBar.getY(), fuelBar.getWidth(), fuelBar.getHeight());
		fuelBarOutline.setFill(Color.TRANSPARENT);
		fuelBarOutline.setStroke(Color.WHITE);
		
		sideBar = new Group();
		sideBar.getChildren().add(fuelBar);
		sideBar.getChildren().add(fuelBarOutline);
		
		this.setRight(sideBar);
		this.setPadding(new Insets(0, 10, 0, 0));	
	}

	private void initializeStatusBar() {
		statusBar = new HBox(60);
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setPadding(new Insets(10, 10, 10, 10));
		
		lblSpeed = new Label("Speed: ");
		lblSpeed.setStyle("-fx-font: 18 Roboto; -fx-text-fill: white");
		lblAltitude = new Label("Altitude: ");
		lblAltitude.setStyle("-fx-font: 18 Roboto; -fx-text-fill: white");
		lblThrustPercent = new Label("Thrust Percent: ");
		lblThrustPercent.setStyle("-fx-font: 18 Roboto; -fx-text-fill: white");
		
		statusBar.getChildren().add(lblSpeed);
		statusBar.getChildren().add(lblAltitude);
		statusBar.getChildren().add(lblThrustPercent);
		
		this.setTop(statusBar);
	}

	private void initializeTimeline() {
		paneUpdate = new Timeline(new KeyFrame(Duration.seconds(landingSim.getGameManager().timeStep), e -> landingSim.getGameManager().simulate()));
		paneUpdate.setCycleCount(Timeline.INDEFINITE);
	}
	
	private void initializeControls() {
		this.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
				landingSim.getGameManager().getPlayer().rotate(landingSim.getGameManager().getPlayer().getCommandModule().getRotationSpeed() * landingSim.getGameManager().timeStep);
			}
			else if(e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
				landingSim.getGameManager().getPlayer().rotate(-landingSim.getGameManager().getPlayer().getCommandModule().getRotationSpeed() * landingSim.getGameManager().timeStep);
			}
			else if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
				landingSim.getGameManager().getPlayer().getEngine().throttle(10 * landingSim.getGameManager().timeStep);
			}
			else if(e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
				landingSim.getGameManager().getPlayer().getEngine().throttle(-10 * landingSim.getGameManager().timeStep);
			}
			else if(e.getCode() == KeyCode.SPACE) {
				landingSim.getGameManager().getPlayer().getEngine().activate();
			}
			else if(e.getCode() == KeyCode.X) {
				landingSim.getGameManager().getPlayer().getEngine().deactivate();
			}
		});
	}
	
	//Getters/Setters
	public Circle getBody() {
		return body;
	}

	public void setBody(Circle body) {
		this.body = body;
	}
	
	public Rectangle getFuselage() {
		return fuselage;
	}

	public void setFuselage(Rectangle fuselage) {
		this.fuselage = fuselage;
	}

	public Polygon getCommandModule() {
		return commandModule;
	}

	public void setCommandModule(Polygon commandModule) {
		this.commandModule = commandModule;
	}

	public Polygon getEngine() {
		return engine;
	}

	public void setEngine(Polygon engine) {
		this.engine = engine;
	}

	public double getRocketDisplayScale() {
		return rocketDisplayScale;
	}

	public void setRocketDisplayScale(double rocketDisplayScale) {
		this.rocketDisplayScale = rocketDisplayScale;
	}

	public double getBodyDisplayDownscale() {
		return bodyDisplayDownscale;
	}

	public void setBodyDisplayDownscale(double bodyDisplayDownscale) {
		this.bodyDisplayDownscale = bodyDisplayDownscale;
	}

}
