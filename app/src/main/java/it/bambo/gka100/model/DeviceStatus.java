package it.bambo.gka100.model;

import java.util.Date;

/**
 * Created by andreas on 04.04.2015.
 */
public class DeviceStatus {

    private String name;
    private Date time;

    // off/on
    private boolean alarm;
    private int gsm;
    private int accu;
    // off/on
    private boolean area;
    private String shock;
    private float volt;
    // off/on
    private boolean holdAlarm;

    // low/high
    private boolean in1;
    // low/high
    private boolean in2;
    // off/on
    private boolean out1;
    // off/on
    private boolean out2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public int getGsm() {
        return gsm;
    }

    public void setGsm(int gsm) {
        this.gsm = gsm;
    }

    public int getAccu() {
        return accu;
    }

    public void setAccu(int accu) {
        this.accu = accu;
    }

    public boolean isArea() {
        return area;
    }

    public void setArea(boolean area) {
        this.area = area;
    }

    public String getShock() {
        return shock;
    }

    public void setShock(String shock) {
        this.shock = shock;
    }

    public float getVolt() {
        return volt;
    }

    public void setVolt(float volt) {
        this.volt = volt;
    }

    public boolean isHoldAlarm() {
        return holdAlarm;
    }

    public void setHoldAlarm(boolean holdAlarm) {
        this.holdAlarm = holdAlarm;
    }

    public boolean isIn1() {
        return in1;
    }

    public void setIn1(boolean in1) {
        this.in1 = in1;
    }

    public boolean isIn2() {
        return in2;
    }

    public void setIn2(boolean in2) {
        this.in2 = in2;
    }

    public boolean isOut1() {
        return out1;
    }

    public void setOut1(boolean out1) {
        this.out1 = out1;
    }

    public boolean isOut2() {
        return out2;
    }

    public void setOut2(boolean out2) {
        this.out2 = out2;
    }

}