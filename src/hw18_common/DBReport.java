package hw18_common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBReport {

    //注意此處連線的資料庫是完整資料的studentdb2
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/hw18db";
    private static final String USER = "hw";
    private static final String PASS = "hw18mis";
    private static Connection conn = null;
    private static Statement state = null;
    private static ResultSet result = null;
    private static int number = 0;

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

    public static boolean boollogin(String table, String account, String password) {//判斷帳號密碼是否正確
        // select
        String sql = "select * from " + table;
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                System.out.printf("姓名:%s 帳號:%s 密碼:%s\n",
                        result.getString(2),
                        result.getString(3),
                        result.getString(4));
                if (account.equals(result.getString(3)) & password.equals(result.getString(4))) {
                    number = Integer.parseInt(result.getString(1));
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return false;
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
    
    public static int showCustomerLast() {
        String sql = "select * from customer";
        int id=0;
        try {
            result = state.executeQuery(sql);
            result.last();
            id=Integer.parseInt(result.getString(1));
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        return id;
    }
    public static boolean accountrepeat(String account){//判斷帳號有無重複
        String sql = "select * from customer";
        boolean success=false;
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (account.equals(result.getString(3))) {
                    success=true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        System.out.println("帳號是否重複?"+success);
        return success;
    }
    public static boolean phonerepeat(String phone){//判斷電話有無重複
        String sql = "select * from customer";
        boolean success=false;
        try {
            result = state.executeQuery(sql);
            while (result.next()) {
                if (phone.equals(result.getString(6))) {
                    success=true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("資料庫 select 出問題:\n" + ex.toString());
        }
        System.out.println("電話是否重複?"+success);
        return success;
    }
    
    public static boolean insert(String name, String account, String password,String address,String phone) { //註冊會員
        boolean sucess = false;
        int idlast=DBReport.showCustomerLast();
        String sql = String.format("Insert into customer(會員編號,姓名,帳號,密碼,外送住址,電話) "
                + "values ('%d','%s','%s','%s','%s','%s')",(idlast+1) ,name, account, password,address,phone);
        try {
            state.execute(sql);
            result= state.executeQuery("SELECT * FROM customer");
            sucess = true;
        } catch (SQLException ex) {
            System.out.println("資料庫 insert 操作異常:" + ex.toString());
        }
        return sucess;
    }
    //更新這一筆
    public static boolean update(String name, String account, String password,String againpasswrod, String address,String phone) {
        boolean sucess = false;
        String sql = String.format("update customer SET 姓名='%s',帳號='%s',密碼='%s',外送住址='%s',電話='%s' where 會員編號='%d'", name,account,password,address, phone, number);
        try {
            state.execute(sql);
            result = state.executeQuery("SELECT * FROM student");//最好是撈新的資料
            sucess = true;
        } catch (SQLException ex) {
            System.out.println("資料庫 update 操作異常:" + ex.toString());
        }
        return sucess;
    }
    

    //學生修課報表-----
    public static String reportStudClass(String student_id) {
        String msg;
        String report = "";
        ResultSet rs;

        String sql = String.format("SELECT student_class.student_id, student.name, class_name, class.credit, score "
                + "FROM student_class Inner join student on student_class.student_id = student.student_id "
                + "INNER JOIN class ON student_class.class_id = class.class_id "
                + "WHERE student_class.student_id='%s'", student_id);

        try {
            //剛開始已經連線過一次，使用靜態全域變數，此處可以不必再連一次
            //Connection conn = DriverManager.getConnection(DB_URL, USER , PASS); //使用靜態全域變數，不必再連一次
            //state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
            if (rs.next() == false) {
                return "查無資料!";
            } else {
                msg = String.format("學號:%s\t姓名:%s\n", rs.getString("student_id"), rs.getString("name"));
                report += msg;
                report += "課名\t學分\t分數\n";
                report += "-------------------------------------------------------------\n";
                do {
                    msg = String.format("%s\t%s\t%s\n", rs.getString("class_name"), rs.getString("credit"), rs.getString("score"));
                    report += msg;
                } while (rs.next());
                report += "-------------------------------------------------------------\n";

                String sql2 = String.format("SELECT COUNT(*) as 修課數, SUM(credit) as 總學分, AVG(score) as 平均 "
                        + "FROM student_class "
                        + "INNER JOIN class on student_class.class_id = class.class_id "
                        + "WHERE student_id='%s'", student_id);
                rs = state.executeQuery(sql2);
                rs.first();

                msg = String.format("修課數:%s\t總學分:%s\t平均成績:%s\n", rs.getString("修課數"), rs.getString("總學分"), rs.getString("平均"));
                report += msg;
            }
        } catch (SQLException ex) {
            System.out.println("資料庫操作異常:" + ex.toString());
        }
        return report;
    }

    //導師的導生資料列印
    public static String reportTeacherStud(String teacher_id) {

        String msg;
        String report = "";
        ResultSet rs;

        String sql = String.format("SELECT student_advisor.student_id, student.name "
                + "FROM  student_advisor  INNER JOIN "
                + "student ON student_advisor.student_id = student.student_id "
                + "WHERE student_advisor.teacher_id = '%s'", teacher_id);
        String sql_teacher = String.format("SELECT name, room, dept "
                + "FROM  teacher "
                + "WHERE teacher_id = '%s'", teacher_id);

        try {
            //conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = state.executeQuery(sql_teacher);

            if (rs.next() == false) {
                return "查無資料!";
            } else {
                rs.first();
                msg = String.format("教師:%s\t研究室:%s\t部門:%s\n", rs.getString("name"), rs.getString("room"), rs.getString("dept"));
                report += msg;

                rs = state.executeQuery(sql);
                rs.first();
                report += "學號\t姓名\n";
                report += "-------------------------------------------------------------\n";
                do {
                    msg = String.format("%s\t%s\n", rs.getString("student_id"), rs.getString("name"));
                    report += msg;
                } while (rs.next());
                report += "-------------------------------------------------------------\n";
            }
        } catch (SQLException ex) {
            System.out.println("資料庫操作異常:" + ex.toString());
        }
        return report;
    }

    //列印所有學生與其導師資料
    public static String reportStudAdvisor() {

        String msg;
        String report = "";
        ResultSet rs;
        String sql = String.format("SELECT student.student_id AS '學號', student.name AS '姓名', teacher.Name AS '老師'\n"
                + "FROM student_advisor\n"
                + "INNER JOIN student ON student_advisor.Student_id = student.student_id \n"
                + "INNER JOIN teacher ON student_advisor.Teacher_id = teacher.Teacher_id;");

        try {
            //conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
            rs.first();
            report += "學號\t姓名\t導師\n";
            report += "-------------------------------------------------------------\n";
            do {
                msg = String.format("%s\t%s\t%s\n", rs.getString("student.student_id"), rs.getString("姓名"), rs.getString("老師"));
                report += msg;
            } while (rs.next());
            report += "-------------------------------------------------------------\n";

        } catch (SQLException ex) {
            System.out.println("資料庫操作異常:" + ex.toString());
        }
        return report;
    }
}
