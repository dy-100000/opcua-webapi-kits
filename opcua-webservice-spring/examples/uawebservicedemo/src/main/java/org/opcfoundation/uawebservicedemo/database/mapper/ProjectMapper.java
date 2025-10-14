package org.opcfoundation.uawebservicedemo.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.opcfoundation.uawebservicedemo.database.entity.Project;

import java.util.List;

@Mapper
public interface ProjectMapper {
    Project getProjectInfo(Integer id);

    Project getProjectData(Integer id);

    List<Project> getProjectOfDepartment(Integer departmentId);
}
