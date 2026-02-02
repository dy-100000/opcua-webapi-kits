package org.opcfoundation.uawebservicedemo.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;

import java.util.List;

@Mapper
public interface SkillMapper {
    Skill getSkill(Integer id);

    Skill getSkillData(Integer id);

    List<Skill> getSkillOfEmployee(Integer employeeId);

    List<Skill> getSkillDataOfEmployee(Integer employeeId);

    List<Skill> getAllSkill();
}
