package tableModel;

import java.util.Date;

public class issuBookTM {
    private Date date;
    private String orderId;
    private String bookId;
    private String customerId;

    public issuBookTM(){

    }

    public issuBookTM(Date date, String orderId, String bookId, String customerId) {
        this.date = date;
        this.orderId = orderId;
        this.bookId = bookId;
        this.customerId=customerId;

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "issuBookTM{" +
                "date=" + date +
                ", orderId='" + orderId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }

}
