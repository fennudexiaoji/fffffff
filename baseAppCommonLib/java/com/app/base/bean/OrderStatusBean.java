package com.app.base.bean;

/**
 * Created by 7du-28 on 2019/1/15.
 */

public class OrderStatusBean {

    /**
     * pending_pay : 1
     * pending_order : 0
     * pending_service : 1
     * in_service : 0
     * pending_comment : 1
     * closed : 1
     * pending_assign : 2
     */

    private int pending_pay;
    private int pending_order;
    private int pending_service;
    private int in_service;
    private int pending_comment;
    private int closed;
    private int pending_assign;

    public int getPending_pay() {
        return pending_pay;
    }

    public void setPending_pay(int pending_pay) {
        this.pending_pay = pending_pay;
    }

    public int getPending_order() {
        return pending_order;
    }

    public void setPending_order(int pending_order) {
        this.pending_order = pending_order;
    }

    public int getPending_service() {
        return pending_service;
    }

    public void setPending_service(int pending_service) {
        this.pending_service = pending_service;
    }

    public int getIn_service() {
        return in_service;
    }

    public void setIn_service(int in_service) {
        this.in_service = in_service;
    }

    public int getPending_comment() {
        return pending_comment;
    }

    public void setPending_comment(int pending_comment) {
        this.pending_comment = pending_comment;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getPending_assign() {
        return pending_assign;
    }

    public void setPending_assign(int pending_assign) {
        this.pending_assign = pending_assign;
    }
}
