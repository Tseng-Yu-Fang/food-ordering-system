package hw18_common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DBOrder {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/hw18db";
    private static final String USER = "hw";
    private static final String PASS = "hw18mis";
    private static Connection conn = null;
    private static Statement state = null;
    private static ResultSet result = null;
    private static String[][] order; //訂單狀態
    private static int customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
    private static TableView<orderdata> table = new TableView<>();
    private static ObservableList<orderdata> orderstate = FXCollections.observableArrayList();

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

    public DBOrder() {
        this.connect();
        customerdata = hw18_customerlogin.CustomerLoginController.getcustomerdata();
        this.setorder();
    }

    public void setorder() {
        int order_length = 0;
        String sql = String.format("SELECT customer.會員編號, `order`.訂單編號, `order`.訂單日期,`order`.訂單狀態 "
                + " FROM customer_order INNER JOIN `order` on customer_order.訂單編號 = `order`.訂單編號 "
                + " INNER JOIN customer ON customer_order.會員編號 = customer.會員編號  "
                + "WHERE customer_order.會員編號='%d'", customerdata);//先判斷會員編號，再回傳對應的訂單
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                order_length++;
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        String[][] order = new String[order_length][4];
        try {
            int i = 0;
            result = state.executeQuery(sql);
            while (result.next()) {
                int custnum = result.getInt(1);
                order[i][0] = String.format("%d", custnum);
                for (int j = 1; j < 4; j++) {
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

    public static void plusorder(TableView table,ObservableList order1) { //加入購物車
        orderstate.clear();
        table.getItems().clear();
        for (int i = 0; i < order.length; i++) {
            orderstate.add(new orderdata(order[i][1], order[i][2], order[i][3]));
        }
        order1 = orderstate;
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

        //addButtonToTable(table);
    }

}
