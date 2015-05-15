package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by KIM on 2015/3/18 0018.
 */
public class CompanyInfo {

    private int id;
    private String  profile;
    private String protext;
    private String imgOrganization;

    public CompanyInfo() {
    }

    public CompanyInfo(int id,String  profile,String protext,String imgOrganization){
        this.id = id;
        this.profile = profile;
        this.profile = protext;
        this.imgOrganization = imgOrganization;
    }

    public String getProfile() {
        return profile;
    }

    public String getProtext() {
        return protext;
    }

    public String getImgOrganization() {
        return imgOrganization;
    }

    public void setImgOrganization(String imgOrganization) {
        this.imgOrganization = imgOrganization;
    }

    public void setProtext(String protext) {
        this.protext = protext;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setId(int id) {
        this.id = id;
    }
}

