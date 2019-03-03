package com.app.base.bean;

import java.util.List;

/**
 * 订单
 */

public class OrderEntity {


    /**
     * time_record : {"receipt_at":"2019-01-12 16:51","add_time":"2019-01-12 16:51","pay_time":"2019-01-12 16:51","complete_at":false,"contact_appointment_at":"2019-01-13 08:00","order_sm_at":false}
     * payment : {"order_deductible_type":"2","order_pay_way":"alipay","order_actual_amount":"0.00","order_amount":"24.00","order_deductible_count":"24.00"}
     * order_detail : {"is_sys_order":false,"order_info":"<p>新家入住，新房装修后保洁<\/p>","order_pic":["uploadfile/20181218103055_.jpeg"],"order_message":"备注留言","order_normal_id":"13","order_type":"1","order_type_id":"6","order_sn":"15472831110000000000013","order_state":"2","order_is_pay":"1","order_refund":"0","order_is_peddling":"0","order_rate":"0","cancel_reason":"","order_comment_id":"0","order_bis_state_dsc":"PENDING_DOOR","order_pay_state_dsc":"PAY_SUCCESS","order_name":"新房开荒保洁","order_sub_sn":"0","cat_name":"开荒清洁","subject_title":"新房开荒保洁","_is_accepted":0,"order_charging":"FIXED_PRICE","order_img":["uploadfile/20181218103055_.jpeg"]}
     * store_info : {"order_belong_store_id":"1","store_phone":"15819943115","store_name":"帮家洁自营店"}
     * user_info : {"user_id":"117","user_name":"李小龙","user_phone":"18594042132","user_pic":"uploadfile/20190109110154_.jpeg"}
     * server_info : {"address_name":"广东省广州市隔坑大街二巷钟二公园黄地园大街五巷8号","appointed_uid":["123"],"contact_name":"法国杂碎王","house_number":"无门牌号","telephone":"19977529989","order_lat":"22.975708","order_lng":"113.321399","service_length":"2"}
     */

    private TimeRecordBean time_record;
    private PaymentBean payment;
    private OrderDetailBean order_detail;
    private StoreInfoBean store_info;
    private UserInfoBean user_info;
    private ServerInfoBean server_info;

    public TimeRecordBean getTime_record() {
        return time_record;
    }

    public void setTime_record(TimeRecordBean time_record) {
        this.time_record = time_record;
    }

    public PaymentBean getPayment() {
        return payment;
    }

    public void setPayment(PaymentBean payment) {
        this.payment = payment;
    }

    public OrderDetailBean getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(OrderDetailBean order_detail) {
        this.order_detail = order_detail;
    }

    public StoreInfoBean getStore_info() {
        return store_info;
    }

    public void setStore_info(StoreInfoBean store_info) {
        this.store_info = store_info;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public ServerInfoBean getServer_info() {
        return server_info;
    }

    public void setServer_info(ServerInfoBean server_info) {
        this.server_info = server_info;
    }

    public static class TimeRecordBean {
        /**
         * receipt_at : 2019-01-12 16:51
         * add_time : 2019-01-12 16:51
         * pay_time : 2019-01-12 16:51
         * complete_at : false
         * contact_appointment_at : 2019-01-13 08:00
         * order_sm_at : false
         */

        private String receipt_at;
        private String add_time;
        private String pay_time;
        private boolean complete_at;
        private String contact_appointment_at;
        private boolean order_sm_at;

        public String getReceipt_at() {
            return receipt_at;
        }

        public void setReceipt_at(String receipt_at) {
            this.receipt_at = receipt_at;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public boolean isComplete_at() {
            return complete_at;
        }

        public void setComplete_at(boolean complete_at) {
            this.complete_at = complete_at;
        }

        public String getContact_appointment_at() {
            return contact_appointment_at;
        }

        public void setContact_appointment_at(String contact_appointment_at) {
            this.contact_appointment_at = contact_appointment_at;
        }

        public boolean isOrder_sm_at() {
            return order_sm_at;
        }

        public void setOrder_sm_at(boolean order_sm_at) {
            this.order_sm_at = order_sm_at;
        }
    }

    public static class PaymentBean {
        /**
         * order_deductible_type : 2
         * order_pay_way : alipay
         * order_actual_amount : 0.00
         * order_amount : 24.00
         * order_deductible_count : 24.00
         */

        private String order_deductible_type;
        private String order_pay_way;
        private String order_actual_amount;
        private String order_amount;
        private String order_deductible_count;

        public String getOrder_deductible_type() {
            return order_deductible_type;
        }

        public void setOrder_deductible_type(String order_deductible_type) {
            this.order_deductible_type = order_deductible_type;
        }

        public String getOrder_pay_way() {
            return order_pay_way;
        }

        public void setOrder_pay_way(String order_pay_way) {
            this.order_pay_way = order_pay_way;
        }

        public String getOrder_actual_amount() {
            return order_actual_amount;
        }

        public void setOrder_actual_amount(String order_actual_amount) {
            this.order_actual_amount = order_actual_amount;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        public String getOrder_deductible_count() {
            return order_deductible_count;
        }

        public void setOrder_deductible_count(String order_deductible_count) {
            this.order_deductible_count = order_deductible_count;
        }
    }

    public static class OrderDetailBean {
        /**
         * is_sys_order : false
         * order_info : <p>新家入住，新房装修后保洁</p>
         * order_pic : ["uploadfile/20181218103055_.jpeg"]
         * order_message : 备注留言
         * order_normal_id : 13
         * order_type : 1
         * order_type_id : 6
         * order_sn : 15472831110000000000013
         * order_state : 2
         * order_is_pay : 1
         * order_refund : 0
         * order_is_peddling : 0
         * order_rate : 0
         * cancel_reason :
         * order_comment_id : 0
         * order_bis_state_dsc : PENDING_DOOR
         * order_pay_state_dsc : PAY_SUCCESS
         * order_name : 新房开荒保洁
         * order_sub_sn : 0
         * cat_name : 开荒清洁
         * subject_title : 新房开荒保洁
         * _is_accepted : 0
         * order_charging : FIXED_PRICE
         * order_img : ["uploadfile/20181218103055_.jpeg"]
         */

        private boolean is_sys_order;
        private String order_info;
        private String order_message;
        private String order_normal_id;
        private String order_type;
        private String order_type_id;
        private String order_sn;
        private String order_state;
        private String order_is_pay;
        private String order_refund;
        private String order_is_peddling;
        private String order_rate;
        private String cancel_reason;
        private String order_comment_id;
        private String order_bis_state_dsc;
        private String order_pay_state_dsc;
        private String order_name;
        private String order_sub_sn;
        private String cat_name;
        private String subject_title;
        private int _is_accepted;
        private String order_charging;
        private List<String> order_pic;
        private List<String> order_img;

        public boolean isIs_sys_order() {
            return is_sys_order;
        }

        public void setIs_sys_order(boolean is_sys_order) {
            this.is_sys_order = is_sys_order;
        }

        public String getOrder_info() {
            return order_info;
        }

        public void setOrder_info(String order_info) {
            this.order_info = order_info;
        }

        public String getOrder_message() {
            return order_message;
        }

        public void setOrder_message(String order_message) {
            this.order_message = order_message;
        }

        public String getOrder_normal_id() {
            return order_normal_id;
        }

        public void setOrder_normal_id(String order_normal_id) {
            this.order_normal_id = order_normal_id;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getOrder_type_id() {
            return order_type_id;
        }

        public void setOrder_type_id(String order_type_id) {
            this.order_type_id = order_type_id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getOrder_state() {
            return order_state;
        }

        public void setOrder_state(String order_state) {
            this.order_state = order_state;
        }

        public String getOrder_is_pay() {
            return order_is_pay;
        }

        public void setOrder_is_pay(String order_is_pay) {
            this.order_is_pay = order_is_pay;
        }

        public String getOrder_refund() {
            return order_refund;
        }

        public void setOrder_refund(String order_refund) {
            this.order_refund = order_refund;
        }

        public String getOrder_is_peddling() {
            return order_is_peddling;
        }

        public void setOrder_is_peddling(String order_is_peddling) {
            this.order_is_peddling = order_is_peddling;
        }

        public String getOrder_rate() {
            return order_rate;
        }

        public void setOrder_rate(String order_rate) {
            this.order_rate = order_rate;
        }

        public String getCancel_reason() {
            return cancel_reason;
        }

        public void setCancel_reason(String cancel_reason) {
            this.cancel_reason = cancel_reason;
        }

        public String getOrder_comment_id() {
            return order_comment_id;
        }

        public void setOrder_comment_id(String order_comment_id) {
            this.order_comment_id = order_comment_id;
        }

        public String getOrder_bis_state_dsc() {
            return order_bis_state_dsc;
        }

        public void setOrder_bis_state_dsc(String order_bis_state_dsc) {
            this.order_bis_state_dsc = order_bis_state_dsc;
        }

        public String getOrder_pay_state_dsc() {
            return order_pay_state_dsc;
        }

        public void setOrder_pay_state_dsc(String order_pay_state_dsc) {
            this.order_pay_state_dsc = order_pay_state_dsc;
        }

        public String getOrder_name() {
            return order_name;
        }

        public void setOrder_name(String order_name) {
            this.order_name = order_name;
        }

        public String getOrder_sub_sn() {
            return order_sub_sn;
        }

        public void setOrder_sub_sn(String order_sub_sn) {
            this.order_sub_sn = order_sub_sn;
        }

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getSubject_title() {
            return subject_title;
        }

        public void setSubject_title(String subject_title) {
            this.subject_title = subject_title;
        }

        public int get_is_accepted() {
            return _is_accepted;
        }

        public void set_is_accepted(int _is_accepted) {
            this._is_accepted = _is_accepted;
        }

        public String getOrder_charging() {
            return order_charging;
        }

        public void setOrder_charging(String order_charging) {
            this.order_charging = order_charging;
        }

        public List<String> getOrder_pic() {
            return order_pic;
        }

        public void setOrder_pic(List<String> order_pic) {
            this.order_pic = order_pic;
        }

        public List<String> getOrder_img() {
            return order_img;
        }

        public void setOrder_img(List<String> order_img) {
            this.order_img = order_img;
        }
    }

    public static class StoreInfoBean {
        /**
         * order_belong_store_id : 1
         * store_phone : 15819943115
         * store_name : 帮家洁自营店
         */

        private String order_belong_store_id;
        private String store_phone;
        private String store_name;

        public String getOrder_belong_store_id() {
            return order_belong_store_id;
        }

        public void setOrder_belong_store_id(String order_belong_store_id) {
            this.order_belong_store_id = order_belong_store_id;
        }

        public String getStore_phone() {
            return store_phone;
        }

        public void setStore_phone(String store_phone) {
            this.store_phone = store_phone;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }
    }

    public static class UserInfoBean {
        /**
         * user_id : 117
         * user_name : 李小龙
         * user_phone : 18594042132
         * user_pic : uploadfile/20190109110154_.jpeg
         */

        private String user_id;
        private String user_name;
        private String user_phone;
        private String user_pic;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_pic() {
            return user_pic;
        }

        public void setUser_pic(String user_pic) {
            this.user_pic = user_pic;
        }
    }

    public static class ServerInfoBean {
        /**
         * address_name : 广东省广州市隔坑大街二巷钟二公园黄地园大街五巷8号
         * appointed_uid : ["123"]
         * contact_name : 法国杂碎王
         * house_number : 无门牌号
         * telephone : 19977529989
         * order_lat : 22.975708
         * order_lng : 113.321399
         * service_length : 2
         */

        private String address_name;
        private String contact_name;
        private String house_number;
        private String telephone;
        private String order_lat;
        private String order_lng;
        private String service_length;
        private List<String> appointed_uid;

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getHouse_number() {
            return house_number;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getOrder_lat() {
            return order_lat;
        }

        public void setOrder_lat(String order_lat) {
            this.order_lat = order_lat;
        }

        public String getOrder_lng() {
            return order_lng;
        }

        public void setOrder_lng(String order_lng) {
            this.order_lng = order_lng;
        }

        public String getService_length() {
            return service_length;
        }

        public void setService_length(String service_length) {
            this.service_length = service_length;
        }

        public List<String> getAppointed_uid() {
            return appointed_uid;
        }

        public void setAppointed_uid(List<String> appointed_uid) {
            this.appointed_uid = appointed_uid;
        }
    }
}
