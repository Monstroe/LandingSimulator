//***************************************************************
// Class: BuildPane
// Author: Monstroe
// Modified: 4/22/2022
//
// Purpose: Pane responsible for the building of rockets
//
// Attributes:	-landingSim: LSGUI
//				-rocketDisplayScale: double
//				-savedRockets: File
//				-commandModules: List<CommandModule>
//				-fuselages: List<Fuselage>
//				-engines: List<Engine>
// 
// Methods: +updateStats(): void,
//			+resetStats(): void,
//			+displayUpdatedStats(): void,
//			+displayRocketImage(): void,
//			+saveRocket(): boolean,
//			+isDuplicateRocket(String[]): boolean,
//			+loadRocket(String[][], int): void,
//			+deleteRocket(int): void,
//			+switchToRocketDisplay(): void,
//			+switchToUserRockets(): void,
//			+readData(): String[][],
//			+writeData(String[], boolean): void,
//			+deleteData(int): void,
//			+initialize(): void
//
//**************************************************************

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class BuildPane extends BorderPane {
	
	private LSGUI landingSim;
	private double rocketDisplayScale = 5;
	
	private File savedRockets;
	
	private List<CommandModule> commandModules;
	private List<Fuselage> fuselages;
	private List<Engine> engines;
	
	private HBox craftName;
	private HBox craftComplete;
	private VBox toolBox;
	private VBox statChart;
	private VBox rocketDisplay;
	private VBox userRockets;
	
	private List<RadioButton> commandModuleOptions;
	private List<RadioButton> fuselageOptions;
	private List<RadioButton> engineOptions;
	private ToggleGroup commandModuleToggleGroup;
	private ToggleGroup fuselageToggleGroup;
	private ToggleGroup engineToggleGroup;
	
	private TextField tfName;
	
	private Label lblMass;
	private Label lblThrust;
	private Label lblFuel;
	private Label lblRotationSpeed;
	
	private Rectangle fuselage;
	private Polygon commandModule;
	private Polygon engine;
	
	public BuildPane(LSGUI lsGUI) {
		landingSim = lsGUI;
		savedRockets = new File("SavedRockets.txt");
		setStyle("-fx-background-color: black");
	}
	
	public void updateStats() {
		landingSim.getGameManager().getPlayer().setCommandModule(commandModules.get(commandModuleToggleGroup.getToggles().indexOf(commandModuleToggleGroup.getSelectedToggle())));
		landingSim.getGameManager().getPlayer().setFuselage(fuselages.get(fuselageToggleGroup.getToggles().indexOf(fuselageToggleGroup.getSelectedToggle())));
		landingSim.getGameManager().getPlayer().setEngine(engines.get(engineToggleGroup.getToggles().indexOf(engineToggleGroup.getSelectedToggle())));
		
		displayUpdatedStats();
		displayRocketImage();
	}
	
	public void resetStats() {
		tfName.setText("");
		commandModuleToggleGroup.selectToggle(commandModuleOptions.get(0));
		fuselageToggleGroup.selectToggle(fuselageOptions.get(0));
		engineToggleGroup.selectToggle(engineOptions.get(0));
		
		updateStats();
	}

	public void displayUpdatedStats() {
		lblMass.setText("Mass: " + (new DecimalFormat("#.##").format(landingSim.getGameManager().getPlayer().getCommandModule().getMass()/1000 + landingSim.getGameManager().getPlayer().getFuselage().getMass()/1000 + landingSim.getGameManager().getPlayer().getEngine().getMass()/1000)) + " t");
		lblThrust.setText("Thrust: " + (new DecimalFormat("#.##").format(landingSim.getGameManager().getPlayer().getEngine().getMaxThrust()/1000)) + " kN");
		lblFuel.setText("Total Fuel: " + (new DecimalFormat("#.##").format(landingSim.getGameManager().getPlayer().getFuselage().getMaxFuelLevel())) + " units");
		lblRotationSpeed.setText("Rotation Speed: " + (new DecimalFormat("#.##").format(landingSim.getGameManager().getPlayer().getCommandModule().getRotationSpeed())) + " m/s");
	}
	
	public void displayRocketImage() {
		commandModule.getPoints().clear();
		commandModule.getPoints().addAll(new Double[] {
				0.0, 0.0,
				landingSim.getGameManager().getPlayer().getCommandModule().getWidth()/2 * rocketDisplayScale, -landingSim.getGameManager().getPlayer().getCommandModule().getHeight() * rocketDisplayScale,
	            landingSim.getGameManager().getPlayer().getCommandModule().getWidth() * rocketDisplayScale, 0.0
		});
		commandModule.setFill(landingSim.getGameManager().getPlayer().getCommandModule().getColor());
		
		fuselage.setWidth(landingSim.getGameManager().getPlayer().getFuselage().getWidth() * rocketDisplayScale);
		fuselage.setHeight(landingSim.getGameManager().getPlayer().getFuselage().getHeight() * rocketDisplayScale);
		fuselage.setFill(landingSim.getGameManager().getPlayer().getFuselage().getColor());
		
		engine.getPoints().clear();
		engine.getPoints().addAll(new Double[] {
				0.0, 0.0,
				landingSim.getGameManager().getPlayer().getEngine().getWidth()/2 * rocketDisplayScale, -landingSim.getGameManager().getPlayer().getEngine().getHeight() * rocketDisplayScale,
	            landingSim.getGameManager().getPlayer().getEngine().getWidth() * rocketDisplayScale, 0.0
		});
		engine.setFill(landingSim.getGameManager().getPlayer().getEngine().getColor());
	}
	
	public boolean saveRocket() {
		boolean success = false;
		if(!tfName.getText().isEmpty()) {
			String[] newRocket = new String[4];
			newRocket[0] = tfName.getText();
			newRocket[1] = "" + commandModuleToggleGroup.getToggles().indexOf(commandModuleToggleGroup.getSelectedToggle());
			newRocket[2] = "" + fuselageToggleGroup.getToggles().indexOf(fuselageToggleGroup.getSelectedToggle());
			newRocket[3] = "" + engineToggleGroup.getToggles().indexOf(engineToggleGroup.getSelectedToggle());
			
			if(!isDuplicateRocket(newRocket)) {
				writeData(newRocket, true);
			}
			success = true;
		}
		return success;
	}
	
	public boolean isDuplicateRocket(String[] newRocket) {
		boolean duplicate = false;
		String[][] userData = readData();
		
		if(userData != null) {
			for(int i = 0; i < userData.length; i++) {
				if(Arrays.equals(newRocket, userData[i])) {
					duplicate = true;
					break;
				}
			}
		}
		return duplicate;
	}

	public void loadRocket(String[][] userData, int index) {
		tfName.setText(userData[index][0]);
		commandModuleToggleGroup.selectToggle(commandModuleToggleGroup.getToggles().get(Integer.parseInt(userData[index][1])));
		fuselageToggleGroup.selectToggle(fuselageToggleGroup.getToggles().get(Integer.parseInt(userData[index][2])));
		engineToggleGroup.selectToggle(engineToggleGroup.getToggles().get(Integer.parseInt(userData[index][3])));
		
		updateStats();
		switchToRocketDisplay();
	}
	
	public void deleteRocket(int index) {
		deleteData(index);
		switchToRocketDisplay();
	}
	
	public void switchToRocketDisplay() {
		this.setCenter(rocketDisplay);
		
		while(userRockets.getChildren().size() > 2) {
			userRockets.getChildren().remove(1);
		}
	}
	
	public void switchToUserRockets() {
		String[][] userData = readData();
		
		if(userData != null) {
			RadioButton[] userRocketOptions = new RadioButton[userData.length];
			ToggleGroup userRocketToggleGroup = new ToggleGroup();
			
			for(int i = 0; i < userRocketOptions.length; i++) {
				userRocketOptions[i] = new RadioButton(userData[i][0]);
				userRocketOptions[i].setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
				userRocketOptions[i].setToggleGroup(userRocketToggleGroup);
				userRockets.getChildren().add(i+1, userRocketOptions[i]);
			}
			userRocketToggleGroup.selectToggle(userRocketOptions[0]);
			
			Button load = new Button("Load");
			Button delete = new Button("Delete");
			
			userRockets.getChildren().add(userRockets.getChildren().size() - 1, load);
			userRockets.getChildren().add(userRockets.getChildren().size() - 1, delete);
			
			load.setOnAction(e -> loadRocket(userData, userRocketToggleGroup.getToggles().indexOf(userRocketToggleGroup.getSelectedToggle())));
			delete.setOnAction(e -> deleteRocket(userRocketToggleGroup.getToggles().indexOf(userRocketToggleGroup.getSelectedToggle())));
			
			this.setCenter(userRockets);
		}
		
	}
	
	private String[][] readData() {
		String[][] data = null;
		try {
			FileReader fr = new FileReader(savedRockets);
			BufferedReader br = new BufferedReader(fr);
			List<String[]> lines = new ArrayList<String[]>();
			String line;
			
			while( (line = br.readLine()) != null) {
				String[] lineColumns;
				lineColumns = line.split(",");
				lines.add(lineColumns);
			}
			
			if(lines.size() > 0) {
				data = new String[lines.size()][lines.get(0).length];
				for(int i = 0; i < data.length; i++) {
					data[i] = lines.get(i);
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file 'SavedRockets.txt cannot be found in the current directory.\n"
							+  "Creating new file now...");
			try {
				savedRockets.createNewFile();
			} catch (IOException e1) {
				System.out.println("Unable to create new file 'SavedRockets.txt'.");
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	private void writeData(String[] data, boolean append) {
		try {
			FileWriter fw = new FileWriter(savedRockets, append);
			PrintWriter pw = new PrintWriter(fw);
			
			String line = "";
			for(int i = 0; i < data.length - 1; i++) {
				line += data[i] + ",";
			}
			line += data[data.length-1];
			
			pw.println(line);
			
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			//'deleteData' writes 'null' to the file to wipe it, and this catches the subsequent error thrown
		}
	}
	
	private void deleteData(int index) {
		String[][] userData = readData();
		writeData(null, false);
		for(int i = 0; i < userData.length; i++) {
			if(i != index) {
				writeData(userData[i], true);
			}
		}
	}
	
	//Initialization Methods
	public void initialize() {
		initializePartLists();
		initializeToolbox();
		initializeStatChart();
		initializeCraftName(100);
		initializeCraftComplete();
		initializeRocketDisplay();
		initializeUserRockets();
		
		updateStats();
	}

	private void initializeUserRockets() {
		userRockets = new VBox(3);
		userRockets.setAlignment(Pos.CENTER_LEFT);
		userRockets.setPadding(new Insets(5, 5, 5, 5));
		
		Label lblRockets = new Label("Your Rockets");
		lblRockets.setStyle("-fx-font: 18 Roboto; -fx-text-fill: white");
		
		Button back = new Button("Back");
		back.setOnAction(e -> switchToRocketDisplay());
		
		userRockets.getChildren().add(lblRockets);
		userRockets.getChildren().add(back);
	}
	
	private void initializeRocketDisplay() {
		rocketDisplay = new VBox(0);
		rocketDisplay.setAlignment(Pos.CENTER);
		rocketDisplay.setPadding(new Insets(5, 5, 5, 5));
		
		commandModule = new Polygon();
		fuselage = new Rectangle();
		engine = new Polygon();
		
		rocketDisplay.getChildren().add(commandModule);
		rocketDisplay.getChildren().add(fuselage);
		rocketDisplay.getChildren().add(engine);
		
		this.setCenter(rocketDisplay);
	}

	private void initializeCraftComplete() {
		craftComplete = new HBox(10);
		craftComplete.setStyle("-fx-border-color: white; "
				+ 	 "-fx-border-insets: 0; "
				+ 	 "-fx-border-width: 2; "
				+ 	 "-fx-border-style: dashed;");
		craftComplete.setAlignment(Pos.CENTER);
		craftComplete.setPadding(new Insets(5, 5, 5, 5));
		
		Button btnSave = new Button("Save and Use");
		Button btnDiscard = new Button("Discard and Exit");
		
		btnSave.setOnAction(e -> {
			if(saveRocket()) {
				landingSim.setActivePane(ActivePane.MENUPANE);
			}
			else {
				tfName.setText("New Rocket");
			}
		});
		btnDiscard.setOnAction(e -> {
			resetStats();
			landingSim.setActivePane(ActivePane.MENUPANE);
		});
		
		craftComplete.getChildren().add(btnSave);
		craftComplete.getChildren().add(btnDiscard);
		
		this.setBottom(craftComplete);
	}
	
	private void initializeCraftName(int maxTextFieldLength) {
		craftName = new HBox(10);
		craftName.setStyle("-fx-border-color: white; "
					+ 	 "-fx-border-insets: 0; "
					+ 	 "-fx-border-width: 2; "
					+ 	 "-fx-border-style: dashed;");
		craftName.setAlignment(Pos.CENTER);
		craftName.setPadding(new Insets(5, 5, 5, 5));
		
		tfName = new TextField();
		tfName.setPromptText("Enter Rocket Name Here");
		//Ensures the user doesn't enter too many characters into the text field (preventing buffer overflow attacks)
		tfName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tfName.getText().length() > maxTextFieldLength) {
	                String s = tfName.getText().substring(0, maxTextFieldLength);
	                tfName.setText(s);
	            }
	        }
		});
		
		Button btnOpenSaved = new Button("Open Saved Crafts");
		btnOpenSaved.setOnAction((e) -> switchToUserRockets());
		
		craftName.getChildren().add(tfName);
		craftName.getChildren().add(btnOpenSaved);
		
		this.setTop(craftName);
	}
	
	private void initializeStatChart() {
		statChart = new VBox(3);
		statChart.setStyle("-fx-border-color: white; "
				+ 	 "-fx-border-insets: 0; "
				+ 	 "-fx-border-width: 2; "
				+ 	 "-fx-border-style: dashed;");
		statChart.setAlignment(Pos.CENTER_LEFT);
		statChart.setPadding(new Insets(5, 5, 5, 5));
		
		Label lblTitle = new Label("Rocket Stats");
		lblMass = new Label("Mass: 0.0 t");
		lblThrust = new Label("Thrust: 0.0 kN");
		lblFuel = new Label("Total Fuel: 0.0 units");
		lblRotationSpeed = new Label("Rotation Speed: 0.0 m/s");
		
		lblTitle.setStyle("-fx-font: 18 Roboto; -fx-text-fill: white");
		lblMass.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		lblThrust.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		lblFuel.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		lblRotationSpeed.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		
		statChart.getChildren().add(lblTitle);
		statChart.getChildren().add(lblMass);
		statChart.getChildren().add(lblThrust);
		statChart.getChildren().add(lblFuel);
		statChart.getChildren().add(lblRotationSpeed);
		
		this.setLeft(statChart);
	}
	
	private void initializeToolbox() {
		toolBox = new VBox(3);
		toolBox.setStyle("-fx-border-color: white; "
					+ 	 "-fx-border-insets: 0; "
					+ 	 "-fx-border-width: 2; "
					+ 	 "-fx-border-style: dashed;");
		toolBox.setAlignment(Pos.CENTER_LEFT);
		toolBox.setPadding(new Insets(5, 5, 5, 5));
		
		Label lblTitle = new Label("ToolBox");
		Label lblCommandModule = new Label("Command Module:");
		Label lblFuselage = new Label("Fuselage:");
		Label lblEngine = new Label("Engine:");
		lblTitle.setStyle("-fx-font: 18 Roboto; -fx-text-fill: white");
		lblCommandModule.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		lblFuselage.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		lblEngine.setStyle("-fx-font: 14 Roboto; -fx-text-fill: white");
		//Command Module Radio Buttons
		RadioButton c0 = new RadioButton(commandModules.get(0).getName());
		RadioButton c1 = new RadioButton(commandModules.get(1).getName());
		RadioButton c2 = new RadioButton(commandModules.get(2).getName());
		c0.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		c1.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		c2.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		commandModuleToggleGroup = new ToggleGroup();
		c0.setToggleGroup(commandModuleToggleGroup);
		c1.setToggleGroup(commandModuleToggleGroup);
		c2.setToggleGroup(commandModuleToggleGroup);
		commandModuleToggleGroup.selectToggle(c0);
		commandModuleToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> updateStats());
		//Fuselage Radio Buttons
		RadioButton f0 = new RadioButton(fuselages.get(0).getName());
		RadioButton f1 = new RadioButton(fuselages.get(1).getName());
		RadioButton f2 = new RadioButton(fuselages.get(2).getName());
		f0.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		f1.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		f2.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		fuselageToggleGroup = new ToggleGroup();
		f0.setToggleGroup(fuselageToggleGroup);
		f1.setToggleGroup(fuselageToggleGroup);
		f2.setToggleGroup(fuselageToggleGroup);
		fuselageToggleGroup.selectToggle(f0);
		fuselageToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> updateStats());
		//Engine Radio Buttons
		RadioButton e0 = new RadioButton(engines.get(0).getName());
		RadioButton e1 = new RadioButton(engines.get(1).getName());
		RadioButton e2 = new RadioButton(engines.get(2).getName());
		RadioButton e3 = new RadioButton(engines.get(3).getName());
		RadioButton e4 = new RadioButton(engines.get(4).getName());
		RadioButton e5 = new RadioButton(engines.get(5).getName());
		e0.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		e1.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		e2.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		e3.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		e4.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		e5.setStyle("-fx-font: 12 Roboto; -fx-text-fill: white");
		engineToggleGroup = new ToggleGroup();
		e0.setToggleGroup(engineToggleGroup);
		e1.setToggleGroup(engineToggleGroup);
		e2.setToggleGroup(engineToggleGroup);
		e3.setToggleGroup(engineToggleGroup);
		e4.setToggleGroup(engineToggleGroup);
		e5.setToggleGroup(engineToggleGroup);
		engineToggleGroup.selectToggle(e0);
		engineToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> updateStats());
		
		//Add Radio Buttons to Lists
		commandModuleOptions = new ArrayList<RadioButton>();
		commandModuleOptions.add(c0);
		commandModuleOptions.add(c1);
		commandModuleOptions.add(c2);
		fuselageOptions = new ArrayList<RadioButton>();
		fuselageOptions.add(f0);
		fuselageOptions.add(f1);
		fuselageOptions.add(f2);
		engineOptions = new ArrayList<RadioButton>();
		engineOptions.add(e0);
		engineOptions.add(e1);
		engineOptions.add(e2);
		engineOptions.add(e3);
		engineOptions.add(e4);
		engineOptions.add(e5);
		
		toolBox.getChildren().add(lblTitle);
		
		toolBox.getChildren().add(lblCommandModule);
		toolBox.getChildren().add(c0);
		toolBox.getChildren().add(c1);
		toolBox.getChildren().add(c2);
		
		toolBox.getChildren().add(lblFuselage);
		toolBox.getChildren().add(f0);
		toolBox.getChildren().add(f1);
		toolBox.getChildren().add(f2);
		
		toolBox.getChildren().add(lblEngine);
		toolBox.getChildren().add(e0);
		toolBox.getChildren().add(e1);
		toolBox.getChildren().add(e2);
		toolBox.getChildren().add(e3);
		toolBox.getChildren().add(e4);
		toolBox.getChildren().add(e5);
		
		this.setRight(toolBox);
	}
	
	private void initializePartLists() {
		//COMMAND MODULES
		commandModules = new ArrayList<>();
		commandModules.add(new CommandModule(860, 12.5, 12.5, "Mk1 Command Pod", Color.DARKGRAY, 100));
		commandModules.add(new CommandModule(1560, 18.75, 18.75, "Mk2 Command Pod", Color.DARKTURQUOISE ,75));
		commandModules.add(new CommandModule(2720, 25, 25, "Mk1-3 Command Pod", Color.WHITE ,50));
		
		//FUSELAGES
		fuselages = new ArrayList<>();
		fuselages.add(new Fuselage(2250, 12.5, 25, "FL-T400 Fuel Tank", Color.BEIGE, 1000, 4));
		fuselages.add(new Fuselage(2250, 18.75, 37.5, "FL-TX1800 Fuel Tank", Color.DARKSLATEGRAY, 10000, 4));
		fuselages.add(new Fuselage(36000, 25, 50, "Rockomax Jumbo-64 Fuel Tank", Color.ORANGE, 100000, 4));
		
		//ENGINES
		engines = new ArrayList<>();
		engines.add(new SolidEngine(520, 12.5, 06.25, "F3S0 \"Shrimp\" Solid Fuel Engine", Color.PAPAYAWHIP, 74500));
		engines.add(new SolidEngine(1200, 18.75, 09.375, "RT-10 \"Hammer\" Solid Fuel Engine", Color.WHITESMOKE, 157900));
		engines.add(new SolidEngine(1500, 25, 12.5, "BACC \"Thumper\" Solid Fuel Engine", Color.DARKGRAY, 300000));
		engines.add(new LiquidEngine(500, 12.5, 06.25, "LV-909 \"Terrier\" Liquid Fuel Engine", Color.ORANGERED, 60000));
		engines.add(new LiquidEngine(1000, 18.75, 09.375, "LV-T91 \"Cheetah\" Liquid Fuel Engine", Color.CRIMSON, 1250000));
		engines.add(new LiquidEngine(1750, 25, 12.5, "RE-L10 \"Poodle\" Liquid Fuel Engine", Color.SLATEGRAY, 250000));
	}

	//Getters/Setters
	public List<CommandModule> getCommandModules() {
		return commandModules;
	}

	public void setCommandModules(List<CommandModule> commandModules) {
		this.commandModules = commandModules;
	}

	public List<Fuselage> getFuselages() {
		return fuselages;
	}

	public void setFuselages(List<Fuselage> fuselages) {
		this.fuselages = fuselages;
	}

	public List<Engine> getEngines() {
		return engines;
	}

	public void setEngines(List<Engine> engines) {
		this.engines = engines;
	}
	
}
