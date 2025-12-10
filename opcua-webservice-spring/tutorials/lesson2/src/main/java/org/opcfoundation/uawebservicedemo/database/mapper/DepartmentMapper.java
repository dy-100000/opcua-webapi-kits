package org.opcfoundation.uawebservicedemo.database.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeData;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    Department getDepartment(Integer id);

    List<Department> getChildDepartment(Integer id);
}
