package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by KIM on 2015/3/20 0020.
 */
public class EquipmentInfo {
    private String id;
    private String description;
    private String img;

    public EquipmentInfo() {
    }

    public EquipmentInfo(String id, String description, String img) {
        this.id = id;
        this.description = description;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
