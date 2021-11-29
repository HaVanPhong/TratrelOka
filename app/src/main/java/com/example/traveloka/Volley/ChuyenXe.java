package com.example.traveloka.Volley;

public class ChuyenXe {
    private int id;
    private String noiDi;
    private String noiDen;
    private int soGheNgoi;
    private long tienXe;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoiDi() {
        return noiDi;
    }

    public void setNoiDi(String noiDi) {
        this.noiDi = noiDi;
    }

    public String getNoiDen() {
        return noiDen;
    }

    public void setNoiDen(String noiDen) {
        this.noiDen = noiDen;
    }

    public int getSoGheNgoi() {
        return soGheNgoi;
    }

    public void setSoGheNgoi(int soGheNgoi) {
        this.soGheNgoi = soGheNgoi;
    }

    public ChuyenXe() {
    }

    public long getTienXe() {
        return tienXe;
    }

    public void setTienXe(long tienXe) {
        this.tienXe = tienXe;
    }

    public ChuyenXe(int id, String noiDi, String noiDen, int soGheNgoi, long tienXe) {
        this.id = id;
        this.noiDi = noiDi;
        this.noiDen = noiDen;
        this.soGheNgoi = soGheNgoi;
        this.tienXe= tienXe;
    }
}
