package hw18_common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DBMenu {

    //注意此處連線的資料庫是完整資料的studentdb2
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/hw18db";
    private static final String USER = "hw";
    private static final String PASS = "hw18mis";
    private static Connection conn = null;
    private static Statement state = null;
    private static ResultSet result = null;
    private static int number = 0; //會員編號
    private static String[][] menu; //菜單
    private static String[][] order; //訂單狀態
    private static int[] qty; //數量
    private static int customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
    private static TableView<cratdata> table = new TableView<>();
    private static ObservableList<cratdata> shoppingcrat = FXCollections.observableArrayList();
    //必須連線一次  使用全域變數conn, state

    public static boolean connect() {

        boolean sucess = false;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sucess = true;
            System.out.println("資料庫連線成功");
        } catch (SQLException ex) {
            System.out.println("資料庫connect操作異常\n" + ex.toString());
        }
        return sucess;
    }

    public DBMenu() {
        this.readMenu();
    }

    public static void setqty(int idx, int num) {
        qty[idx] = num;
    }

    public void readMenu() {

        int menu_length = 0;
        String sql = "select * from menu";
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                menu_length++;
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        String[][] menu = new String[menu_length][3];
        try {
            int i = 0;
            result = state.executeQuery(sql);
            while (result.next()) {
                for (int j = 0; j < 2; j++) {
                    menu[i][j] = result.getString(j + 1);
                    System.out.print(menu[i][j]);
                }
                menu[i][2] = String.format("%d", result.getInt(3));
                System.out.print(menu[i][2]);
                System.out.println("");
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        this.menu = menu;
        qty = new int[menu.length];
        System.out.println("菜單讀取完成");
    }

    public void add1(int idx) {
        qty[idx] += 1;
    }

    public String foodnum(int idx) {
        String num = String.format("%d", qty[idx]);
        return num;
    }

    public static void foodnum(Label[] label, int idx) {
        String num = String.format("%d", qty[idx]);
        label[idx].setText(num);
    }

    public void minus1(int idx) {
        if (qty[idx] > 0) {
            qty[idx] -= 1;
        }
    }

    public static void pluscart(ObservableList shop, Label lbl_subtotal, Label lbl_tansport, Label lbl_total) { //先判斷哪個使用者，在加入購物車
        int subtotal1 = 0;
        int total1 = 0;
        int transport = 0;
        for (int i = 0; i < menu.length; i++) {
            String num = String.format("%d", qty[i]);
            if (qty[i] != 0) {
                transport = 75;
                String price = "$" + menu[i][2];
                int price1 = Integer.parseInt(menu[i][2]);
                int money1 = qty[i] * price1;
                subtotal1 = subtotal1 + money1;
                String money = String.format("%d", money1);
                shoppingcrat.add(new cratdata(menu[i][1], price, num, money));
            }
        }
        total1 = subtotal1 + transport;
        String subtotal = String.format("%d", subtotal1);
        String total = String.format("%d", total1);
        String transport1 = String.format("%d", transport);
        lbl_subtotal.setText(subtotal);
        lbl_tansport.setText(transport1);

        lbl_total.setText(total);
        shop = shoppingcrat;
    }

    public static boolean check() { //先判斷哪個使用者，在加入購物車
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        int nomenu = 0;
        boolean success = false;
        StringBuilder sb = new StringBuilder();
        sb.append("yyyy年MM月dd日HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
        String dateString = sdf.format(new Date());
        System.out.println(dateString);
        for (int i = 0; i < menu.length; i++) {
            if (qty[i] != 0) {
                int price = Integer.parseInt(menu[i][2]);
                String id = getmenunum();
                String sql = String.format("Insert into customermenu(會員編號,訂單編號,餐點,數量,小計) "
                        + "values ('%d','%s','%s','%d','%d')", customerdata, id, menu[i][1], qty[i], (qty[i] * price));
                try {
                    state.execute(sql);
                    result = state.executeQuery("SELECT * FROM customer");
                    success = true;
                } catch (SQLException ex) {
                    System.out.println("資料庫 insert 操作異常:" + ex.toString());
                }
            } else {
                nomenu++;
            }
            if (nomenu == menu.length) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示訊息");
                alert.setHeaderText("");
                alert.setContentText("請下單在結帳！");
                alert.showAndWait();
            }
        }
        if (success) {
            String sql = "select * from customer";
            int num = 0;
            try {
                result = state.executeQuery(sql);
                while (result.next()) {
                    if (customerdata == result.getInt(1)) {
                        num = result.getInt(7);
                    }
                }
            } catch (SQLException ex) {
                System.out.println("資料庫 select 出問題:\n" + ex.toString());
            }
            num++;
            String id = getmenunum();
            String orderstate = "準備中";
            String sql_update = String.format("update customer set 訂購次數 = '%d' where 會員編號 = '%d'", num, customerdata);
            insertorder(id, dateString, orderstate);
            insertcustomermenu_order(customerdata, id);
            try {
                state.execute(sql_update);
                System.out.println("訂購次數更新成功");
            } catch (SQLException ex) {
                System.out.println("資料庫 update 出問題:\n" + ex.toString());
            }
        }
        return success;
    }

    public static void insertorder(String ordernum, String date, String orderstate) { //新增order
        String sql = String.format("Insert into `order`(訂單編號,訂單日期,訂單狀態) "
                + "values ('%s','%s','%s')", ordernum, date, orderstate);
        try {
            state.execute(sql);
            result = state.executeQuery("SELECT * FROM customer");
            System.out.println("資料庫order新增成功");
        } catch (SQLException ex) {
            System.out.println("資料庫 insertorder 操作異常:" + ex.toString());
        }
    }

    public static void insertcustomermenu_order(int customerdata, String ordernum) { //新增到會員編號與訂單編號的關聯表
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        String sql = String.format("Insert into `customer_order`(會員編號,訂單編號) "
                + "values ('%d','%s')", customerdata, ordernum);
        try {
            state.execute(sql);
            result = state.executeQuery("SELECT * FROM customer");
            System.out.println("資料庫customer_order新增成功");
        } catch (SQLException ex) {
            System.out.println("資料庫 customer_order 操作異常:" + ex.toString());
        }
    }

    public static String getmenunum() { //訂單編號
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        String sql = "select * from customer";
        int num = 0;
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (customerdata == result.getInt(1)) {
                    num = result.getInt(7);
                    num++;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }

        String id = String.format("%dD%d", customerdata, num);
        return id;
    }

    public static void checkcleanqty() {//結帳後清空數量
        for (int i = 0; i < qty.length; i++) {
            setqty(i, 0);
        }
    }

    public void setorder() {
        int order_length = 0;
        String sql = "select * from order";
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                order_length++;
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        String[][] order = new String[order_length][3];
        try {
            int i = 0;
            result = state.executeQuery(sql);
            while (result.next()) {
                for (int j = 0; j < 3; j++) {
                    order[i][j] = result.getString(j + 1);
                    System.out.print(order[i][j]);
                }
                System.out.println("");
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        this.order = order;
        System.out.println("訂單狀態讀取完成");
    }

    public static void settable(TableView table) { //放入TABLEVIEW
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(shoppingcrat);

        TableColumn<cratdata, String> colId = new TableColumn<>("餐點");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<cratdata, String> colName = new TableColumn<>("單價");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<cratdata, String> colNum = new TableColumn<>("數量");
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<cratdata, String> colMoney = new TableColumn<>("小計");
        colMoney.setCellValueFactory(new PropertyValueFactory<>("money"));

        table.getColumns().addAll(colId, colName, colNum, colMoney);

        //addButtonToTable(table);
    }

    public void addButtonToTable() {
        TableColumn<cratdata, Void> colBtn = new TableColumn("");

        Callback<TableColumn<cratdata, Void>, TableCell<cratdata, Void>> cellFactory = new Callback<TableColumn<cratdata, Void>, TableCell<cratdata, Void>>() {
            @Override
            public TableCell<cratdata, Void> call(final TableColumn<cratdata, Void> param) {
                final TableCell<cratdata, Void> cell = new TableCell<cratdata, Void>() {

                    private final Button btn = new Button("刪除");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            int index = getTableRow().getIndex();
                            System.out.println(index);
                            DBMenu.setqty(index, 0);
                        });
                    }

                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        //colBtn.setCellFactory(cellFactory);
        //tableview_displayshopping.getColumns().add(colBtn);
        // TODO
    }

    public static String getaddress() {//從編號找到地址
        String sql = "select * from customer";
        String address = "";
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (customerdata == result.getInt(1)) {
                    address = result.getString(5);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return address;
    }

    public static String getname() {//從編號找到姓名
        String sql = "select * from customer";
        String address = "";
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (customerdata == result.getInt(1)) {
                    address = result.getString(2);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return address;
    }

    public static String getaccount() {//從編號找到帳號
        String sql = "select * from customer";
        String address = "";
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (customerdata == result.getInt(1)) {
                    address = result.getString(3);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return address;
    }

    public static String getphone() {//從編號找到電話
        String sql = "select * from customer";
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        String address = "";
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (customerdata == result.getInt(1)) {
                    address = result.getString(6);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return address;
    }

    public static int getnumber() {//正確的話傳回編號
        return number;
    }

    public static String customergetpassword(String phone) {//從電話去找會員密碼
        String password = "查無資料";
        String sql = "select * from customer";
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (phone.equals(result.getString(6))) {
                    password = result.getString(4);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return password;
    }

    public static String managergetpassword(String phone) {//從電話去找管理員密碼，存放電話的資料庫位置不同所以分開寫
        String password = "查無資料";
        String sql = "select * from manager";
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (phone.equals(result.getString(5))) {
                    password = result.getString(4);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return password;
    }

    public static int showCustomerLast() {//回傳最後一位的編號
        String sql = "select * from customer";
        int id = 0;
        try {
            result = state.executeQuery(sql);
            result.last();
            id = Integer.parseInt(result.getString(1));
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return id;
    }

    public static boolean accountrepeat(String account) {//判斷帳號有無重複
        String sql = "select * from customer";
        boolean success = false;
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (account.equals(result.getString(3))) {
                    success = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        System.out.println("帳號是否重複?" + success);
        return success;
    }

    public static boolean phonerepeat(String phone) {//判斷電話有無重複
        String sql = "select * from customer";
        boolean success = false;
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (phone.equals(result.getString(6))) {
                    success = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        System.out.println("電話是否重複?" + success);
        return success;
    }
    //更新這一筆
    public static boolean update(String name, String account, String password, String address,String phone) {
        boolean sucess = false;
        String sql = String.format("update customer SET 姓名='%s',帳號='%s',密碼='%s',外送住址='%s',電話='%s' where 會員編號='%d'", name,account,password,address, phone, customerdata);
        try {
            state.execute(sql);
            result = state.executeQuery("SELECT * FROM customer");//最好是撈新的資料
            sucess = true;
        } catch (SQLException ex) {
            System.out.println("資料庫 update 操作異常:" + ex.toString());
        }
        return sucess;
    }

    public static boolean insert(String name, String account, String password, String address, String phone) { //註冊會員
        boolean sucess = false;
        int idlast = DBMenu.showCustomerLast();
        String sql = String.format("Insert into customer(會員編號,姓名,帳號,密碼,外送住址,電話) "
                + "values ('%d','%s','%s','%s','%s','%s')", (idlast + 1), name, account, password, address, phone);
        try {
            state.execute(sql);
            result = state.executeQuery("SELECT * FROM customer");
            sucess = true;
        } catch (SQLException ex) {
            System.out.println("資料庫 insert 操作異常:" + ex.toString());
        }
        return sucess;
    }

    //學生修課報表-----
    public static String reportOrderMenu(int customern, String ordernum) throws SQLException {
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        System.out.println(customerdata);
        if (customern != customerdata) {
            return "查無資料";
        }
        String msg;
        String report = "";
        String date = "";
        String orderstate = "";
        String ordern;
       boolean search=false;
        String sql1 = String.format("SELECT * FROM customer_order WHERE customer_order.會員編號='%d'",customerdata);
        result = state.executeQuery(sql1);
        while (result.next()) {
            System.out.println(ordernum.equals(result.getString(2)));
            System.out.println(result.getString(2));
            if (ordernum.equals(result.getString(2))) {
                search=true;
            }
        }
        if(search==false){
        return "查無資料";
        }
        String sql = String.format("SELECT customermenu.會員編號, customermenu.訂單編號,customermenu.餐點"
                + ",customermenu.數量,customermenu.小計,`order`.訂單日期,`order`.訂單狀態 "
                + " FROM customermenu INNER JOIN `order` on customermenu.訂單編號 = `order`.訂單編號"
                + " WHERE customermenu.訂單編號='%s'", ordernum
        );
        try {
            result = state.executeQuery(sql);
            result.next();
            ordern = result.getString(2);
            date = result.getString(6);
            orderstate = result.getString(7);
            report += "訂單編號 : " + ordern + "\n";
            report += "訂單日期 : " + date + "\n";
            report += "訂單狀態 : " + orderstate + "\n";
            System.out.println(ordernum.equals(result.getString(2)));
        } catch (SQLException ex) {
            System.out.println("資料庫reportOrdeMenu讀取失敗" + ex.toString());
        }

        try {
            result = state.executeQuery(sql);

            while (result.next()) {
                if (ordernum.equals(result.getString(2)) == false) {
                    return "查無資料!";
                } else {
                    msg = String.format("%s", result.getString(3));
                    msg += String.format(" * %d\t\t", result.getInt(4));
                    msg += String.format("$%d\t\n", result.getInt(5));
                    report += "---------------------------------------\n";
                    report += msg;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 customermenu 讀取異常:" + ex.toString());
        }
        return report;
    }

    
}
