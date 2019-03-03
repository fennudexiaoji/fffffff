package com.qidu.jiajie.bean;



public class Notice {

    private String msgType;
    private String title;
    private String unread;

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    /**
     * {
     "bulletin_sponsor_name": "",
     "id": "4",
     "bulletin_post_at": "2018-06-26 21:11",
     "bulletin_title": "测试标题，测试标题，测试标题",
     "bulletin_content": "测试内容，历代君王弧顶科学家拉火热i企鹅和经济v的复活节而据",
     "have_read": "0",
     "is_read": "0"
     }
     */

    private String id;
    private String bulletin_title;
    private String bulletin_content;
    private String bulletin_post_at;
    private String bulletin_sponsor;
    private String real_name;
    private String have_read;
    private String bulletin_sponsor_name;

    public String getBulletin_sponsor_name() {
        return bulletin_sponsor_name;
    }

    public void setBulletin_sponsor_name(String bulletin_sponsor_name) {
        this.bulletin_sponsor_name = bulletin_sponsor_name;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBulletin_title() {
        return bulletin_title;
    }

    public void setBulletin_title(String bulletin_title) {
        this.bulletin_title = bulletin_title;
    }

    public String getBulletin_content() {
        return bulletin_content;
    }

    public void setBulletin_content(String bulletin_content) {
        this.bulletin_content = bulletin_content;
    }

    public String getBulletin_post_at() {
        return bulletin_post_at;
    }

    public void setBulletin_post_at(String bulletin_post_at) {
        this.bulletin_post_at = bulletin_post_at;
    }

    public String getBulletin_sponsor() {
        return bulletin_sponsor;
    }

    public void setBulletin_sponsor(String bulletin_sponsor) {
        this.bulletin_sponsor = bulletin_sponsor;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getHave_read() {
        return have_read;
    }

    public void setHave_read(String have_read) {
        this.have_read = have_read;
    }
}
