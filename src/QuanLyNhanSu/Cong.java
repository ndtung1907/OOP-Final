package QuanLyNhanSu;

public class Cong {
    private String maChamCong;
    private String maNhanVien;
    private int ngayCong;
    private int nghiPhep;
    private String maLuong;
    public Cong(String maChamCong, String maNhanVien, int ngayCong, int nghiPhep, String maLuong) {
    	this.maChamCong = maChamCong;
    	this.maNhanVien = maNhanVien;
    	this.ngayCong = ngayCong;
    	this.nghiPhep = nghiPhep;
    	this.maLuong = maLuong;
    }

    // Getters and Setters
    public String getMaChamCong() { return maChamCong; }
    public void setMaChamCong(String maChamCong) { this.maChamCong = maChamCong; }


    public int getNgayCong() { return ngayCong; }
    public void setNgayCong(int ngayCong) { this.ngayCong = ngayCong; }

    public int getNghiPhep() { return nghiPhep; }
    public void setNghiPhep(int nghiPhep) { this.nghiPhep = nghiPhep; }
    
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getMaLuong() { return maLuong; }
    public void setMaLuong(String maLuong) { this.maLuong = maLuong; }
    

    @Override
    public String toString() {
        return "ChamCong [MaChamCong=" + maChamCong + ", NgayCong=" + ngayCong + ", NghiPhep=" + nghiPhep + ", MaLuong=" + maLuong + ", MaNhanVien=" + maNhanVien + "]";
    }
}

