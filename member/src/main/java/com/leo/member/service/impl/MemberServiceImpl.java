package com.leo.member.service.impl;

import com.leo.member.dao.MemberDao;
import com.leo.member.entity.Member;
import com.leo.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Liu
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member getMemberInfo(String id) {
        Member member = memberDao.selectById(id);
        return member;
    }
}
