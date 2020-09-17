package com.leo.member.service;

import com.leo.member.entity.Address;

import java.util.List;

/**
 * @author Liu
 */
public interface AddressService {
    List<Address> getUserAddress(String uid);

    Integer addUserAddress(Address address);

    Address getPreferAddress(String uid);

    Address getAddressById(String id);
}
