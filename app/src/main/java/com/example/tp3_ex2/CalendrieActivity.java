package com.example.tp3_ex2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendrieActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvInstruction;
    private FloatingActionButton fabAddPlanning;
    private String selectedDate;
    private String currentUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrie);

        currentUserLogin = getIntent().getStringExtra("USER_LOGIN");
        if (currentUserLogin == null) {
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupCalendar();
        setupFabButton();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        tvInstruction = findViewById(R.id.tv_instruction);
        fabAddPlanning = findViewById(R.id.fab_add_planning);
        selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        ImageView ivMenu = findViewById(R.id.iv_menu);

        ivMenu.setOnClickListener(v -> {
            showPopupMenu(v);
        });
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("USER_LOGIN", currentUserLogin);
        startActivity(intent);
    }

    private void logoutUser() {
        Intent intent = new Intent(this, Connexionounouvelleinsc.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                    dayOfMonth, month + 1, year);
            tvInstruction.setText("Date sélectionnée: " + selectedDate);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            currentUserLogin = data.getStringExtra("USER_LOGIN");
        }
    }

    private void setupFabButton() {
        fabAddPlanning.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlanningActivity.class);
            intent.putExtra("date", selectedDate);
            startActivity(intent);
        });
    }
    private void showPopupMenu(View anchor) {
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceShowIcon.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.toolbar_menu, popup.getMenu());

        Menu menu = popup.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString span = new SpannableString(item.getTitle());
            span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, span.length(), 0);
            item.setTitle(span);
        }

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                openEditProfileActivity();
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                logoutUser();
                return true;
            }
            return false;
        });

        popup.show();
    }
}