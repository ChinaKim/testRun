package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class Type {
    private String name;
    private int id;
    private String version;

    public Type() {
    }

    public Type(String name, int id, String version) {
        this.name = name;
        this.id = id;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
