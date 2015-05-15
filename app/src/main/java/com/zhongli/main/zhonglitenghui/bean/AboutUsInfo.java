package com.zhongli.main.zhonglitenghui.bean;

/**
 * Created by KIM on 2015/3/18 0018.
 */
public class AboutUsInfo {

    private int id;
    private String name;
    private String aliss_name;
    private String ch_address;
    private String area_address;
    private String postcode;
    private String telephone;
    private String phone;
    private String fax;
    private String email;
    private String webUrl;

    public AboutUsInfo() {
    }

    public AboutUsInfo(int id, String name, String aliss_name, String ch_address, String area_address, String postcode, String telephone, String phone, String fax, String email, String webUrl) {
        this.id = id;
        this.name = name;
        this.aliss_name = aliss_name;
        this.ch_address = ch_address;
        this.area_address = area_address;
        this.postcode = postcode;
        this.telephone = telephone;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.webUrl = webUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliss_name() {
        return aliss_name;
    }

    public void setAliss_name(String aliss_name) {
        this.aliss_name = aliss_name;
    }

    public String getCh_address() {
        return ch_address;
    }

    public void setCh_address(String ch_address) {
        this.ch_address = ch_address;
    }

    public String getArea_address() {
        return area_address;
    }

    public void setArea_address(String area_address) {
        this.area_address = area_address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
