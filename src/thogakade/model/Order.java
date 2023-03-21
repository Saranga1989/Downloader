/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.model;

import java.util.Date;
import java.util.List;


/**
 *
 * @author cmjd
 */
public class Order {
    private String orderId;
    private Date date;
    private String itemCode;
    private List<OrderDetail> listOrderDetail;

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the listOrderDetail
     */
    public List<OrderDetail> getListOrderDetail() {
        return listOrderDetail;
    }

    /**
     * @param listOrderDetail the listOrderDetail to set
     */
    public void setListOrderDetail(List<OrderDetail> listOrderDetail) {
        this.listOrderDetail = listOrderDetail;
    }
    
    
     
}
