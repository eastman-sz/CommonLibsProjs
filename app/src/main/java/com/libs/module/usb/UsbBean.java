package com.libs.module.usb;

import android.hardware.usb.UsbDevice;

/**
 * Created by E on 2018/1/4.
 */
public class UsbBean {

    private String name = null;
    private UsbDevice usbDevice = null;
    private String path = null;

    public UsbBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
