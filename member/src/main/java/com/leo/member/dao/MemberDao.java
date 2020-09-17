package com.leo.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leo.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Liu
 */
@Mapper
public interface MemberDao extends BaseMapper<Member> {
}
