/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw18_customerregister;

import hw18_common.DBReport;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerRegisterController implements Initializable {

    @FXML
    private Label slogan;
    @FXML
    private GridPane grid;
    @FXML
    private Label lbl_customerpassword;
    @FXML
    private TextField customer_account;
    @FXML
    private TextField customer_password;
    @FXML
    private Label lbl_customerpassword1;
    @FXML
    private Label lbl_customerpassword2;
    @FXML
    private Label lbl_customerpassword21;
    @FXML
    private TextField customer_name;
    @FXML
    private TextField customer_address;
    @FXML
    private TextField customer_phone;
    @FXML
    private Button btn_ok;
    @FXML
    private Button btn_cancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBReport.connect();
        // TODO
    }

    @FXML
    private void atn_ok(ActionEvent event) {
        String name = customer_name.getText();
        String account = customer_account.getText();
        String password = customer_password.getText();
        String address = customer_address.getText();
        boolean success = false;
        String phone = customer_phone.getText();
        if (name.equals("") == false & account.equals("") == false & password.equals("") == false
                & address.equals("") == false & phone.equals("") == false & DBReport.accountrepeat(account) == false
                & DBReport.phonerepeat(phone) == false) {
            success = DBReport.insert(name, account, password, address, phone);
        }
        if (success) {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示訊息");
            alert.setHeaderText("");
            alert.setContentText("註冊成功！");
            alert.showAndWait();
        } else {
            if (DBReport.phonerepeat(phone)) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("此電話已被註冊！");
                alert.showAndWait();
            } else if (DBReport.accountrepeat(account)) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("此帳號已被註冊！");
                alert.showAndWait();
            } else {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("註冊失敗，請確認資料都有正確填寫！");
                alert.showAndWait();
            }
        }

    }

    @FXML
    private void atn_cancel(ActionEvent event) {
        final Alert alert = new Alert(AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle("提示訊息"); //設定對話框視窗的標題列文字
        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText("確定要清空嗎？"); //設定對話框的訊息文字
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代
        System.out.println(rtn);
        if (rtn == ButtonType.OK) {
            //若使用者按下「確定」
            customer_account.setText("");
            customer_address.setText("");
            customer_name.setText("");
            customer_password.setText("");
            customer_phone.setText("");
        }
    }

}
