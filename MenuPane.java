//***************************************************************
// Class: MenuPane
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Pane responsible for the main menu of the program
//
// Attributes:	-landingSim: LSGUI
//				-bodies: List<Body>
//				-customBody: Body
// 
// Methods: +selectBody(): void,
//			+createCustomBody(): void,
//			+editCustomBody(): boolean,
//			+setBodyCreationDisable(boolean): void,
//			+initialize(): void
//
//**************************************************************

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MenuPane extends FlowPane {

	private LSGUI landingSim;
	
	private List<Body> bodies;
	private Body customBody;
	
	private VBox bodySelection;
	private VBox rocketBuilder;
	private VBox startHeight;
	private VBox startSim;
	
	private HBox mass;
	private HBox radius;
	private HBox surfaceGravity;
	private Button btnCreateBody;
	private Label lblCustomBodyNote;
	private TextField tfMass;
	private TextField tfRadius;
	private TextField tfSurfaceGravity;
	
	private ToggleGroup bodyToggleGroup;
	
	public MenuPane(LSGUI lsGUI) {
		landingSim = lsGUI;
		setStyle("-fx-background-color: black");
	}

	public void selectBody() {
		int index = bodyToggleGroup.getToggles().indexOf(bodyToggleGroup.getSelectedToggle());
		if(index == bodies.size()) {
			setBodyCreationDisable(false);
		}
		else {
			landingSim.getGameManager().getPlayer().setCurrentBody(bodies.get(index));
			setBodyCreationDisable(true);
		}
	}
	
	public void createCustomBody() {
		if(editCustomBody()) {
			landingSim.getGameManager().getPlayer().setCurrentBody(customBody);
		}
	}
	
	public boolean editCustomBody() {
		boolean success = false;
		double mass = 0;
		double radius = 0;
		double surfaceGravity = 0;
		int inputCounter = 0;
		try {
			mass = Math.abs(Double.parseDouble(tfMass.getText()));
			inputCounter++;
			customBody.setMass(mass * Math.pow(10, 24));
		} catch(Exception e) {
			tfMass.setText("");
		}
		try {
			radius = Math.abs(Double.parseDouble(tfRadius.getText()));
			inputCounter++;
			customBody.setRadius(radius * 1000);
		} catch(Exception e) {
			tfRadius.setText("");
		}
		try {
			surfaceGravity = Math.abs(Double.parseDouble(tfSurfaceGravity.getText()));
			inputCounter++;
			customBody.setSurfaceGravity(surfaceGravity);
		} catch(Exception e) {
			tfSurfaceGravity.setText("");
		}
		
		if(inputCounter >= 2) {
			tfMass.setText("" + customBody.getMass() / Math.pow(10, 24));
			tfRadius.setText("" + customBody.getRadius() / 1000);
			tfSurfaceGravity.setText("" + customBody.getSurfaceGravity());
			success = true;
		}
		return success;
	}
	
	public void setBodyCreationDisable(boolean disable) {
		mass.setDisable(disable);
		radius.setDisable(disable);
		surfaceGravity.setDisable(disable);
		btnCreateBody.setDisable(disable);
		lblCustomBodyNote.setDisable(disable);
	}
	
	//Initialization Methods
	public void initialize() {
		customBody = new Body();
		
		initializeBodies();
		initializeBodySelection(10);
		initializeRocketBuilder();
		initializeStartingHeight(100, 1000, 5);
		initializeStart();
		
		selectBody();
	}
	
	private void initializeStart() {
		startSim = new VBox(3);
		startSim.setStyle("-fx-border-color: gold; "
				+ 	 "-fx-border-insets: 10; "
				+ 	 "-fx-border-width: 2; ");
		startSim.setAlignment(Pos.CENTER);
		startSim.setPadding(new Insets(10, 10, 10, 10));
		
		Label lblTitle = new Label("Start Simulation");
		lblTitle.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		
		Button btnStartSim = new Button("Start");
		btnStartSim.setOnAction(e -> {
			landingSim.setActivePane(ActivePane.SIMULATIONPANE);
			landingSim.getSimulationPane().startSimulation();
		});
		
		startSim.getChildren().add(lblTitle);
		startSim.getChildren().add(btnStartSim);
		
		this.getChildren().add(startSim);
	}
	
	private void initializeStartingHeight(int minHeight, int maxHeight, int maxTextFieldLength) {
		startHeight = new VBox(3);
		startHeight.setStyle("-fx-border-color: gold; "
				+ 	 "-fx-border-insets: 10; "
				+ 	 "-fx-border-width: 2; ");
		startHeight.setAlignment(Pos.CENTER);
		startHeight.setPadding(new Insets(10, 10, 10, 10));
		
		Label lblTitle = new Label("Starting Height");
		lblTitle.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		
		TextField tfHeight = new TextField();
		tfHeight.setPromptText("Enter Starting Height");
		tfHeight.setOnAction(e -> {
			try {
				int startingHeight = Math.max(minHeight, Math.min(Integer.parseInt(tfHeight.getText()), maxHeight));
				landingSim.getGameManager().getPlayer().setStartingAltitude(startingHeight);
				tfHeight.setText(startingHeight + "");
			} catch(Exception ex) {
				tfHeight.setText("");
			}
		});
		tfHeight.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tfHeight.getText().length() > maxTextFieldLength) {
	                String s = tfHeight.getText().substring(0, maxTextFieldLength);
	                tfHeight.setText(s);
	            }
	        }
		});
		
		Label lblReminder = new Label("*Press enter on the text box to set as new height.\n"
									+ "Default starting height is 250m.");
		lblReminder.setStyle("-fx-font: 10 Roboto; -fx-text-fill: white");
		
		startHeight.getChildren().add(lblTitle);
		startHeight.getChildren().add(tfHeight);
		startHeight.getChildren().add(lblReminder);
		
		this.getChildren().add(startHeight);
	}
	
	private void initializeRocketBuilder() {
		rocketBuilder = new VBox(3);
		rocketBuilder.setStyle("-fx-border-color: gold; "
				+ 	 "-fx-border-insets: 10; "
				+ 	 "-fx-border-width: 2; ");
		rocketBuilder.setAlignment(Pos.CENTER);
		rocketBuilder.setPadding(new Insets(10, 10, 10, 10));
		
		Label lblTitle = new Label("Build Your Rocket");
		lblTitle.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		
		Button btnRocketBuilder = new Button("Rocket Builder");
		btnRocketBuilder.setOnAction(e -> {
			landingSim.setActivePane(ActivePane.BUILDPANE);
		});
		
		rocketBuilder.getChildren().add(lblTitle);
		rocketBuilder.getChildren().add(btnRocketBuilder);
		
		this.getChildren().add(rocketBuilder);
	}
	
	private void initializeBodySelection(int maxTextFieldLength) {
		bodySelection = new VBox(3);
		bodySelection.setStyle("-fx-border-color: gold; "
				+ 	 "-fx-border-insets: 10; "
				+ 	 "-fx-border-width: 2; ");
		bodySelection.setAlignment(Pos.CENTER_LEFT);
		bodySelection.setPadding(new Insets(10, 10, 10, 10));
		
		Label lblTitle = new Label("Pick a Body");
		lblTitle.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		
		RadioButton b0 = new RadioButton("The Moon");
		RadioButton b1 = new RadioButton("Mercury");
		RadioButton b2 = new RadioButton("Pluto");
		RadioButton bCustom = new RadioButton("Create Your Own:");
		b0.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		b1.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		b2.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		bCustom.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		bodyToggleGroup = new ToggleGroup();
		b0.setToggleGroup(bodyToggleGroup);
		b1.setToggleGroup(bodyToggleGroup);
		b2.setToggleGroup(bodyToggleGroup);
		bCustom.setToggleGroup(bodyToggleGroup);
		bodyToggleGroup.selectToggle(b0);
		bodyToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> selectBody());
		
		tfMass = new TextField();
		tfRadius = new TextField();
		tfSurfaceGravity = new TextField();
		tfMass.setPromptText("Enter Mass of Body");
		tfMass.setOnAction(e -> editCustomBody());
		tfRadius.setPromptText("Enter Radius of Body");
		tfRadius.setOnAction(e -> editCustomBody());
		tfSurfaceGravity.setPromptText("Enter Surface Gravity of Body");
		tfSurfaceGravity.setOnAction(e -> editCustomBody());
		tfMass.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tfMass.getText().length() > maxTextFieldLength) {
	                String s = tfMass.getText().substring(0, maxTextFieldLength);
	                tfMass.setText(s);
	            }
	        }
		});
		tfRadius.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tfRadius.getText().length() > maxTextFieldLength) {
	                String s = tfRadius.getText().substring(0, maxTextFieldLength);
	                tfRadius.setText(s);
	            }
	        }
		});
		tfSurfaceGravity.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tfSurfaceGravity.getText().length() > maxTextFieldLength) {
	                String s = tfSurfaceGravity.getText().substring(0, maxTextFieldLength);
	                tfSurfaceGravity.setText(s);
	            }
	        }
		});
		Label lblMass = new Label("Mass (10^24 kg)");
		Label lblRadius = new Label("Radius (km)");
		Label lblSurfaceGravity = new Label("Surface Gravity (m/s^2)");
		lblMass.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		lblRadius.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		lblSurfaceGravity.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		mass = new HBox(3, tfMass, lblMass);
		radius = new HBox(3, tfRadius, lblRadius);
		surfaceGravity = new HBox(3, tfSurfaceGravity, lblSurfaceGravity);
		btnCreateBody = new Button("Create Body");
		btnCreateBody.setOnAction(e -> createCustomBody());
		lblCustomBodyNote = new Label("*NOTE: These values are all mathematically related to each \nother, so you may see them change.");
		lblCustomBodyNote.setStyle("-fx-font: 10 Roboto; -fx-text-fill: white");
		setBodyCreationDisable(true);
		
		bodySelection.getChildren().add(lblTitle);
		bodySelection.getChildren().add(b0);
		bodySelection.getChildren().add(b1);
		bodySelection.getChildren().add(b2);
		bodySelection.getChildren().add(bCustom);
		bodySelection.getChildren().add(mass);
		bodySelection.getChildren().add(radius);
		bodySelection.getChildren().add(surfaceGravity);
		bodySelection.getChildren().add(btnCreateBody);
		bodySelection.getChildren().add(lblCustomBodyNote);
		
		this.getChildren().add(bodySelection);
	}

	private void initializeBodies() {
		bodies = new ArrayList<>();
		
		Body moon = new Body();
		moon.setMass(0.07346 * Math.pow(10, 24));
		moon.setRadius(1737.4 * 1000);
		moon.setColor(Color.DARKGRAY);
		Body mercury = new Body();
		mercury.setMass(0.33010 * Math.pow(10, 24));
		mercury.setRadius(2439.7 * 1000);
		mercury.setColor(Color.GRAY);
		Body pluto = new Body();
		pluto.setMass(0.01303 * Math.pow(10, 24));
		pluto.setRadius(1188 * 1000);
		pluto.setColor(Color.DARKGRAY);
		
		bodies.add(moon);
		bodies.add(mercury);
		bodies.add(pluto);
	}

	//Getters/Setters
	public List<Body> getBodies() {
		return bodies;
	}

	public void setBodies(List<Body> bodies) {
		this.bodies = bodies;
	}

}
