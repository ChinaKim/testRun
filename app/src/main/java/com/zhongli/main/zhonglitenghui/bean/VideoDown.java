package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by 278877385 on 2015/3/23.
 */
public class VideoDown {
    private int position;
    private int downLoad;

    public VideoDown() {
    }

    public VideoDown(int position, int downLoad) {
        this.position = position;
        this.downLoad = downLoad;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDownLoad() {
        return downLoad;
    }

    public void setDownLoad(int downLoad) {
        this.downLoad = downLoad;
    }
}
