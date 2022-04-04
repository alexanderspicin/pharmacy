package com.example.pharmacy.entity;

public enum Promocode {
    NEW150(150);

    int saleSum;

    Promocode(int saleSum) {
        this.saleSum = saleSum;
    }

    public int getSaleSum() {
        return saleSum;
    }

}
