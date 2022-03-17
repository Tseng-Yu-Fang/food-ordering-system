/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw18_home;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class HomeScreenController implements Initializable {

    @FXML
    private MenuItem customerlogin_menuitem;
    @FXML
    private MenuItem managerlogin_menuitem;
     @FXML
    private Pane pane_container;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void customerlogin(ActionEvent event) throws IOException {
        Parent nodes = FXMLLoader.load(getClass().getResource("/hw18_customerlogin/CustomerLogin.fxml"));
//取得 pane_container 內所有的元件,並重新設定為新的 nodes
        pane_container.getChildren().setAll(nodes);
    }

    @FXML
    private void managerlogin(ActionEvent event) throws IOException {
        Parent nodes = FXMLLoader.load(getClass().getResource("/hw18_managerlogin/ManagerLogin.fxml"));
//取得 pane_container 內所有的元件,並重新設定為新的 nodes
        pane_container.getChildren().setAll(nodes);
    }

    @FXML
    private void customerregister(ActionEvent event) throws IOException {
        Parent nodes = FXMLLoader.load(getClass().getResource("/hw18_customerregister/CustomerRegister.fxml"));
//取得 pane_container 內所有的元件,並重新設定為新的 nodes
        pane_container.getChildren().setAll(nodes);
    }
    
}
