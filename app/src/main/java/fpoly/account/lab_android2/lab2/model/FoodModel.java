package fpoly.account.lab_android2.lab2.model;

public class FoodModel {
    String ten;
    String gia;

    public FoodModel(String ten, String gia) {
        this.ten = ten;
        this.gia = gia;
    }

    public FoodModel() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }
}
