//***************************************************************
// Class: LSGUI
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Top Level GUI for program
//
// Attributes:  -manager: GameManager
//				-topPane: Pane
//				-menuPane: MenuPane
//				-buildPane: BuildPane
//				-simulationPane: SimulationPane
// 
// Methods: -start(Stage): void,
//			+setActivePane(ActivePane): void
//			+main(String[]): void,
//
//**************************************************************

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

enum ActivePane {
	MENUPANE,
	BUILDPANE,
	SIMULATIONPANE
}

public class LSGUI extends Application {
	
	private GameManager manager;
	
	private BorderPane topPane;
	private MenuPane menuPane;
	private BuildPane buildPane;
	private SimulationPane simulationPane;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		topPane = new BorderPane();
		menuPane = new MenuPane(this);
		buildPane = new BuildPane(this);
		simulationPane = new SimulationPane(this);
		
		manager = new GameManager(simulationPane);
		
		menuPane.initialize();
		buildPane.initialize();
		simulationPane.initialize();
		
		Scene scene = new Scene(topPane, 600, 600);
		setActivePane(ActivePane.MENUPANE);
		
		primaryStage.setTitle("Landing Simulator");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//testSimulation(1050000, -1338.56);
		//testSimulation(1050000, -1181);
		//simulationPane.startSimulation();
	}

	public void setActivePane(ActivePane pane) {
		switch(pane) {
			case SIMULATIONPANE: {
				topPane.setCenter(simulationPane);
				simulationPane.requestFocus();
				break;
			}
			case BUILDPANE: {
				topPane.setCenter(buildPane);
				buildPane.requestFocus();
				break;
			}
			case MENUPANE: {
				topPane.setCenter(menuPane);
				menuPane.requestFocus();
				break;
			}
			default:
				System.out.println("This should never print.");
				break;
		}
	}
	
	//FOR TESTING
	//This code would go somewhere in MenuPane, when the user is choosing/creating a moon
	private void testSimulation(int startingHeightInMeters, double orbitalVelocity) {
		
		//Create Mathematical Body
		Body moon = new Body();
		moon.setMass(0.07346 * Math.pow(10, 24));
		moon.setRadius(1737.4 * 1000);
		//System.out.println("Mass: " + moon.getMass() + " Radius: " + moon.getRadius() + " Surface Gravity: " + moon.getSurfaceGravity());
		
		//Create Mathematical Rocket
		//manager.setPlayer(new Rocket());
		manager.getPlayer().setCurrentBody(moon);
		manager.getPlayer().setXPosition(0);
		manager.getPlayer().setYPosition(startingHeightInMeters + moon.getRadius());
		manager.getPlayer().setXVelocity(orbitalVelocity);
		//manager.getPlayer().setCommandModule(new CommandModule(5000, 100));
		//manager.getPlayer().setFuselage(new Fuselage(5000, 100000, 10));
		//manager.getPlayer().setEngine(new LiquidEngine(5000, 35000));
		
		//Create Display Body
		simulationPane.setBody(new Circle(moon.getRadius() / 10000));
		//simulationPane.getBody().setCenterX(simulationPane.getWidth()/2);
		//simulationPane.getBody().setCenterY(simulationPane.getHeight()/2);
		simulationPane.getBody().setFill(Color.GRAY);
		
		//Create Display Rocket Fuselage
		/*simulationPane.setFuselage(new Rectangle());
		simulationPane.getFuselage().setWidth(40);
		simulationPane.getFuselage().setHeight(20);
		simulationPane.getFuselage().setX(simulationPane.getWidth()/2 + manager.getPlayer().getXPosition()/10000);
		simulationPane.getFuselage().setY(simulationPane.getHeight()/2 - manager.getPlayer().getYPosition()/10000);
		simulationPane.getFuselage().setFill(Color.RED);
		//Create Display Rocket Command Module
		Polygon commandModule = new Polygon();
		commandModule.getPoints().addAll(new Double[]{
				0.0, 0.0,
	            simulationPane.getFuselage().getHeight(), simulationPane.getFuselage().getHeight()/2,
	            0.0, simulationPane.getFuselage().getHeight() });
		commandModule.setFill(Color.GREEN);
		commandModule.setLayoutX(simulationPane.getFuselage().getX() + simulationPane.getFuselage().getWidth());
		commandModule.setLayoutY(simulationPane.getFuselage().getY());
		simulationPane.setCommandModule(commandModule);
		//Create Display Rocket Engine
		Polygon engine = new Polygon();
		engine.getPoints().addAll(new Double[] {
				0.0, 0.0,
				simulationPane.getFuselage().getHeight()/2, simulationPane.getFuselage().getHeight()/2,
				0.0, simulationPane.getFuselage().getHeight()
		});
		engine.setFill(Color.ORANGE);
		engine.setLayoutX(simulationPane.getFuselage().getX() - simulationPane.getFuselage().getHeight()/2);
		engine.setLayoutY(simulationPane.getFuselage().getY());
		simulationPane.setEngine(engine);*/
		
		//Add Nodes to Simulation Pane
		simulationPane.getChildren().add(simulationPane.getBody());
		//simulationPane.getChildren().add(simulationPane.getFuselage());
		//simulationPane.getChildren().add(commandModule);
		//simulationPane.getChildren().add(engine);
		
		simulationPane.startSimulation();
	}
	
	public static void main(String[] args) {
		launch();
	}

	//Getters/Setters
	public GameManager getGameManager() {
		return manager;
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	public MenuPane getMenuPane() {
		return menuPane;
	}

	public void setMenuPane(MenuPane menuPane) {
		this.menuPane = menuPane;
	}

	public BuildPane getBuildPane() {
		return buildPane;
	}

	public void setBuildPane(BuildPane buildPane) {
		this.buildPane = buildPane;
	}

	public SimulationPane getSimulationPane() {
		return simulationPane;
	}

	public void setSimulationPane(SimulationPane simulationPane) {
		this.simulationPane = simulationPane;
	}
	
}
