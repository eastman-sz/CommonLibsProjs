package com.libs.module.ble.tizhicheng;

/**
 * Created by E on 2018/1/4.
 */
public class TzcBean {

    private float weight = 0f;
    private int impedanceEnHex = 0;

    public TzcBean() {
    }

    public TzcBean(float weight, int impedanceEnHex) {
        this.weight = weight;
        this.impedanceEnHex = impedanceEnHex;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getImpedanceEnHex() {
        return impedanceEnHex;
    }

    public void setImpedanceEnHex(int impedanceEnHex) {
        this.impedanceEnHex = impedanceEnHex;
    }
}
