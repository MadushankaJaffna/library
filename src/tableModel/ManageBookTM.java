package tableModel;

public class ManageBookTM {
    private String id;
    private String name;
    private String Auther;
    private int price;
    private int qty;
    private int exactQty;

    public ManageBookTM(){

    }

    public ManageBookTM(String id, String name, String auther, int price, int qty, int exactQty) {
        this.id = id;
        this.name = name;
        Auther = auther;
        this.price = price;
        this.qty = qty;
        this.exactQty = exactQty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuther() {
        return Auther;
    }

    public void setAuther(String auther) {
        Auther = auther;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getExactQty() {
        return exactQty;
    }

    public void setExactQty(int exactQty) {
        this.exactQty = exactQty;
    }

    @Override
    public String toString() {
        return "ManageBookTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", Auther='" + Auther + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", exactQty=" + exactQty +
                '}';
    }
}
