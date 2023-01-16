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
