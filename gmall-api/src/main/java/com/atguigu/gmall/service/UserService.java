package com.atguigu.gmall.service;

import com.atguigu.gmall.beans.UmsMember;
import com.atguigu.gmall.beans.UmsMemberReceiveAddress;


import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    UmsMember getUserById(String id);

    void addUserCache(String token, UmsMember umsMemberFromDb);

    UmsMember login(UmsMember umsMember);

    UmsMember isUidExists(String uid);

    UmsMember addUser(UmsMember umsMember);

    UmsMemberReceiveAddress getAddressById(String deliveryAddressId);

    List<UmsMemberReceiveAddress> getAddressListByMemberId(String memberId);
}
