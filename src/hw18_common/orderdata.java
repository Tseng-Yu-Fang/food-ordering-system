package hw18_common;

public class orderdata {

    private String ordernum;
    private String orderdate;
    private String orderstate;

    orderdata(String ordernum, String orderdate, String orderstate) {
        this.ordernum = ordernum;
        this.orderdate = orderdate;
        this.orderstate = orderstate;
    }


    public String getOrderstate() {
        return orderstate;
    }

    public String getOrdernum() {
        return ordernum;
    }
    
    public String getOrderdate() {
        return orderdate;
    }

}
