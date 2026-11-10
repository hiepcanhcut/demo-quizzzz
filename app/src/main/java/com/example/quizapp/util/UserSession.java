package com.example.quizapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Quản lý phiên người dùng và trạng thái ứng dụng (Login, Tiếp tục làm bài, Giả lập trạng thái).
 */
public class UserSession {

    private static final String PREF_NAME = "ptit_prepquiz_prefs";
    private static final String KEY_IS_LOGGED_IN = "key_is_logged_in";
    private static final String KEY_USER_EMAIL = "key_user_email";
    private static final String KEY_USER_NAME = "key_user_name";

    // Tiếp tục làm bài (Resume active exam)
    private static final String KEY_RESUME_EXAM_TITLE = "key_resume_exam_title";
    private static final String KEY_RESUME_CATEGORY = "key_resume_category";
    private static final String KEY_RESUME_CURRENT_Q = "key_resume_current_q";
    private static final String KEY_RESUME_TOTAL_Q = "key_resume_total_q";
    private static final String KEY_RESUME_REMAINING_SEC = "key_resume_remaining_sec";
    private static final String KEY_HAS_RESUME_EXAM = "key_has_resume_exam";

    // Trạng thái demo UI SCR-02 (0: Success, 1: Loading, 2: Empty, 3: Error, 4: Offline)
    private static final String KEY_UI_DEMO_STATE = "key_ui_demo_state";

    private final SharedPreferences prefs;

    public UserSession(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn, String email, String name) {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, loggedIn)
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_NAME, name)
                .apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Lê Đăng Hiệp");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "hiep@ptit.edu.vn");
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    // Active Exam Resume
    public boolean hasActiveExam() {
        return prefs.getBoolean(KEY_HAS_RESUME_EXAM, true); // Mặc định hiển thị để khớp Wireframe
    }

    public void saveActiveExam(String title, String category, int currentQ, int totalQ, int remainingSec) {
        prefs.edit()
                .putBoolean(KEY_HAS_RESUME_EXAM, true)
                .putString(KEY_RESUME_EXAM_TITLE, title)
                .putString(KEY_RESUME_CATEGORY, category)
                .putInt(KEY_RESUME_CURRENT_Q, currentQ)
                .putInt(KEY_RESUME_TOTAL_Q, totalQ)
                .putInt(KEY_RESUME_REMAINING_SEC, remainingSec)
                .apply();
    }

    public void clearActiveExam() {
        prefs.edit().putBoolean(KEY_HAS_RESUME_EXAM, false).apply();
    }

    public String getResumeTitle() {
        return prefs.getString(KEY_RESUME_EXAM_TITLE, "Đề giữa kỳ – Cơ sở dữ liệu");
    }

    public String getResumeCategory() {
        return prefs.getString(KEY_RESUME_CATEGORY, "Cơ sở dữ liệu");
    }

    public int getResumeCurrentQuestion() {
        return prefs.getInt(KEY_RESUME_CURRENT_Q, 5);
    }

    public int getResumeTotalQuestions() {
        return prefs.getInt(KEY_RESUME_TOTAL_Q, 40);
    }

    public int getResumeRemainingSeconds() {
        return prefs.getInt(KEY_RESUME_REMAINING_SEC, 32 * 60 + 15); // 32:15
    }

    // UI Demo State for SCR-02
    public int getDemoState() {
        return prefs.getInt(KEY_UI_DEMO_STATE, 0); // 0: Success, 1: Loading, 2: Empty, 3: Error, 4: Offline
    }

    public void setDemoState(int state) {
        prefs.edit().putInt(KEY_UI_DEMO_STATE, state).apply();
    }
}

