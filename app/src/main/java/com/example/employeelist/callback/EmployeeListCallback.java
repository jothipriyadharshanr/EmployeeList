package com.example.employeelist.callback;


import com.example.employeelist.model.Employee;

import java.util.List;

public interface EmployeeListCallback {

    void onSuccess(List<Employee> list);

    void onFailure(String status);

}
