package cn.goitman.pojo;

/**
 * @author Nicky
 * @version 1.0
 * @className Order
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 订单对象
 * @date 2021/7/9 15:43
 */
public class Order {

    // 订单ID
    public String orderId;

    // 订单状态，0：投递中、1：消费中、2：消费成功
    public String orderStatus;

    public Order() {
    }

    public Order(String orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


}
