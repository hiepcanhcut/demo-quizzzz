package com.example.quizapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.util.UserSession;

/**
 * SCR-01: Màn hình Đăng nhập / Đăng ký (PTIT PrepQuizz)
 */
public class LoginActivity extends AppCompatActivity {

    private TextView tabLogin, tabRegister;
    private TextView tvLabelName;
    private EditText etName, etEmail, etPassword;
    private TextView tvErrorMessage;
    private Button btnSubmitAuth;
    private TextView tvSwitchAuth;

    private boolean isLoginMode = true;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSession = new UserSession(this);
        // Nếu đã đăng nhập trước đó thì chuyển tiếp vào MainActivity
        if (userSession.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tabLogin = findViewById(R.id.tabLogin);
        tabRegister = findViewById(R.id.tabRegister);
        tvLabelName = findViewById(R.id.tvLabelName);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnSubmitAuth = findViewById(R.id.btnSubmitAuth);
        tvSwitchAuth = findViewById(R.id.tvSwitchAuth);
    }

    private void setupListeners() {
        tabLogin.setOnClickListener(v -> setAuthMode(true));
        tabRegister.setOnClickListener(v -> setAuthMode(false));
        tvSwitchAuth.setOnClickListener(v -> setAuthMode(!isLoginMode));

        btnSubmitAuth.setOnClickListener(v -> handleAuthSubmit());
    }

    private void setAuthMode(boolean login) {
        isLoginMode = login;
        tvErrorMessage.setVisibility(View.GONE);

        if (isLoginMode) {
            tabLogin.setBackgroundResource(R.drawable.bg_button_primary);
            tabLogin.setTextColor(0xFFFFFFFF);
            tabRegister.setBackgroundResource(android.R.color.transparent);
            tabRegister.setTextColor(getColor(R.color.text_secondary));

            tvLabelName.setVisibility(View.GONE);
            etName.setVisibility(View.GONE);
            btnSubmitAuth.setText("Đăng nhập");
            tvSwitchAuth.setText("Chưa có tài khoản? Đăng ký");
        } else {
            tabRegister.setBackgroundResource(R.drawable.bg_button_primary);
            tabRegister.setTextColor(0xFFFFFFFF);
            tabLogin.setBackgroundResource(android.R.color.transparent);
            tabLogin.setTextColor(getColor(R.color.text_secondary));

            tvLabelName.setVisibility(View.VISIBLE);
            etName.setVisibility(View.VISIBLE);
            btnSubmitAuth.setText("Đăng ký tài khoản");
            tvSwitchAuth.setText("Đã có tài khoản? Đăng nhập");
        }
    }

    private void handleAuthSubmit() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showError("Vui lòng nhập địa chỉ email!");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Địa chỉ email không đúng định dạng!");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showError("Mật khẩu phải chứa ít nhất 6 ký tự!");
            return;
        }

        String name = "Lê Đăng Hiệp";
        if (!isLoginMode) {
            name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                showError("Vui lòng nhập họ và tên của bạn!");
                return;
            }
        }

        // Lưu phiên đăng nhập
        userSession.setLoggedIn(true, email, name);

        // Chuyển tới màn hình chính (SCR-02)
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
}

