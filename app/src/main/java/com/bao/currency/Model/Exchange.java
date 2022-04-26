package com.bao.currency.Model;

public class Exchange {
    private String tiente;
    private String rate;

    public Exchange(String tiente, String rate) {
        this.tiente = tiente;
        this.rate = rate;
    }

    public Exchange() {
    }

    public String getTiente() {
        return tiente;
    }

    public void setTiente(String tiente) {
        this.tiente = tiente;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
