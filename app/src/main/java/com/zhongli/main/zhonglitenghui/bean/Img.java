package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by 278877385 on 2015/3/19.
 */
public class Img {
    private int id;
    private String src;
    private String zip;

    public Img() {
    }

    public Img(int id, String src, String zip) {
        this.id = id;
        this.src = src;
        this.zip = zip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
