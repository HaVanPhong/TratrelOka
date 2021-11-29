package com.example.traveloka.Volley;

public class ChuyenBay {
    private int id;
    private String noiDi;
    private String noiDen;
    private String gioDi;
    private String gioDen;
    private String hangGhe;
    private long giaVe;

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

    public String getGioDi() {
        return gioDi;
    }

    public void setGioDi(String gioDi) {
        this.gioDi = gioDi;
    }

    public String getGioDen() {
        return gioDen;
    }

    public void setGioDen(String gioDen) {
        this.gioDen = gioDen;
    }

    public String getHangGhe() {
        return hangGhe;
    }

    public void setHangGhe(String hangGhe) {
        this.hangGhe = hangGhe;
    }

    public long getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(long giaVe) {
        this.giaVe = giaVe;
    }

    public ChuyenBay() {
    }

    public ChuyenBay(int id, String noiDi, String noiDen, String gioDi, String gioDen, String hangGhe, long giaVe) {
        this.id = id;
        this.noiDi = noiDi;
        this.noiDen = noiDen;
        this.gioDi = gioDi;
        this.gioDen = gioDen;
        this.hangGhe = hangGhe;
        this.giaVe = giaVe;
    }
}
