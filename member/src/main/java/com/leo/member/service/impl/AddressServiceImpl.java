package com.leo.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leo.member.dao.AddressDao;
import com.leo.member.entity.Address;
import com.leo.member.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liu
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    /**
     * 查询用户的储存地址
     * @param uid
     * @return
     */
    @Override
    public List<Address> getUserAddress(String uid) {
        List<Address> addressList = addressDao.selectList(new QueryWrapper<Address>().eq("uid", uid));
        return addressList;
    }

    /**
     * 添加用户地址
     * @param address
     * @return
     */
    @Override
    public Integer addUserAddress(Address address) {
        int insert = addressDao.insert(address);
        return insert;
    }

    /**
     *查询默认设置的地址
     * @param uid
     * @return
     */
    @Override
    public Address getPreferAddress(String uid) {
        Address address = addressDao.selectOne(new QueryWrapper<Address>().eq("prefer", 1).eq("uid", uid));
        return address;
    }

    @Override
    public Address getAddressById(String id) {
        Address address = addressDao.selectById(id);
        return address;
    }
}
