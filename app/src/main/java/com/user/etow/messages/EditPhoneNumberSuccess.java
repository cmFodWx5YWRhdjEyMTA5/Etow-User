/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. All rights reserved
 *  Author DangTin. Create on 2018/02/08
 * ******************************************************************************
 */

package com.user.etow.messages;

public class EditPhoneNumberSuccess {

    private String phone;

    public EditPhoneNumberSuccess(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
