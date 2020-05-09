package com.example.employeelist.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeelist.R;
import com.example.employeelist.adapter.EmployeeAdapter;
import com.example.employeelist.callback.EmployeeListCallback;
import com.example.employeelist.database.EmployeeTable;
import com.example.employeelist.model.Constants;
import com.example.employeelist.model.Employee;
import com.example.employeelist.network.ApiClient;
import com.example.employeelist.util.Network;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Employee> syncList = new ArrayList<>();
    private EmployeeTable employeeTable;
    private EmployeeAdapter adapter;
    private RelativeLayout progressBar;
    private TextView txtEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
        employeeTable = new EmployeeTable(this);
        adapter = new EmployeeAdapter(this);
        txtEmpty = findViewById(R.id.txtEmpty);
        progressBar = findViewById(R.id.progressBar);
        //Clear Database
        ImageView imgClear = findViewById(R.id.imgClear);
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncList.clear();
                employeeTable.clearTable();
                adapter.clearItem();
            }
        });
        //Sort Database
        ImageView imgSort = findViewById(R.id.imgSort);
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncList.clear();
                List<Employee> sortList = employeeTable.sortEmployeeByAge();
                adapter.addList(sortList);
            }
        });
        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        //Check RecyclerView is empty
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                txtEmpty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtEmpty.setVisibility(View.GONE);
        populateList();
    }

    /*
     * Every time activity start, Check table and sync data.
     * Otherwise load data from server.
     * */
    private void populateList() {
        syncList = employeeTable.getEmployeeList();
        if (syncList.size() > 0) {
            adapter.addList(syncList);
        } else {
            if (Network.isConnected(this)) {
                progressBar.setVisibility(View.VISIBLE);
                syncList();
            } else {
                Toast.makeText(MainActivity.this,Constants.ERROR_NETWORK,Toast.LENGTH_SHORT);
            }
        }
    }
    //Sync data from server.
    private void syncList() {
        ApiClient.enqueue(new EmployeeListCallback() {
            @Override
            public void onSuccess(List<Employee> list) {
                progressBar.setVisibility(View.GONE);
                if (list.size() > 0) {
                    //Clear list, table and load data into RecyclerView
                    syncList.clear();
                    employeeTable.clearTable();
                    employeeTable.insertList(list);
                    syncList = employeeTable.getEmployeeList();
                    adapter.addList(syncList);
                }
            }

            @Override
            public void onFailure(String status) {
            Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT);
            }
        });
    }
}
