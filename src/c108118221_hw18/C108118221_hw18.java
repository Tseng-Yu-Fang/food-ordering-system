/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c108118221_hw18;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class C108118221_hw18 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/hw18_home/HomeScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("麥當勞線上點餐系統");
        stage.setScene(scene);
        stage.show();
        
        stage.getIcons().add(new Image(getClass().getResource("/image/icons8-mcdonald`s-512.png").toExternalForm()));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
