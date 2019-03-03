package com.app.base.bean;



public class StaffUser {

    /**
     * id : 1
     * staff_name : 周发发
     * user_type : 1
     * staff_pass : 1
     * staff_status : 1
     * balance : 0.00
     * total_income : 0.00
     * yesterday : 0.00
     */

    private String id;
    private String staff_name;
    private String user_type;
    private String staff_pass;
    private String staff_status;
    private String balance;
    private String total_income;
    private String yesterday;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStaff_pass() {
        return staff_pass;
    }

    public void setStaff_pass(String staff_pass) {
        this.staff_pass = staff_pass;
    }

    public String getStaff_status() {
        return staff_status;
    }

    public void setStaff_status(String staff_status) {
        this.staff_status = staff_status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public String getYesterday() {
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }
}
