package com.example.Lab08_1B_210041106;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pokedex_Database_Controller {

    private Stage stage;
    private Scene scene;

    int ID_number_creature;

    @FXML
    TextField keywordTextField;

    public void switchtoPokedexDashboardScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pokedex_Dashboard1.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }


    @FXML
    private TableView<Pokedex_Database> pokedexDatabaseTableView;
    @FXML
    private TableColumn<Pokedex_Database,Integer> pokedexDatabaseIDTableColumn;
    @FXML
    private TableColumn<Pokedex_Database,String>pokedexDatabaseNameTableColumn;
    @FXML
    private TableColumn<Pokedex_Database,String>pokedexDatabaseTypeTableColumn;
    @FXML
    private TableColumn<Pokedex_Database, String>pokedexDatabaseFavouriteTableColumn;

    ObservableList<Pokedex_Database> pokedexDatabaseObservableList = FXCollections.observableArrayList();

    public void initialize() {
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();

        String adminproviderTableViewquery = "Select id, creature_name,creature_type, favourite from pokedex.creatures";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery (adminproviderTableViewquery);
            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("id");
                String queryname = queryOutput.getString( "creature_name");
                String queryType = queryOutput.getString("creature_type");
                String queryFavourite = queryOutput.getString("favourite");

                pokedexDatabaseObservableList.add(new Pokedex_Database(queryID, queryname, queryType, queryFavourite));
            }

            pokedexDatabaseIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
            pokedexDatabaseNameTableColumn.setCellValueFactory (new PropertyValueFactory<>("Name"));
            if (pokedexDatabaseTypeTableColumn != null) {
                pokedexDatabaseTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            }
            pokedexDatabaseFavouriteTableColumn.setCellValueFactory (new PropertyValueFactory<>("Favourite"));

            pokedexDatabaseTableView.setItems(pokedexDatabaseObservableList);

            //Initial filtered list
            FilteredList<Pokedex_Database> filteredData = new FilteredList<>(pokedexDatabaseObservableList, b -> true);
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate (Pokedex_Database -> {
                    // If no search value then display all records or whatever records it current have. no changes.
                    if (newValue.isEmpty() || newValue.isBlank () || newValue == null) {
                        return true;
                    }
                    String search_Keyword = newValue.toLowerCase();
                    if (Pokedex_Database.getName().toLowerCase().indexOf(search_Keyword) > -1) {
                        return true; // Means we found a match in ProductName
                    }
                    else if (Pokedex_Database.getType().toLowerCase().indexOf(search_Keyword) > -1) {
                        return true;
                    } else
                    return false;
                });
            });

            SortedList<Pokedex_Database> sortedData = new SortedList<>(filteredData);
            // Bind sorted result with Table View
            sortedData.comparatorProperty().bind(pokedexDatabaseTableView.comparatorProperty());
            // Apply filtered and sorted data to the Table View
            pokedexDatabaseTableView.setItems(sortedData);
            //show just one selected rov
        }
        catch(SQLException e) {
            Logger.getLogger (Pokedex_Database_Controller.class.getName()).log (Level. SEVERE,null, e);
            e.printStackTrace();
        }
    }

    @FXML
    public void Selection(ActionEvent event) throws IOException {
        ObservableList<Pokedex_Database> pokedexlist;
        pokedexlist = pokedexDatabaseTableView.getSelectionModel().getSelectedItems();

        if (pokedexlist.isEmpty()) {
            // If no item is selected, show an alert message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Select Desired Pokemon First!");
            alert.showAndWait();
            return; // Stop further execution
        }

        System.out.println("ID = ");
        System.out.println(pokedexlist.get(0).getID());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pokedex_creature1.fxml"));
        Parent root = fxmlLoader.load();
        Pokedex_Creature nextPokemonController = fxmlLoader.getController(); // Cast to the appropriate type
        nextPokemonController.initialize(pokedexlist.get(0).getID());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
}

