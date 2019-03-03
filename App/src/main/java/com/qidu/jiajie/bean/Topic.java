package com.qidu.jiajie.bean;


public class Topic {
    private String title;
    private String content;

    private boolean isComMeg;

    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isComMeg() {
        return isComMeg;
    }

    public void setComMeg(boolean comMeg) {
        isComMeg = comMeg;
    }

    public Topic(String title, String content,boolean isComMeg,int viewType) {
        this.title = title;
        this.content = content;
        this.isComMeg=isComMeg;
        this.viewType=viewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
