package com.leo.member.controller;

import com.leo.common.common.CommonResult;
import com.leo.member.entity.Member;
import com.leo.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/member")
public class MemberInfoController {

    @Autowired
    MemberService memberService;

    @GetMapping("/getMemberInfo/{id}")
    public CommonResult getMemberInfoById(@PathVariable String id){
        Member member = memberService.getMemberInfo(id);
        return CommonResult.ok().set("data", member);

    }
}
