package com.example.financeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etIncomeAmount, etIncomeMonth, etExpenseAmount, etExpenseCategory, etCategoryName, etCategoryBudget;
    Button btnAddIncome, btnAddExpense, btnAddCategory;
    ListView lvCategories;
    TextView tvTotalIncome, tvTotalExpense, tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // Инициализация элементов интерфейса
        etIncomeAmount = findViewById(R.id.etIncomeAmount);
        etIncomeMonth = findViewById(R.id.etIncomeMonth);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseCategory = findViewById(R.id.etExpenseCategory);
        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryBudget = findViewById(R.id.etCategoryBudget);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        lvCategories = findViewById(R.id.lvCategories);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvBalance = findViewById(R.id.tvBalance);

        // Добавить доход
        btnAddIncome.setOnClickListener(v -> {
            String month = etIncomeMonth.getText().toString();
            String incomeAmountStr = etIncomeAmount.getText().toString();

            if (!incomeAmountStr.isEmpty()) {
                double amount = Double.parseDouble(incomeAmountStr);
                db.addIncome(month, amount);
                Toast.makeText(MainActivity.this, "Доход добавлен!", Toast.LENGTH_SHORT).show();
                updateAnalytics();
            } else {
                Toast.makeText(MainActivity.this, "Введите сумму дохода", Toast.LENGTH_SHORT).show();
            }
        });

        // Добавить расход
        btnAddExpense.setOnClickListener(v -> {
            String category = etExpenseCategory.getText().toString();
            String expenseAmountStr = etExpenseAmount.getText().toString();

            if (!expenseAmountStr.isEmpty()) {
                double amount = Double.parseDouble(expenseAmountStr);
                db.addExpense(category, amount);
                Toast.makeText(MainActivity.this, "Расход добавлен!", Toast.LENGTH_SHORT).show();
                updateAnalytics();
            } else {
                Toast.makeText(MainActivity.this, "Введите сумму расхода", Toast.LENGTH_SHORT).show();
            }
        });


        // Добавить категорию
        btnAddCategory.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString();
            String categoryBudgetStr = etCategoryBudget.getText().toString();

            if (!categoryBudgetStr.isEmpty()) {
                double budget = Double.parseDouble(categoryBudgetStr);
                db.addCategory(name, budget);
                displayCategories();
                Toast.makeText(MainActivity.this, "Категория добавлена!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Введите бюджет категории", Toast.LENGTH_SHORT).show();
            }
        });


        // Отображаем категории и аналитику
        displayCategories();
        updateAnalytics();
        // Найти кнопку сброса данных
        Button btnResetData = findViewById(R.id.btnResetData);

// Обработчик нажатий для сброса данных
        btnResetData.setOnClickListener(v -> {
            db.resetAll();  // Вызов функции сброса всех данных
            updateAnalytics();  // Обновить аналитику (доходы, расходы, баланс)
            Toast.makeText(MainActivity.this, "Все данные сброшены!", Toast.LENGTH_SHORT).show();
        });

    }

    // Обновление аналитики
    @SuppressLint("StringFormatMatches")
    private void updateAnalytics() {
        double totalIncome = db.getTotalIncome();
        double totalExpense = db.getTotalExpense();
        double balance = totalIncome - totalExpense;

        tvTotalIncome.setText(getString(R.string.total_income_label, totalIncome));
        tvTotalExpense.setText(getString(R.string.total_expense_label, totalExpense));
        tvBalance.setText(getString(R.string.balance_label, balance));
    }


    private void displayCategories() {
        // Закрываем предыдущий курсор, если он не null
        Cursor cursor = db.getAllCategories();

        if (cursor != null && !cursor.isClosed()) {
            startManagingCursor(cursor);
        }

        // Убедитесь, что адаптер существует
        String[] from = { "name", "budget" };
        int[] to = { R.id.tvCategoryName, R.id.tvCategoryBudget };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.category_item, cursor, from, to, 0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                ImageButton btnDelete = view.findViewById(R.id.btnDeleteCategory);

                btnDelete.setOnClickListener(v -> {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    db.deleteCategory(id);

                    // После удаления обновляем курсор и адаптер
                    displayCategories();
                    Toast.makeText(MainActivity.this, "Категория удалена!", Toast.LENGTH_SHORT).show();
                });
            }
        };

        lvCategories.setAdapter(adapter);
    }






}
