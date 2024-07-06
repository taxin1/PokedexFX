package com.example.Lab08_1B_210041106;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pokedex_Creature {

    private Stage stage;
    private Scene scene;
    Integer ID_cr,prev_cr,next_cr;
    String name_s,type_s,des_s,evo_s,img_s,h_s,w_s,fav_s;
    String prev_s,next_s;

    @FXML
    private Label CreatureName,Type,height,weight;
    @FXML
    private TextArea description,evolution;
    @FXML
    private CheckBox favourite;
    @FXML
    ImageView imageview;
    @FXML
    private void handleFavouriteCheckbox(ActionEvent event) {
        if (favourite.isSelected()) {
            add_favourite(event);
        } else {
            remove_favourite(event);
        }
    }

    public void switchToDatabaseScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pokedex_database1.fxml")); // Ensure path is correct
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    public void setCreatureID(int id)
    {
        ID_cr = id;
    }

    public void initialize(int id_cr)
    {
        ID_cr = id_cr;
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();

        String Viewquery = "Select * from pokedex.creatures where id ='"+id_cr+"'";
        try{
            Statement statement2 = connectDB.createStatement();
            ResultSet queryOutput2 = statement2.executeQuery (Viewquery);
            while (queryOutput2.next()) {
                String queryname = queryOutput2.getString( "creature_name");
                String queryDescription = queryOutput2.getString("creature_description");
                //String queryevolution = queryOutput2.getString("creature_evolution");
                String querytype = queryOutput2.getString("creature_type");
                String queryImage = queryOutput2.getString("creature_image");
                String queryheight = queryOutput2.getString("height");
                String queryweight = queryOutput2.getString("weight");
                String queryfav = queryOutput2.getString("favourite");
                String queryprev = queryOutput2.getString("prev_gen");
                String querynext = queryOutput2.getString("next_gen");

                name_s = queryname;
                type_s = querytype;
                des_s = queryDescription;
                //evo_s = queryevolution;
                img_s = queryImage;
                h_s = queryheight;
                w_s = queryweight;
                fav_s = queryfav;
                prev_s = queryprev;
                next_s = querynext;
            }
            CreatureName.setText(name_s);
            Type.setText(type_s);
            description.setText(des_s);
            if (img_s != null) {
                System.out.println(img_s);
                Image image1 = new Image("file:///" +img_s);
                System.out.println(fav_s);
                imageview.setImage(image1);
            } else {
                System.out.println("No image");
            }
            height.setText(h_s);
            weight.setText(w_s);
            favourite.setSelected(fav_s != null && !fav_s.equals("--"));
            System.out.println(ID_cr);
        }
        catch(SQLException e) {
            Logger.getLogger (Pokedex_Creature.class.getName()).log (Level. SEVERE,null, e);
            e.printStackTrace();
        }
    }

    public void prev_clicked(ActionEvent event) throws IOException {
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();
        System.out.println(name_s);
        String Viewquery = "Select * from pokedex.creatures where next_gen ='"+name_s+"'";
        try{
            Statement statement2 = connectDB.createStatement();
            ResultSet queryOutput2 = statement2.executeQuery (Viewquery);
            while (queryOutput2.next()) {
                Integer queryID = queryOutput2.getInt("id");
                System.out.println("Query prev "+queryID);
                ID_cr = queryID;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pokedex_creature1.fxml"));
        Parent root = fxmlLoader.load();
        System.out.println("IN PREV = "+ID_cr);
        Pokedex_Creature prev = fxmlLoader.getController();
        prev.initialize(ID_cr);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void next_clicked(ActionEvent event) throws IOException {
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();
        System.out.println(name_s);
        String Viewquery = "Select * from pokedex.creatures where prev_gen ='"+name_s+"'";
        try{
            Statement statement2 = connectDB.createStatement();
            ResultSet queryOutput2 = statement2.executeQuery (Viewquery);
            while (queryOutput2.next()) {
                Integer queryID = queryOutput2.getInt("id");
                System.out.println("Query next "+queryID);
                ID_cr = queryID;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pokedex_creature1.fxml"));
        Parent root = fxmlLoader.load();
        System.out.println("IN next = "+ID_cr);
        Pokedex_Creature next = fxmlLoader.getController();
        next.initialize(ID_cr);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void add_favourite(ActionEvent event) {
        System.out.println(name_s);
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();

        String Viewquery = "UPDATE pokedex.creatures SET favourite = 'Favourite' where creature_name = ?";
        try {
            PreparedStatement statement = connectDB.prepareStatement(Viewquery);
            statement.setString(1, name_s);
            int queryOutput = statement.executeUpdate();
            if (queryOutput > 0) {
                System.out.println("Add Favourite Updated");
                //favourite.setSelected(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove_favourite(ActionEvent event) {
        System.out.println(name_s);
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();

        String Viewquery = "UPDATE pokedex.creatures SET favourite = '' where creature_name = ?";
        try {
            PreparedStatement statement = connectDB.prepareStatement(Viewquery);
            statement.setString(1, name_s);
            int queryOutput = statement.executeUpdate();
            if (queryOutput > 0) {
                System.out.println("Remove Favourite Updated");
                //favourite.setSelected(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
