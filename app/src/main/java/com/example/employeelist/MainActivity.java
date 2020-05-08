package com.example.employeelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.employeelist.callback.EmployeeListCallback;
import com.example.employeelist.database.EmployeeTable;
import com.example.employeelist.model.Employee;
import com.example.employeelist.network.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Employee> syncList = new ArrayList<>();
    private EmployeeTable employeeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Sync data from server.
    private void syncList() {
        ApiClient.enqueue(new EmployeeListCallback() {
            @Override
            public void onSuccess(List<Employee> list) {
                if (list.size() > 0) {
                    //Clear list, table and load data into RecyclerView
                    syncList.clear();
                    employeeTable.insertList(list);
                }
            }

            @Override
            public void onFailure(String status) {
            //will be added later
            }
        });
    }
}
