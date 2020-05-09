package com.example.employeelist.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.employeelist.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTable extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "employeedb";
    private static final int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "employee";
    private static String P_ID = "ID";
    private static String EMP_ID = "empId";
    private static String EMP_NAME = "name";
    private static String EMP_AGE = "age";
    private static String EMP_SALARY = "salary";
    private static String EMP_PROFILE = "profile";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EMP_ID + " INTEGER NULL, " +
            EMP_NAME + " TEXT NULL , " +
            EMP_AGE + " INTEGER NULL , " +
            EMP_SALARY + " INTEGER NULL , " +
            EMP_PROFILE + " TEXT NULL ) ";

    public EmployeeTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    private void openDB() {
        sqLiteDatabase = this.getWritableDatabase();
    }

    private void closeDB() {
        sqLiteDatabase.close();
    }

    public void insertList(List<Employee> list) {
        openDB();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        for (int i = 0; i < list.size(); i++) {
            Employee employee = list.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(EMP_ID, employee.getId());
            contentValues.put(EMP_NAME, employee.getEmployee_name());
            contentValues.put(EMP_AGE, employee.getEmployee_age());
            contentValues.put(EMP_SALARY, employee.getEmployee_salary());
            contentValues.put(EMP_PROFILE, employee.getProfile_image());
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        }
        closeDB();
    }

    public List<Employee> getEmployeeList() {
        openDB();
        List<Employee> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getInt(cursor.getColumnIndex(EMP_ID)));
                employee.setEmployee_name(cursor.getString(cursor.getColumnIndex(EMP_NAME)));
                employee.setEmployee_age(cursor.getInt(cursor.getColumnIndex(EMP_AGE)));
                employee.setEmployee_salary(cursor.getLong(cursor.getColumnIndex(EMP_SALARY)));
                employee.setProfile_image(cursor.getString(cursor.getColumnIndex(EMP_PROFILE)));
                list.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return list;
    }

//    public List<Employee> sortEmployeeByAge(String age) {
//    to be added
//    }

    public void clearTable() {
        openDB();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        closeDB();
    }
}

