package com.zhongli.main.zhonglitenghui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class Case implements Serializable {
    private int id;
    private String img;
    private String name;
    private String description;
    private List<String> details;
    private boolean isSelFag;

    public Case() {
    }

    public Case(int id, String img, String name, String description, List<String> details) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.description = description;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public boolean isSelFag() {
        return isSelFag;
    }

    public void setSelFag(boolean isSelFag) {
        this.isSelFag = isSelFag;
    }
}
