package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by 278877385 on 2015/3/20.
 */
public class Video {
    //    id(视频列表的id)
//    videoName(视频主题)
//    introduction(视频简介)
//    size(视频文件大小)
//    url(视频文件路径)
//    thumburl(视频文件ipad版缩略图)
//    murl(视频文件手机版缩略图)
//    mins(视频文件时长毫秒数)
//    appMins(时长格式化为HH:mm:SS)
    private int id;
    private String videoName;
    private String size;
    private String url;
    private String thumburl;
    private String murl;
    private long mins;
    private String appMins;
    private boolean isDown;

    public Video() {
    }

    public Video(int id, String videoName, String size, String url, String thumburl, String murl, long mins, String appMins) {
        this.id = id;
        this.videoName = videoName;
        this.size = size;
        this.url = url;
        this.thumburl = thumburl;
        this.murl = murl;
        this.mins = mins;
        this.appMins = appMins;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public long getMins() {
        return mins;
    }

    public void setMins(long mins) {
        this.mins = mins;
    }

    public String getAppMins() {
        return appMins;
    }

    public void setAppMins(String appMins) {
        this.appMins = appMins;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setDown(boolean isDown) {
        this.isDown = isDown;
    }
}
