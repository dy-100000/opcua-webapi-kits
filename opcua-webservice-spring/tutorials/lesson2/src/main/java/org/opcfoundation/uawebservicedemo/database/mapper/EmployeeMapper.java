package org.opcfoundation.uawebservicedemo.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeData;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    Employee getEmployee(Integer id);

    List<Employee> getEmployeeOfDepartment(Integer departmentId);

    EmployeeData getEmployeeData(Integer employeeId);

    List<Employee> getAllEmployee();
}
