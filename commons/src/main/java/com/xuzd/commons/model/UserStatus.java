package com.xuzd.commons.model;

public enum UserStatus {
    OK(0),// 正常账号
    NotActive(1),// 账号未激活
    NeedChangePassword(2), // 需要修改密码才能继续操作
    Incomplete(3),// 账户信息未完成。一般来说，会影响到功能的使用
    WillStop(4),// 即将停止服务，比如欠费
    Lock(5),// 账号已经锁定，暂时无法进行某些操作， 比如发布不适当的信息
    Stopped(6),// 账号已停止，和Lock类似
    ToDeleted(7);// 账号即将删除，这个时候账号是不能恢复的，系统在该状态下，将会删除用户数据。

    private int value;

    private UserStatus(int value) {
        this.value = value;
    }

    public static UserStatus fromValue(int value) {
        for (UserStatus stutas : UserStatus.values()) {
            if (stutas.value == value) {
                return stutas;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}