package com.zhongli.main.zhonglitenghui.bean;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/19.
 */
public class Certificate {
    private int id;
    private int type;
    private String name;
    private String description;
    private List<Img> imgs;
    private String typeName;

    public Certificate() {
    }

    public Certificate(int id, int type, String name, String description, List<Img> imgs, String typeName) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.imgs = imgs;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Img> getImgs() {
        return imgs;
    }

    public void setImgs(List<Img> imgs) {
        this.imgs = imgs;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
