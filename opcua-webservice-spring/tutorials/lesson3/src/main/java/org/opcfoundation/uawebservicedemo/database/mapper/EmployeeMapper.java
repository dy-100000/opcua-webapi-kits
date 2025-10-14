package org.opcfoundation.uawebservicedemo.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeInfo;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    Employee getEmployee(Integer id);

    List<Employee> getEmployeeOfDepartment(Integer departmentId);

    EmployeeInfo getEmployeeInfo(Integer employeeId);
}
