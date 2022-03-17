/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw18_managerpage;

import hw18_common.DBManager;
import hw18_common.DBReport;
import hw18_common.customerdata;
import hw18_common.customermenudata;
import hw18_common.orderdata;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ManagerPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private int managerdata;
    @FXML
    private Button btn_close;
    @FXML
    private TextField txt_ordernumber1;
    @FXML
    private Button btn_ordernumbersearch1;
    @FXML
    private Label lbl_ordername;
    @FXML
    private Label lbl_ordernumber;
    @FXML
    private TableView<orderdata> tableview_displayorder;
    @FXML
    private TableView<orderdata> tableview_displaymenu;
    @FXML
    private Label lbl_reportname;
    @FXML
    private Label lbl_reportnumber;
    @FXML
    private TextField txt_customerreportname;
    @FXML
    private Button btn_customerreportsearch;
    @FXML
    private TableView<customerdata> tableview_displayreport;
    boolean search = false;
    boolean customersearch = false;
    boolean customersearchid = false;
    boolean customermenusearch = false;
    boolean customermenusearchid = false;
    DBManager manager;
    private static ObservableList<orderdata> order = FXCollections.observableArrayList();
    private static ObservableList<orderdata> menu = FXCollections.observableArrayList();
    private static ObservableList<customerdata> customer = FXCollections.observableArrayList();
    private static ObservableList<customermenudata> customermenu = FXCollections.observableArrayList();
    @FXML
    private Label lbl_orderreportname;
    @FXML
    private Label lbl_orderreportnumber;
    @FXML
    private TextField txt_orderreportnum;
    @FXML
    private Button btn_orderreportsearch;
    @FXML
    private TableView<customermenudata> tableview_displayorderreport;
    @FXML
    private TextField txt_customerreportid;
    @FXML
    private Button btn_customerreportsearchid;
    @FXML
    private TextField txt_orderreportid;
    @FXML
    private Button btn_orderreportsearchid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = new DBManager();
        DBManager.connect();
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        lbl_ordername.setText(DBManager.getname());
        lbl_reportname.setText(DBManager.getname());
        lbl_orderreportname.setText(DBManager.getname());
        lbl_ordernumber.setText(String.format("%d", managerdata));
        lbl_reportnumber.setText(String.format("%d", managerdata));
        lbl_orderreportnumber.setText(String.format("%d", managerdata));
        DBManager.setordertable(tableview_displayorder);
        DBManager.plusorder(tableview_displayorder, order);
        DBManager.setmenutable(tableview_displaymenu);
        DBManager.plusmenu(tableview_displaymenu, menu);
        DBManager.pluscustomer(tableview_displayreport, customer);
        DBManager.setcustomertable(tableview_displayreport);
        DBManager.pluscustomermenu(tableview_displayorderreport, customermenu);
        DBManager.setcustomermenutable(tableview_displayorderreport);
    }

    @FXML
    private void atn_close(ActionEvent event) throws IOException {
        //產生新的容器,裝一大堆的元件
        Parent node = FXMLLoader.load(getClass().getResource("/hw18_home/HomeScreen.fxml"));
        //產生新的布景
        Scene scene = new Scene(node);
        //產生新的一個視窗
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("麥當勞線上點餐系統");
        stage.show();
        ((Node) (event.getSource())).getScene().getWindow().hide();
         stage.getIcons().add(new Image(getClass().getResource("/image/icons8-mcdonald`s-512.png").toExternalForm()));
    }

    @FXML
    private void atn_ordernumbersearch(ActionEvent event) {
        search = !search;
        if (search) {
            btn_ordernumbersearch1.setText("取消");
            btn_ordernumbersearch1.setStyle("-fx-background-color: #FA8072;");
            txt_ordernumber1.editableProperty().set(false);
            DBManager.plusmenusearch(txt_ordernumber1.getText(), tableview_displaymenu, menu);

        } else {
            btn_ordernumbersearch1.setText("查詢");
            btn_ordernumbersearch1.setStyle("-fx-styleClass: warning;");
            txt_ordernumber1.editableProperty().set(true);
            txt_ordernumber1.setText("");
            DBManager.plusmenu(tableview_displaymenu, menu);
        }

    }

    @FXML
    private void atn_customerreportsaerch(ActionEvent event) {
        customersearch = !customersearch;
        if (customersearch) {
            customersearchid=false;
            btn_customerreportsearchid.setText("查詢");
            btn_customerreportsearchid.setStyle("-fx-styleClass: warning;");
            txt_customerreportid.editableProperty().set(true);
            txt_customerreportid.setText("");
            btn_customerreportsearch.setText("取消");
            btn_customerreportsearch.setStyle("-fx-background-color: #FA8072;");
            txt_customerreportname.editableProperty().set(false);
            DBManager.pluscustomersearch(txt_customerreportname.getText(), tableview_displayreport, customer);

        } else {
            btn_customerreportsearch.setText("查詢");
            btn_customerreportsearch.setStyle("-fx-styleClass: warning;");
            txt_customerreportname.editableProperty().set(true);
            txt_customerreportname.setText("");
            DBManager.pluscustomer(tableview_displayreport, customer);
        }
    }

    @FXML
    private void atn_orderreportsaerch(ActionEvent event) {
        customermenusearch = !customermenusearch;
        if (customermenusearch) {
            customermenusearchid=false;
            btn_orderreportsearchid.setText("查詢");
            btn_orderreportsearchid.setStyle("-fx-styleClass: warning;");
            txt_orderreportid.editableProperty().set(true);
            txt_orderreportid.setText("");
            btn_orderreportsearch.setText("取消");
            btn_orderreportsearch.setStyle("-fx-background-color: #FA8072;");
            txt_orderreportnum.editableProperty().set(false);
            DBManager.pluscustomermenusearch(txt_orderreportnum.getText(), tableview_displayorderreport, customermenu);

        } else {
            btn_orderreportsearch.setText("查詢");
            btn_orderreportsearch.setStyle("-fx-styleClass: warning;");
            txt_orderreportnum.editableProperty().set(true);
            txt_orderreportnum.setText("");
            DBManager.pluscustomermenu(tableview_displayorderreport, customermenu);
        }
    }

    @FXML
    private void atn_customerreportsaerchid(ActionEvent event) {
        customersearchid = !customersearchid;
        if (customersearchid) {
            customersearch=false;
            btn_customerreportsearch.setText("查詢");
            btn_customerreportsearch.setStyle("-fx-styleClass: warning;");
            txt_customerreportname.editableProperty().set(true);
            txt_customerreportname.setText("");
            btn_customerreportsearchid.setText("取消");
            btn_customerreportsearchid.setStyle("-fx-background-color: #FA8072;");
            txt_customerreportid.editableProperty().set(false);
            DBManager.pluscustomeridsearch(txt_customerreportid.getText(), tableview_displayreport, customer);

        } else {
            btn_customerreportsearchid.setText("查詢");
            btn_customerreportsearchid.setStyle("-fx-styleClass: warning;");
            txt_customerreportid.editableProperty().set(true);
            txt_customerreportid.setText("");
            DBManager.pluscustomer(tableview_displayreport, customer);
        }
    }

    @FXML
    private void atn_orderreportsaerchid(ActionEvent event) {
        customermenusearchid = !customermenusearchid;
        if (customermenusearchid) {
            customermenusearch=false;
            btn_orderreportsearch.setText("查詢");
            btn_orderreportsearch.setStyle("-fx-styleClass: warning;");
            txt_orderreportnum.editableProperty().set(true);
            txt_orderreportnum.setText("");
            btn_orderreportsearchid.setText("取消");
            btn_orderreportsearchid.setStyle("-fx-background-color: #FA8072;");
            txt_orderreportid.editableProperty().set(false);
            DBManager.pluscustomermenuidsearch(txt_orderreportid.getText(), tableview_displayorderreport, customermenu);

        } else {
            btn_orderreportsearchid.setText("查詢");
            btn_orderreportsearchid.setStyle("-fx-styleClass: warning;");
            txt_orderreportid.editableProperty().set(true);
            txt_orderreportid.setText("");
            DBManager.pluscustomermenu(tableview_displayorderreport, customermenu);
        }
    }

}
