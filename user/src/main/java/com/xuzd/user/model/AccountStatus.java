package com.xuzd.user.model;

public enum AccountStatus {
    //暂时就这两种状态
    Consumption(0),//消费
    Recharge(1);//充值

    private int value;
    private AccountStatus(int value) {
        this.value = value;
    }
}
