/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. All rights reserved
 *  Author DangTin. Create on 2018/02/08
 * ******************************************************************************
 */

package com.user.etow.messages;

import com.user.etow.models.Image;

public class SelectAvatarSuccess {

    private Image image;

    public SelectAvatarSuccess(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
