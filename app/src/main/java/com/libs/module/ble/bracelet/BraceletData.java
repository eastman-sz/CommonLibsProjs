package com.libs.module.ble.bracelet;
/**
 * Created by E on 2018/1/15.
 */
public class BraceletData {

    private int steps = 0;
    private float calorie = 0;
    private float distance = 0;
    private int hr = 0;

    public BraceletData() {
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }
}
