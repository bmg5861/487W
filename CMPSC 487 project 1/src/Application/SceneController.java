package Application;



import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SceneController {

	private Stage stage;
	private Scene scene;

	DatabaseConnection connection = new DatabaseConnection();
	Connection connect = connection.getConnection();

	@FXML private Label StudentLabel;
	@FXML private TextField StudentID;
	@FXML private Button swipeIn;
	@FXML private Button swipeOut;
	
	@FXML private TextField AdminID;
	@FXML private Label warringLabel;
	
	@FXML private TextField startTime = new TextField();
	@FXML private TextField endTime = new TextField();
	
	private ObservableList<Access> Accessdata = FXCollections.observableArrayList();
	private ObservableList<Users> Usersdata = FXCollections.observableArrayList();
	
	@FXML private TableView<Users> usersTable = new TableView<Users>();
	@FXML private TableView<Access> recordsTable = new TableView<Access>();

	@FXML private TableColumn<Access, String> idColumnAccess = new TableColumn<Access, String>();
	@FXML private TableColumn<Access, String> nameColumnAccess = new TableColumn<Access, String>();
	@FXML private TableColumn<Access, String> typeColumnAccess = new TableColumn<Access, String>();
	@FXML private TableColumn<Access, Timestamp> timeColumnAccess = new TableColumn<Access, Timestamp>();
	@FXML private TableColumn<Access, String> inoutColumnAccess = new TableColumn<Access, String>();
	@FXML private TextField idRecords = new TextField();
	
	@FXML private TableColumn<Users, String> idColumnRecords = new TableColumn<Users, String>();
	@FXML private TableColumn<Users, String> nameColumnRecords = new TableColumn<Users, String>();
	@FXML private TableColumn<Users, String> typeColumnRecords = new TableColumn<Users, String>();
	@FXML private TableColumn<Users, String> accessColumnRecords = new TableColumn<Users, String>();;
	@FXML private TextField idUsers= new TextField();
	@FXML private Label statusLabel = new Label();
	
	private StringBuilder enterText = new StringBuilder();
	
	@FXML
	public void initialize() {
		
		Accessdata = FXCollections.observableArrayList();
		Usersdata = FXCollections.observableArrayList();
		
		try {
			ResultSet result = connect.createStatement().executeQuery("SELECT * FROM access");
			while(result.next()) {
				Access access = new Access();
				access.setId(result.getString(1));
				access.setName(result.getString(2));
				access.setType(result.getString(3));
				access.setTimeStamp(result.getTimestamp(4));
				access.setSwipeType(result.getString(5));
				Accessdata.add(access);
			}
			idColumnAccess.setCellValueFactory(cellData ->  new ReadOnlyStringWrapper(cellData.getValue().getId()));
			nameColumnAccess.setCellValueFactory(new PropertyValueFactory<Access, String>("name"));
			typeColumnAccess.setCellValueFactory(new PropertyValueFactory<Access, String>("type"));
			timeColumnAccess.setCellValueFactory(new PropertyValueFactory<Access, Timestamp>("timeStamp"));
			inoutColumnAccess.setCellValueFactory(new PropertyValueFactory<Access, String>("swipeType"));
			
			FilteredList<Access> filteredData = new FilteredList<>(Accessdata, b -> true);
			
			idRecords.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(use -> {
									
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					
					if (use.getId().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
						return true; // Filter matches id.
					} else if (use.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true; // Filter matches name.
					}
					     else  
					    	 return false; // Does not match.
				});
			});
			
			startTime.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(timestamp -> {
				
                if (newValue == null || newValue.isEmpty()) {
                	return true;
				}
                try {
                
                Timestamp start = Timestamp.valueOf(newValue.split("/")[0]);
                Timestamp end = Timestamp.valueOf(newValue.split("/")[1]);
                
                if(timestamp.getTimeStamp().after(start) && timestamp.getTimeStamp().before(end)) {
                	return true;
                } else {
                return false;
                }
                
                } catch (Exception e){
                	return false;
             
                }
            });
		});
			
			 
			SortedList<Access> sortedData = new SortedList<>(filteredData);
			
			sortedData.comparatorProperty().bind(recordsTable.comparatorProperty());
			
			recordsTable.setItems(sortedData);
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			ResultSet result = connect.createStatement().executeQuery("SELECT * FROM users");
			while(result.next()) {
				Users user = new Users();
				user.setId(result.getString(1));
				user.setName(result.getString(2));
				user.setType(result.getString(3));
				user.setStatus(user.isStatus(result.getBoolean(4)));
				Usersdata.add(user);
			}
			idColumnRecords.setCellValueFactory(cellData ->  new ReadOnlyStringWrapper(cellData.getValue().getId()));
			nameColumnRecords.setCellValueFactory(new PropertyValueFactory<Users, String>("name"));
			typeColumnRecords.setCellValueFactory(new PropertyValueFactory<Users, String>("type"));
			accessColumnRecords.setCellValueFactory(new PropertyValueFactory<Users, String>("status"));
			
			usersTable.setItems(Usersdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
	
	public void refreshTable() {
		try {
			usersTable.getItems().clear();
			ResultSet result = connect.createStatement().executeQuery("SELECT * FROM users");
			while(result.next()) {
				Users user = new Users();
				user.setId(result.getString(1));
				user.setName(result.getString(2));
				user.setType(result.getString(3));
				user.setStatus(user.isStatus(result.getBoolean(4)));
				Usersdata.add(user);
			}
			idColumnRecords.setCellValueFactory(cellData ->  new ReadOnlyStringWrapper(cellData.getValue().getId()));
			nameColumnRecords.setCellValueFactory(new PropertyValueFactory<Users, String>("name"));
			typeColumnRecords.setCellValueFactory(new PropertyValueFactory<Users, String>("type"));
			accessColumnRecords.setCellValueFactory(new PropertyValueFactory<Users, String>("status"));
			
			usersTable.setItems(Usersdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	public boolean validateID(String ID, Label label) {
		boolean valid = true;
		
		if (!(ID.length() >= 11)) {
			valid = false;
			label.setText("Invalid Id");
			return valid;
		} else if (!(ID.substring(0, 2).equals("%A"))) {
			valid = false;
			label.setText("Invalid Id");
			return valid;
		} else if (!(ID.substring(3, 11).matches("^[0-9]*$"))) {
			valid = false;
			label.setText("Invalid Id");
			return valid;
		}
		
		if(valid) {
			
		try {
			PreparedStatement statement = connect.prepareStatement("SELECT COUNT(ID) FROM users WHERE ID = " + "\"" + ID + "\"");
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			if(resultSet.getInt(1) == 0) {
				valid = false;
				label.setText("Invalid Id 1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			label.setText("Invalid Id 2");
		}
		
		try {
			PreparedStatement statement = connect.prepareStatement("SELECT user_status FROM users WHERE ID = " + "\"" + ID + "\"");
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			if(resultSet.getInt(1) == 0) {
				valid = false;
				label.setText("User does not have access");
			}
		} catch (SQLException e) {
			label.setText("User does not have access");
		}
		
	}
		if(valid) {
			label.setText("Swipe Succesfull");
		}
		
		return valid;
	}

	public void swipeIn(ActionEvent event) {
		enterText.append(StudentID.getText());
		StudentID.clear();
		if (validateID(enterText.toString(), StudentLabel)) {
			try {
				PreparedStatement s3 = connect.prepareStatement("SELECT COUNT(ID) FROM access WHERE ID = " + "\"" + enterText.toString() + "\"");
				ResultSet rs3 = s3.executeQuery();
				rs3.next();
				if(rs3.getInt(1) % 2 == 0) {
					PreparedStatement s1 = connect.prepareStatement("SELECT * FROM users WHERE ID = " + "\"" + enterText.toString() + "\"");
					ResultSet rs1 = s1.executeQuery();
					rs1.next();
					String ID = rs1.getString(1);
					String name = rs1.getString(2);
					String type = rs1.getString(3);
					Statement s = connect.createStatement();
					s.executeUpdate("INSERT INTO access values (" + "\"" + ID + "\","  + "\"" + name + "\","  + "\"" + type + "\", CURRENT_TIMESTAMP(), 'In'); ");
				} else {
					StudentLabel.setText("Must Swipe out first");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				StudentLabel.setText("Invalid Id");
			}
		}
		enterText.delete(0, enterText.length());
	}

	public void swipeOut(ActionEvent event) {
		enterText.append(StudentID.getText());
		StudentID.clear();
		if (validateID(enterText.toString(), StudentLabel)) {
			try {
				PreparedStatement s3 = connect.prepareStatement("SELECT COUNT(ID) FROM access WHERE ID = " + "\"" + enterText.toString() + "\"");
				ResultSet rs3 = s3.executeQuery();
				rs3.next();
				if(rs3.getInt(1) % 2 == 1) {
					PreparedStatement s1 = connect.prepareStatement("SELECT * FROM users WHERE ID = " + "\"" + enterText.toString() + "\"");
					ResultSet rs1 = s1.executeQuery();
					rs1.next();
					String ID = rs1.getString(1);
					String name = rs1.getString(2);
					String type = rs1.getString(3);
					Statement s = connect.createStatement();
					s.executeUpdate("INSERT INTO access values (" + "\"" + ID + "\","  + "\"" + name + "\","  + "\"" + type + "\", CURRENT_TIMESTAMP(), 'Out'); ");
				} else {
					StudentLabel.setText("Must Swipe In first");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				StudentLabel.setText("Invalid Id");
			}
		}
		enterText.delete(0, enterText.length());
	}
	
	public boolean adminSwipe() {
		boolean valid = false;
		enterText.append(AdminID.getText());
		AdminID.clear();
		if (validateID(enterText.toString(), warringLabel)) {
			try {
					PreparedStatement s1 = connect.prepareStatement("SELECT * FROM users WHERE ID = " + "\"" + enterText.toString() + "\"");
					ResultSet rs1 = s1.executeQuery();
					rs1.next();
					String type = rs1.getString(3);
					if(type.equals("Admin")) {
						valid = true;
					}
			} catch (SQLException e) {
				e.printStackTrace();
				StudentLabel.setText("Invalid Id");
			}
		}
		
		return valid;
	}
	
	public void grantAccessBtn() {
		try {
			Statement stateMent = connect.createStatement();
			stateMent.executeUpdate("UPDATE users SET user_status = true WHERE ID = " + "\"" + idUsers.getText() + "\"");
			statusLabel.setText("Access Grated");
			refreshTable();
			idUsers.clear();
		} catch (SQLException e) {
			statusLabel.setText("Invalid ID");
			idUsers.clear();
		}
	}
	
	public void revokeAccessBtn() {
		try {
			Statement stateMent = connect.createStatement();
			stateMent.executeUpdate("UPDATE users SET user_status = false WHERE ID = " + "\"" + idUsers.getText() + "\"");
			statusLabel.setText("Access Revoked");
			refreshTable();
			idUsers.clear();
		} catch (SQLException e) {
			statusLabel.setText("Invalid ID");
			idUsers.clear();
		}
	}
	
	public void resetFilter() {
		idRecords.clear();
		startTime.clear();
		endTime.clear();
	}

	public void switchToMain(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToStudentAccess(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/StudentAccess.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToAdminAccess(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/AdminAccess.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToAdminView(ActionEvent event) throws IOException {
		if(adminSwipe()) {
		Parent root = FXMLLoader.load(getClass().getResource("/AdminView.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		initialize();
		} else {
			warringLabel.setText("User does not have Admin access");
		}
	}

}
