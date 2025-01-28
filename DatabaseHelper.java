package com.example.financeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance.db";

    // Таблицы
    private static final String TABLE_INCOME = "income_table";
    private static final String TABLE_EXPENSE = "expense_table";
    private static final String TABLE_CATEGORY = "category_table";

    // Поля для доходов
    private static final String COL_INCOME_ID = "id";
    private static final String COL_INCOME_MONTH = "month";
    private static final String COL_INCOME_AMOUNT = "amount";

    // Поля для расходов
    private static final String COL_EXPENSE_ID = "id";
    private static final String COL_EXPENSE_CATEGORY = "category";
    private static final String COL_EXPENSE_AMOUNT = "amount";

    // Поля для категорий
    private static final String COL_CATEGORY_ID = "id";
    private static final String COL_CATEGORY_NAME = "name";
    private static final String COL_CATEGORY_BUDGET = "budget";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Увеличиваем версию на 1
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблиц
        db.execSQL("CREATE TABLE " + TABLE_INCOME + " (" +
                COL_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_INCOME_MONTH + " TEXT, " +
                COL_INCOME_AMOUNT + " REAL)");

        db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " (" +
                COL_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EXPENSE_CATEGORY + " TEXT, " +
                COL_EXPENSE_AMOUNT + " REAL)");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT, " +
                COL_CATEGORY_BUDGET + " REAL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    // Добавление дохода
    public void addIncome(String month, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_INCOME_MONTH, month);
        contentValues.put(COL_INCOME_AMOUNT, amount);
        db.insert(TABLE_INCOME, null, contentValues); // Сохранение данных в таблицу
    }


    // Добавление расхода
    public void addExpense(String category, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EXPENSE_CATEGORY, category);
        contentValues.put(COL_EXPENSE_AMOUNT, amount);
        db.insert(TABLE_EXPENSE, null, contentValues);
    }

    // Добавление категории
    public void addCategory(String name, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CATEGORY_NAME, name);
        contentValues.put(COL_CATEGORY_BUDGET, budget);
        db.insert(TABLE_CATEGORY, null, contentValues); // Сохранение данных в таблицу
    }

    // Получение общего дохода
    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_INCOME, null);
        double totalIncome = 0;
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }
        cursor.close();
        return totalIncome;
    }

    // Получение общих расходов
    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSE, null);
        double totalExpense = 0;
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0);
        }
        cursor.close();
        return totalExpense;
    }

    // Получение всех категорий
    // Получение всех категорий
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Убедитесь, что "id" возвращается как "_id" для использования в SimpleCursorAdapter
        return db.rawQuery("SELECT id AS _id, name, budget FROM " + TABLE_CATEGORY, null);
    }

    // Очистка всех доходов
    public void resetIncome() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INCOME, null, null); // Удалить все данные из таблицы доходов
    }

    // Очистка всех расходов
    public void resetExpense() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, null, null); // Удалить все данные из таблицы расходов
    }

    public void resetAll() {
        resetIncome();  // Вызов сброса доходов
        resetExpense(); // Вызов сброса расходов
    }
    // Удаление категории
    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, COL_CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
    }




}
