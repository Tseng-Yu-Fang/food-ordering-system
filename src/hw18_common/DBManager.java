package hw18_common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DBManager {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/hw18db";
    private static final String USER = "hw";
    private static final String PASS = "hw18mis";
    private static Connection conn = null;
    private static Statement state = null;
    private static ResultSet result = null;
    private static String[][] order; //訂單狀態
    private static int managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
    private static TableView<orderdata> table = new TableView<>();
    private static TableView<orderdata> tablemenu = new TableView<>();
    private static TableView<customerdata> tablecustomer = new TableView<>();
    private static TableView<customermenudata> tablecustomermenu = new TableView<>();
    private static ObservableList<orderdata> orderstate = FXCollections.observableArrayList();
    private static ObservableList<orderdata> menu = FXCollections.observableArrayList();
    private static ObservableList<customerdata> customer = FXCollections.observableArrayList();
    private static ObservableList<customermenudata> customermenu = FXCollections.observableArrayList();

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

    public DBManager() {
        this.connect();
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        this.setorder();
    }

    public void setorder() {
        int order_length = 0;
        String sql = String.format("SELECT *FROM `order`");//先判斷會員編號，再回傳對應的訂單
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

    public static String getname() {//從編號找到姓名
        String sql = "select * from manager";
        String name = "";
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (managerdata == result.getInt(1)) {
                    name = result.getString(2);
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return name;
    }

    public static void plusorder(TableView table, ObservableList order1) { //加入購物車
        orderstate.clear();
        table.getItems().clear();
        for (int i = 0; i < order.length; i++) {
            orderstate.add(new orderdata(order[i][0], order[i][1], order[i][2]));
        }
        order1 = orderstate;
    }

    public static void plusmenu(TableView table, ObservableList order1) { //加入購物車
        menu.clear();
        table.getItems().clear();
        String sql = "SELECT `order`.訂單編號, customermenu.餐點, customermenu.數量,`order`.訂單狀態 "
                + "FROM customermenu INNER JOIN `order` on customermenu.訂單編號 = `order`.訂單編號 "
                + "WHERE `order`.訂單狀態='準備中'";
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                menu.add(new orderdata(result.getString(1), result.getString(2), String.format("%d", result.getInt(3))));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = menu;
    }

    public static void plusmenusearch(String txtsearch, TableView table, ObservableList order1) { //加入購物車
        menu.clear();
        table.getItems().clear();
        String sql = "SELECT `order`.訂單編號, customermenu.餐點, customermenu.數量,`order`.訂單狀態 "
                + "FROM customermenu INNER JOIN `order` on customermenu.訂單編號 = `order`.訂單編號 "
                + "WHERE `order`.訂單狀態='準備中'";
        sql += String.format(" AND `order`.訂單編號='%s' ", txtsearch);
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                menu.add(new orderdata(result.getString(1), result.getString(2), String.format("%d", result.getInt(3))));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = menu;
    }

    public static void setmenutable(TableView table) { //放入TABLEVIEW
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(menu);

        TableColumn<cratdata, String> colId = new TableColumn<>("訂單編號");
        colId.setCellValueFactory(new PropertyValueFactory<>("ordernum"));
        colId.setPrefWidth(60);
        colId.setMaxWidth(60);
        colId.setMinWidth(60);

        TableColumn<cratdata, String> colDate = new TableColumn<>("餐點");
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderdate"));

        TableColumn<cratdata, String> colNum = new TableColumn<>("數量");
        colNum.setCellValueFactory(new PropertyValueFactory<>("orderstate"));
        colNum.setPrefWidth(60);
        colNum.setMaxWidth(60);
        colNum.setMinWidth(60);

        table.getColumns().addAll(colId, colDate, colNum);

    }

    public static void pluscustomer(TableView table, ObservableList order1) { //加入購物車
        customer.clear();
        table.getItems().clear();
        String sql = "SELECT * FROM customer";
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                customer.add(new customerdata(String.format("%d", result.getInt(1)), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = customer;
    }

    public static void pluscustomersearch(String search, TableView table, ObservableList order1) { //加入購物車
        customer.clear();
        table.getItems().clear();
        String sql = "SELECT * FROM customer";
        sql += String.format(" WHERE 姓名='%s' ", search);
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                customer.add(new customerdata(String.format("%d", result.getInt(1)), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = customer;
    }
    
     public static void pluscustomeridsearch(String search, TableView table, ObservableList order1) { //加入購物車
        customer.clear();
        table.getItems().clear();
        String sql = "SELECT * FROM customer";
        sql += String.format(" WHERE 會員編號='%s' ", search);
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                customer.add(new customerdata(String.format("%d", result.getInt(1)), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = customer;
    }

    public static void setcustomertable(TableView table) { //放入TABLEVIEW
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(customer);

        TableColumn<cratdata, String> colId = new TableColumn<>("會員編號");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(60);
        colId.setMaxWidth(60);
        colId.setMinWidth(60);

        TableColumn<cratdata, String> colDate = new TableColumn<>("姓名");
        colDate.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setPrefWidth(90);
        colDate.setMaxWidth(90);
        colDate.setMinWidth(90);

        TableColumn<cratdata, String> colNum = new TableColumn<>("帳號");
        colNum.setCellValueFactory(new PropertyValueFactory<>("account"));
        colNum.setPrefWidth(80);
        colNum.setMaxWidth(80);
        colNum.setMinWidth(80);

        TableColumn<cratdata, String> colPassword = new TableColumn<>("密碼");
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colPassword.setPrefWidth(80);
        colPassword.setMaxWidth(80);
        colPassword.setMinWidth(80);

        TableColumn<cratdata, String> colAddress = new TableColumn<>("外送住址");
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<cratdata, String> colPhone = new TableColumn<>("電話");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPhone.setPrefWidth(85);
        colPhone.setMaxWidth(85);
        colPhone.setMinWidth(85);

        table.getColumns().addAll(colId, colDate, colNum, colPassword, colAddress, colPhone);

    }
    
    public static void pluscustomermenu(TableView table, ObservableList order1) { //加入購物車
        customermenu.clear();
        table.getItems().clear();
        String sql = "SELECT * FROM customermenu";
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                customermenu.add(new customermenudata(String.format("%d", result.getInt(1)), result.getString(2), result.getString(3), String.format("%d", result.getInt(4)), String.format("%d", result.getInt(5))));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = customer;
    }
    
    public static void pluscustomermenusearch(String search, TableView table, ObservableList order1) { //加入購物車
        customermenu.clear();
        table.getItems().clear();
        String sql = "SELECT * FROM customermenu";
        sql += String.format(" WHERE 訂單編號='%s' ", search);
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                customermenu.add(new customermenudata(String.format("%d", result.getInt(1)), result.getString(2), result.getString(3), String.format("%d", result.getInt(4)), String.format("%d", result.getInt(5))));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = customermenu;
    }
    
    public static void pluscustomermenuidsearch(String search, TableView table, ObservableList order1) { //加入購物車
        customermenu.clear();
        table.getItems().clear();
        String sql = "SELECT * FROM customermenu";
        sql += String.format(" WHERE 會員編號='%s' ", search);
        managerdata = hw18_managerlogin.ManagerLoginController.getmanagerdata();
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                customermenu.add(new customermenudata(String.format("%d", result.getInt(1)), result.getString(2), result.getString(3), String.format("%d", result.getInt(4)), String.format("%d", result.getInt(5))));
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        order1 = customermenu;
    }
    
    public static void setcustomermenutable(TableView table) { //放入TABLEVIEW
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(customermenu);

        TableColumn<cratdata, String> colId = new TableColumn<>("會員編號");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(60);
        colId.setMaxWidth(60);
        colId.setMinWidth(60);

        TableColumn<cratdata, String> colDate = new TableColumn<>("訂單編號");
        colDate.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<cratdata, String> colNum = new TableColumn<>("餐點");
        colNum.setCellValueFactory(new PropertyValueFactory<>("account"));


        TableColumn<cratdata, String> colPassword = new TableColumn<>("數量");
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));


        TableColumn<cratdata, String> colAddress = new TableColumn<>("小計");
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        table.getColumns().addAll(colId, colDate, colNum, colPassword, colAddress);

    }

    public static void addButtonToTable(TableView table) {
        TableColumn<cratdata, Void> colBtn = new TableColumn("");

        Callback<TableColumn<cratdata, Void>, TableCell<cratdata, Void>> cellFactory = new Callback<TableColumn<cratdata, Void>, TableCell<cratdata, Void>>() {
            @Override
            public TableCell<cratdata, Void> call(final TableColumn<cratdata, Void> param) {
                final TableCell<cratdata, Void> cell = new TableCell<cratdata, Void>() {
                    private final Button btn = new Button("出餐");

                    {
                        btn.setStyle("-fx-background-color: #DDFFA4;-fx-font-size: 12;-fx-background-radius:10;");
                        btn.setOnAction((ActionEvent event) -> {
                            int index = getTableRow().getIndex();
                            order[index][2] = "已出餐";
                            DBManager.update(index);
                            DBManager.plusorder(table, orderstate);
                            DBManager.plusmenu(tablemenu, menu);
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
        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
        colBtn.setPrefWidth(50);
        colBtn.setMaxWidth(50);
        colBtn.setMinWidth(50);

        // TODO
    }
    //更新這一筆

    public static void update(int index) {
        String ordernum = order[index][0];
        System.out.println(order[index][0]);
        String sql = String.format("update `order` SET 訂單狀態='已出餐' where 訂單編號='%s'", ordernum);
        try {
            state.execute(sql);
            result = state.executeQuery("SELECT * FROM `order`");//最好是撈新的資料
            System.out.println("資料庫update order成功!");
        } catch (SQLException ex) {
            System.out.println("資料庫 update 操作異常:" + ex.toString());
        }
    }

    public static void setordertable(TableView table) { //放入TABLEVIEW
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(orderstate);

        TableColumn<cratdata, String> colId = new TableColumn<>("訂單編號");
        colId.setCellValueFactory(new PropertyValueFactory<>("ordernum"));
        colId.setPrefWidth(60);
        colId.setMaxWidth(60);
        colId.setMinWidth(60);

        TableColumn<cratdata, String> colDate = new TableColumn<>("訂單日期");
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderdate"));

        TableColumn<cratdata, String> colNum = new TableColumn<>("訂單狀態");
        colNum.setCellValueFactory(new PropertyValueFactory<>("orderstate"));
        colNum.setPrefWidth(60);
        colNum.setMaxWidth(60);
        colNum.setMinWidth(60);

        table.getColumns().addAll(colId, colDate, colNum);

        addButtonToTable(table);

    }

}
