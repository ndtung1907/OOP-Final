package QuanLyNhanSu;

public class Luong {
    private int maLuong;
    private float ungLuong;
    private float phuCap;
    private float thuong;

    public Luong(int maLuong, float ungLuong, float phuCap, float thuong) {
        this.maLuong = maLuong;
        this.ungLuong = ungLuong;
        this.phuCap = phuCap;
        this.thuong = thuong;
    }

    // Getters and Setters
    public int getMaLuong() { return maLuong; }
    public void setMaLuong(int maLuong) { this.maLuong = maLuong; }

    public float getPhuCap() { return phuCap; }
    public void setPhuCap(float phuCap) { this.phuCap = phuCap; }
    
    public float getUngLuong() { return ungLuong; }
    public void setUngLuong(float ungLuong) { this.ungLuong = ungLuong; }

    public float getThuong() { return thuong; }
    public void setThuong(float thuong) { this.thuong = thuong; }

    @Override
    public String toString() {
        return "Luong [MaLuong=" + maLuong + ", UngLuong=" + ungLuong + ", PhuCap=" + phuCap + ", Thuong=" + thuong + "]";
    }
}