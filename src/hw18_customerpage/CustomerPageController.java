/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw18_customerpage;

import hw18_common.DBMenu;
import hw18_common.DBOrder;
import hw18_common.DBReport;
import hw18_common.cratdata;
import hw18_common.orderdata;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static int customerdata;
    @FXML
    private TextField txt_menuaddress;
    @FXML
    private Button btn_foodM1m;
    @FXML
    private Label lbl_foodM1num;
    @FXML
    private Button btn_foodM1;
    @FXML
    private Button btn_foodM2m;
    @FXML
    private Label lbl_foodM2num;
    @FXML
    private Button btn_foodM2;
    @FXML
    private Button btn_foodM3m;
    @FXML
    private Label lbl_foodM3num;
    @FXML
    private Button btn_foodM3;
    @FXML
    private Button btn_foodM4m;
    @FXML
    private Label lbl_foodM4num;
    @FXML
    private Button btn_foodM4;
    @FXML
    private Button btn_foodM5m;
    @FXML
    private Label lbl_foodM5num;
    @FXML
    private Button btn_foodM5;
    @FXML
    private Button btn_foodM6m;
    @FXML
    private Label lbl_foodM6num;
    @FXML
    private Button btn_foodM6;
    @FXML
    private TextField txt_shoppingaddress;
    @FXML
    private TableView<cratdata> tableview_displayshopping;
    @FXML
    private Label lbl_shoppingsubtotal;
    @FXML
    private Label lbl_shoppingtransport;
    @FXML
    private Label lbl_shoppingtotal;
    @FXML
    private Button btn_shoppingcheck;
    @FXML
    private Label lbl_ordername;
    @FXML
    private Label lbl_ordernumber1;
    @FXML
    private TableView<orderdata> tableview_displayorder;
    @FXML
    private TextField txt_ordernumber;
    @FXML
    private Button btn_ordernumbersearch;
    @FXML
    private TextArea txt_displayordermenu;
    @FXML
    private Label lbl_customername;
    @FXML
    private Label lbl_customernumber;
    @FXML
    private TextField txt_customername;
    @FXML
    private TextField txt_customeraccount;
    @FXML
    private PasswordField txt_customerpassword;
    @FXML
    private PasswordField txt_customerpasswordagain;
    @FXML
    private TextField txt_customeraddress;
    @FXML
    private TextField txt_customerphone;
    @FXML
    private Button btn_okrenew;
    @FXML
    private Button btn_cancelrenew;
    private DBMenu model;
    private DBOrder modelorder;
    private static ObservableList<cratdata> shoppingcrat = FXCollections.observableArrayList();
    private static ObservableList<orderdata> order = FXCollections.observableArrayList();
    @FXML
    private Button btn_foodM1clean;
    @FXML
    private Button btn_foodM2clean;
    @FXML
    private Button btn_foodM3clean;
    @FXML
    private Button btn_foodM4clean;
    @FXML
    private Button btn_foodM5clean;
    @FXML
    private Button btn_foodM6clean;
    @FXML
    private Button btn_close;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableview_displayshopping.getItems().clear();
        tableview_displayorder.getItems().clear();
        DBMenu.connect();
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        String number = String.format("%d", customerdata);
        txt_menuaddress.setText(DBMenu.getaddress());
        txt_shoppingaddress.setText(DBMenu.getaddress());
        txt_customeraddress.setText(DBMenu.getaddress());
        txt_customeraccount.setText(DBMenu.getaccount());
        txt_customername.setText(DBMenu.getname());
        txt_customerphone.setText(DBMenu.getphone());
        lbl_customername.setText(DBMenu.getname());
        lbl_ordername.setText(DBMenu.getname());
        lbl_customernumber.setText(number);
        lbl_ordernumber1.setText(number);
        model = new DBMenu();
        modelorder = new DBOrder();
        DBMenu.settable(tableview_displayshopping);
        DBOrder.plusorder(tableview_displayshopping, order);
        DBOrder.setordertable(tableview_displayorder);
    }

    public static int getcustomerdata() {
        return customerdata;
    }

    @FXML
    private void atn_minusOne(ActionEvent event) {
        Button btn = (Button) event.getSource();
        switch (btn.getId()) {
            case "btn_foodM1m":
                model.minus1(0);
                lbl_foodM1num.setText(model.foodnum(0));
                break;
            case "btn_foodM2m":
                model.minus1(1);
                lbl_foodM2num.setText(model.foodnum(1));
                break;
            case "btn_foodM3m":
                model.minus1(2);
                lbl_foodM3num.setText(model.foodnum(2));
                break;
            case "btn_foodM4m":
                model.minus1(3);
                lbl_foodM4num.setText(model.foodnum(3));
                break;
            case "btn_foodM5m":
                model.minus1(4);
                lbl_foodM5num.setText(model.foodnum(4));
                break;
            case "btn_foodM6m":
                model.minus1(5);
                lbl_foodM6num.setText(model.foodnum(5));
                break;
        }
        tableview_displayshopping.getItems().clear();
        DBMenu.pluscart(shoppingcrat, lbl_shoppingsubtotal, lbl_shoppingtransport, lbl_shoppingtotal);
    }

    @FXML
    private void atn_addOne(ActionEvent event) {
        Button btn = (Button) event.getSource();
        switch (btn.getId()) {
            case "btn_foodM1":
                model.add1(0);
                lbl_foodM1num.setText(model.foodnum(0));
                break;
            case "btn_foodM2":
                model.add1(1);
                lbl_foodM2num.setText(model.foodnum(1));
                break;
            case "btn_foodM3":
                model.add1(2);
                lbl_foodM3num.setText(model.foodnum(2));
                break;
            case "btn_foodM4":
                model.add1(3);
                lbl_foodM4num.setText(model.foodnum(3));
                break;
            case "btn_foodM5":
                model.add1(4);
                lbl_foodM5num.setText(model.foodnum(4));
                break;
            case "btn_foodM6":
                model.add1(5);
                lbl_foodM6num.setText(model.foodnum(5));
                break;
        }
        tableview_displayshopping.getItems().clear();
        DBMenu.pluscart(shoppingcrat, lbl_shoppingsubtotal, lbl_shoppingtransport, lbl_shoppingtotal);
    }

    private void atn_plusCart(ActionEvent event) {
        tableview_displayshopping.getItems().clear();
        DBMenu.pluscart(shoppingcrat, lbl_shoppingsubtotal, lbl_shoppingtransport, lbl_shoppingtotal);
    }

    @FXML
    private void atn_shoppingcheck(ActionEvent event) {
        if (DBMenu.check()) {
            modelorder = new DBOrder();
            DBOrder.plusorder(tableview_displayshopping, order);
            tableview_displayshopping.getItems().clear();
            DBMenu.checkcleanqty();
            lbl_foodM1num.setText(model.foodnum(0));
            lbl_foodM2num.setText(model.foodnum(1));
            lbl_foodM3num.setText(model.foodnum(2));
            lbl_foodM4num.setText(model.foodnum(3));
            lbl_foodM5num.setText(model.foodnum(4));
            lbl_foodM6num.setText(model.foodnum(5));
            lbl_shoppingsubtotal.setText("0");
            lbl_shoppingtotal.setText("0");
            lbl_shoppingtransport.setText("0");
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示訊息");
            alert.setHeaderText("");
            alert.setContentText("結帳完成，請留意訂餐狀態！");
            alert.showAndWait();
        }
    }

    @FXML
    private void btn_ordernumbersearch(ActionEvent event) throws SQLException {
        txt_displayordermenu.setText(DBMenu.reportOrderMenu(customerdata, txt_ordernumber.getText()));
    }

    @FXML
    private void atn_okrenew(ActionEvent event) {
        String name = txt_customername.getText();
        String account = txt_customeraccount.getText();
        String password = txt_customerpassword.getText();
        String passwordagain = txt_customerpasswordagain.getText();
        String address = txt_customeraddress.getText();
        String phone = txt_customerphone.getText();
        boolean success = false;
        boolean nowsameaccount=false;
        boolean nowsamephone=false;
        if(DBMenu.accountrepeat(account)&& account.equals(DBMenu.getaccount())){
        nowsameaccount=true;
        }
        if(DBMenu.phonerepeat(phone)&& phone.equals(DBMenu.getphone())){
        nowsamephone=true;
        }
        if ((name.equals("") == false & account.equals("") == false & password.equals("") == false & passwordagain.equals("") == false
                & address.equals("") == false & phone.equals("") == false & DBReport.accountrepeat(account) == false
                & DBReport.phonerepeat(phone) == false& password.equals(passwordagain)) || (nowsameaccount 
                &name.equals("") == false & account.equals("") == false & password.equals("") == false 
                & passwordagain.equals("") == false& address.equals("") == false & phone.equals("") == false 
                & DBReport.phonerepeat(phone) == false& password.equals(passwordagain))||(nowsamephone
                &name.equals("") == false & account.equals("") == false & password.equals("") == false 
                & passwordagain.equals("") == false& address.equals("") == false & phone.equals("") == false 
                & DBReport.accountrepeat(account) == false& password.equals(passwordagain)) || (nowsameaccount 
                &name.equals("") == false & account.equals("") == false & password.equals("") == false 
                & passwordagain.equals("") == false& address.equals("") == false & phone.equals("") == false 
                & nowsamephone & password.equals(passwordagain))) {
            success = DBMenu.update(name, account, password, address, phone);
        }
        if (success) {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示訊息");
            alert.setHeaderText("");
            alert.setContentText("更新成功！");
            alert.showAndWait();
            txt_menuaddress.setText(DBMenu.getaddress());
            txt_shoppingaddress.setText(DBMenu.getaddress());
            lbl_customername.setText(DBMenu.getname());
            lbl_ordername.setText(DBMenu.getname());
            txt_customerpassword.setText("");
            txt_customerpasswordagain.setText("");
        } else {
            if (DBReport.phonerepeat(phone) && phone.equals(DBMenu.getphone())==false) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("此電話已被註冊！");
                alert.showAndWait();
            } else if (DBReport.accountrepeat(account) && account.equals(DBMenu.getaccount())==false) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("此帳號已被註冊！");
                alert.showAndWait();
            } else if (password.equals(passwordagain)==false) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("確認密碼錯誤！");
                alert.showAndWait();
            } else {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("變更失敗，請確認資料都有正確填寫！");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void atn_cancelrenew(ActionEvent event) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle("提示訊息"); //設定對話框視窗的標題列文字
        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText("確定要取消變更嗎？"); //設定對話框的訊息文字
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代
        System.out.println(rtn);
        if (rtn == ButtonType.OK) {
            lbl_customername.setText(DBMenu.getname());
            txt_customeraddress.setText(DBMenu.getaddress());
            txt_customeraccount.setText(DBMenu.getaccount());
            txt_customername.setText(DBMenu.getname());
            txt_customerpassword.setText("");
            txt_customerpasswordagain.setText("");
            txt_customerphone.setText(DBMenu.getphone());
            lbl_customername.setText(DBMenu.getname());
        }
    }

    @FXML
    private void atn_clean(ActionEvent event) {
        Button btn = (Button) event.getSource();
        switch (btn.getId()) {
            case "btn_foodM1clean":
                DBMenu.setqty(0, 0);
                lbl_foodM1num.setText(model.foodnum(0));
                break;
            case "btn_foodM2clean":
                DBMenu.setqty(1, 0);
                lbl_foodM2num.setText(model.foodnum(1));
                break;
            case "btn_foodM3clean":
                DBMenu.setqty(2, 0);
                lbl_foodM3num.setText(model.foodnum(2));
                break;
            case "btn_foodM4clean":
                DBMenu.setqty(3, 0);
                lbl_foodM4num.setText(model.foodnum(3));
                break;
            case "btn_foodM5clean":
                DBMenu.setqty(4, 0);
                lbl_foodM5num.setText(model.foodnum(4));
                break;
            case "btn_foodM6clean":
                DBMenu.setqty(5, 0);
                lbl_foodM6num.setText(model.foodnum(5));
                break;
        }
        tableview_displayshopping.getItems().clear();
        DBMenu.pluscart(shoppingcrat, lbl_shoppingsubtotal, lbl_shoppingtransport, lbl_shoppingtotal);
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

}
