package com.libs.module.usb;

/**
 * Created by E on 2018/1/5.
 */
public class UsbFile {

    private String name = null;
    private String path = null;
    private int type = 0;

    public UsbFile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
