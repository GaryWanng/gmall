package com.atguigu.gmall.service;

import com.atguigu.gmall.beans.OmsCartItem;

import java.util.List;

public interface CartService {
    void checkCart(OmsCartItem omsCartItem);

    List<OmsCartItem> getCartList(String memberId);

    OmsCartItem isCartExists(OmsCartItem omsCartItem);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);
}
