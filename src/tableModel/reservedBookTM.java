package tableModel;

import java.util.Date;

public class reservedBookTM {
    private String bookId;
    private String bookName;
    private String orderId;
    private String memberName;
    private Date issuedDate;
    private Date recervedDate;
    private String note;
    private int additionalPrice;
    private int totalPrice;


    public reservedBookTM(String bookId, String bookName, String orderId, String memberName, Date issuedDate, Date recervedDate, String note, int additionalPrice, int totalPrice) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.orderId = orderId;
        this.memberName = memberName;
        this.issuedDate = issuedDate;
        this.recervedDate = recervedDate;
        this.note = note;
        this.additionalPrice = additionalPrice;
        this.totalPrice = totalPrice;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getRecervedDate() {
        return recervedDate;
    }

    public void setRecervedDate(Date recervedDate) {
        this.recervedDate = recervedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(int additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "reservedBookTM{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", issuedDate=" + issuedDate +
                ", recervedDate=" + recervedDate +
                ", note='" + note + '\'' +
                ", additionalPrice=" + additionalPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
