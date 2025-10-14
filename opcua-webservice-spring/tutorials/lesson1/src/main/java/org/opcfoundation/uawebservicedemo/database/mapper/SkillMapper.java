package org.opcfoundation.uawebservicedemo.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;

import java.util.List;

@Mapper
public interface SkillMapper {
    Skill getSkillInfo(Integer id);

    Skill getSkillData(Integer id);

    List<Skill> getAllSkillInfo();
}
