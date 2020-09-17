package com.leo.member.controller;

import com.leo.common.common.CommonResult;
import com.leo.member.entity.Address;
import com.leo.member.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping("/getAddress")
    public CommonResult getUserAddress(@RequestParam("uid") String uid){
        List<Address> userAddress = addressService.getUserAddress(uid);
        return CommonResult.ok().set("address", userAddress);
    }


    @PostMapping("/addAddress")
    public CommonResult addUserAddress(@RequestBody Address address){
        Integer integer = addressService.addUserAddress(address);
        return CommonResult.ok();
    }

    @GetMapping("/getPreferAddress")
    public CommonResult getPreferAddress(@RequestParam("uid") String uid){
        Address address = addressService.getPreferAddress(uid);
        return CommonResult.ok().set("preferAddress", address);
    }

    @GetMapping("/getAddressByid")
    public CommonResult getAddressById(@RequestParam("id") String id){
        Address address = addressService.getAddressById(id);
        return CommonResult.ok().set("data", address);
    }
}
