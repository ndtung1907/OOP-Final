package QuanLyNhanSu;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String ngaySinh;
    private String soCMND;
    private String queQuan;
    private String soDienThoai;
    private String gioiTinh;
    private String diaChi;
    private String maPhongBan;

    public NhanVien(String maNhanVien, String hoTen, String ngaySinh, String soCMND, String queQuan, String soDienThoai, String gioiTinh, String diaChi, String maPhongBan) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.soCMND = soCMND;
        this.queQuan = queQuan;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.maPhongBan = maPhongBan;
    }

    // Getters and Setters
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getSoCMND() { return soCMND; }
    public void setSoCMND(String soCMND) { this.soCMND = soCMND; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }


    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getMaPhongBan() { return maPhongBan; }
    public void setMaPhongBan(String maPhongBan) { this.maPhongBan = maPhongBan; }

    @Override
    public String toString() {
        return "NhanVien [MaNhanVien=" + maNhanVien + ", HoTen=" + hoTen + ", NgaySinh=" + ngaySinh + ", SoCMND=" + soCMND + ", QueQuan=" + queQuan + ", SoDienThoai=" + soDienThoai + ", GioiTinh=" + gioiTinh + ", DiaChi=" + diaChi + ", MaPhongBan = "+"]";
    }
}
