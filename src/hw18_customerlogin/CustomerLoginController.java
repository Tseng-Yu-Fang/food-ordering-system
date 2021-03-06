/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw18_customerlogin;

import hw18_common.DBReport;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerLoginController implements Initializable {

    @FXML
    private TextField customer_account;
    @FXML
    private TextField customer_password;
    @FXML
    private Button btn_customerlogin;
    @FXML
    private Button btn_customerforget;
    @FXML
    private Label lbl_customerpassword;
    private boolean forget = false;
    private boolean register = false;
    private Node[] node;
    private String table = "customer";
    private static int customernumber;
    @FXML
    private Label slogan;
    @FXML
    private GridPane grid;
    @FXML
    private Label lbl_customeraccount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBReport.connect();
        getgrid(4);
        // TODO
    }

    @FXML
    private void atn_customerlogin(ActionEvent event) throws IOException {
        if (DBReport.boollogin(table, customer_account.getText(), customer_password.getText())) {
            customernumber=DBReport.getnumber();
            System.out.println("????????????:"+customernumber);
            customerpage();
            customer_password.setText("");
             ((Node) (event.getSource())).getScene().getWindow().hide();
        } else {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("????????????");
            alert.setHeaderText("");
            alert.setContentText("????????????????????????");
            alert.showAndWait();
        }
    }
    public static int getcustomerdata(){
    return customernumber;
    }

    public void customerpage() throws IOException {
        //??????????????????,?????????????????????
        Parent node = FXMLLoader.load(getClass().getResource("/hw18_customerpage/CustomerPage.fxml"));
        //??????????????????
        Scene scene = new Scene(node);
        //????????????????????????
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("???????????????????????????");
        stage.show();
        
         stage.getIcons().add(new Image(getClass().getResource("/image/icons8-mcdonald`s-512.png").toExternalForm()));
    }

    public void boolregister() {
        register = !register;
    }

    public void boolforget() {
        forget = !forget;
    }

    public void getgrid(int n) {
        node = new Node[n];
        for (int i = 0; i < n; i++) {
            node[i] = grid.getChildren().get(i);
        }
    }

    public void writegrid(int n) {
        for (int i = 0; i < node.length; i++) {
            grid.getChildren().add(i, node[i]);
        }
    }

    public void displaypassword() { //????????????
        Label display = new Label();
        String msg = String.format("????????????: %s", DBReport.customergetpassword(customer_account.getText()));
        display.setText(msg);
        display.setStyle("-fx-font-size: 14px;"
                + "-fx-text-fill: #ffffff;"
                + "-fx-background-radius:5;"
                + "-fx-padding: 2px;" + "-fx-font-weight:bold;");
        //????????????????????? 200
        display.setMaxWidth(200);
        display.setAlignment(Pos.CENTER);
        //???????????????,????????????????????????
        display.setWrapText(true);
        grid.getChildren().clear();
        writegrid(2);
        grid.add(display, 0, 1, 2, 1);
    }

    public void addfindbtn() { //?????????????????????????????????????????????

        Label space = new Label("          ");//?????????
        Button btnfind = new Button();
        HBox hbBtn = new HBox(); //?????????
        hbBtn.getChildren().add(space);//?????????
        hbBtn.getChildren().add(btnfind);//?????????
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);//?????????
        btnfind.setStyle("-fx-background-color: #DDFFA4;-fx-font-size: 14;");
        grid.add(hbBtn, 0, 1, 2, 1);
        btnfind.setText("??????");
        btnfind.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displaypassword();
            }
        });
    }

    @FXML
    private void atn_customerforget(ActionEvent event) {
        boolforget(); //???????????????????????????
        System.out.println("???????????????" + forget);
        customer_account.setText("");
        if (forget) {
            lbl_customeraccount.setText("??????:");
            customer_account.setPromptText("???????????????");
            btn_customerlogin.setVisible(false);
            addfindbtn();
            lbl_customerpassword.setVisible(false);
            customer_password.setVisible(false);
            slogan.setText("???????????????????????????");
            btn_customerforget.setText("??????");
        } else {
            customer_account.setText("");
            customer_password.setText("");
            lbl_customeraccount.setText("??????:");
            customer_account.setPromptText("???????????????");
            btn_customerlogin.setVisible(true);
            grid.getChildren().clear();
            writegrid(4);
            lbl_customerpassword.setVisible(true);
            customer_password.setVisible(true);
            slogan.setText("????????????");
            btn_customerforget.setText("????????????");
        }
    }

}
