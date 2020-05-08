package com.example.employeelist.network;


import com.example.employeelist.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

final class JSONDecode {

    static List<Employee> convertJSONtoList(String jsonString) throws JSONException {
        List<Employee> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        int count = jsonArray.length();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                JSONObject jsonEmployee = jsonArray.getJSONObject(i);
                Employee employee = new Employee();
                employee.setId(jsonEmployee.getInt("id"));
                employee.setEmployee_name(jsonEmployee.getString("employee_name"));
                employee.setEmployee_age(jsonEmployee.getInt("employee_age"));
                employee.setEmployee_salary(jsonEmployee.getLong("employee_salary"));
                employee.setProfile_image(nullCheck(jsonEmployee.getString("profile_image")));
                list.add(employee);
            }
        }
        return list;
    }

    private static String nullCheck(String text) {
        if (text.isEmpty()) {
            return null;
        } else {
            return text;
        }
    }
}