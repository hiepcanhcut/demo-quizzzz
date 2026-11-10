package com.example.quizapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.R;
import com.example.quizapp.ui.exam.ExamListFragment;
import com.example.quizapp.ui.history.HistoryFragment;
import com.example.quizapp.ui.question.QuestionListFragment;
import com.example.quizapp.util.UserSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Màn hình chính với BottomNavigationView — 3 tab:
 * - Tab 1: Danh sách đề thi (ExamListFragment / SCR-02)
 * - Tab 2: Lịch sử thi (HistoryFragment / SCR-06)
 * - Tab 3: Ngân hàng câu hỏi (QuestionListFragment / SCR-07)
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private UserSession userSession;

    private ExamListFragment examListFragment;
    private HistoryFragment historyFragment;
    private QuestionListFragment questionListFragment;

    private static final String TAG_EXAM = "tab_exam";
    private static final String TAG_HISTORY = "tab_history";
    private static final String TAG_QUESTION = "tab_question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSession = new UserSession(this);

        // Nếu chưa đăng nhập → về Login
        if (!userSession.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bottomNav = findViewById(R.id.bottomNav);
        setupBottomNavigation();

        if (savedInstanceState == null) {
            showFragment(TAG_EXAM);
        }
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_exams) {
                showFragment(TAG_EXAM);
                return true;
            } else if (id == R.id.nav_history) {
                showFragment(TAG_HISTORY);
                return true;
            } else if (id == R.id.nav_questions) {
                showFragment(TAG_QUESTION);
                return true;
            }
            return false;
        });
    }

    private void showFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Ẩn tất cả fragments hiện có
        for (Fragment f : fm.getFragments()) {
            ft.hide(f);
        }

        Fragment existing = fm.findFragmentByTag(tag);
        if (existing != null) {
            ft.show(existing);
        } else {
            Fragment newFrag = createFragment(tag);
            ft.add(R.id.fragmentContainer, newFrag, tag);
        }

        ft.commit();
    }

    private Fragment createFragment(String tag) {
        switch (tag) {
            case TAG_HISTORY:
                if (historyFragment == null) historyFragment = new HistoryFragment();
                return historyFragment;
            case TAG_QUESTION:
                if (questionListFragment == null) questionListFragment = new QuestionListFragment();
                return questionListFragment;
            case TAG_EXAM:
            default:
                if (examListFragment == null) examListFragment = new ExamListFragment();
                return examListFragment;
        }
    }
}

