package com.example.quizapp.ui.exam;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.data.HistoryRepository;
import com.example.quizapp.data.QuestionRepository;
import com.example.quizapp.logic.ExamEngine;
import com.example.quizapp.model.ExamHistory;
import com.example.quizapp.model.ExamSession;
import com.example.quizapp.model.Question;
import com.example.quizapp.util.AppExecutors;
import com.example.quizapp.util.Constants;
import com.example.quizapp.util.TimeFormatter;
import com.example.quizapp.util.UserSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * SCR-04: Màn hình Làm bài thi (Core Exam Engine).
 */
public class ExamActivity extends AppCompatActivity implements QuestionGridDialog.OnGridItemClickListener {

    private static final String KEY_EXAM_SESSION = "key_exam_session";

    private ImageButton btnCloseExam;
    private TextView tvQuestionProgressHeader;
    private TextView tvTimer;
    private TextView tvAnsweredCount;
    private ProgressBar progressBar;

    private TextView tvQuestionNumber;
    private TextView tvQuestionContent;

    private LinearLayout layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD;
    private RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;

    private LinearLayout btnFlagAction, btnGridAction;
    private ImageView ivFlagIcon;
    private TextView tvFlagText;

    private Button btnPrevious, btnNext;

    private ExamSession examSession;
    private List<Question> questionList = new ArrayList<>();
    private QuestionRepository questionRepository;
    private HistoryRepository historyRepository;
    private UserSession userSession;

    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;
    private boolean isSubmitted = false;
    private String examCategory = "Tổng hợp";
    private String examTitle = "Đề giữa kỳ – Cơ sở dữ liệu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        questionRepository = new QuestionRepository(this);
        historyRepository = new HistoryRepository(this);
        userSession = new UserSession(this);

        initViews();
        setupListeners();

        if (getIntent().hasExtra("exam_title")) {
            examTitle = getIntent().getStringExtra("exam_title");
        }
        if (getIntent().hasExtra(Constants.EXTRA_CATEGORY)) {
            examCategory = getIntent().getStringExtra(Constants.EXTRA_CATEGORY);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_EXAM_SESSION)) {
            examSession = (ExamSession) savedInstanceState.getSerializable(KEY_EXAM_SESSION);
            loadQuestionsAndDisplay();
        } else {
            long[] questionIds = getIntent().getLongArrayExtra(Constants.EXTRA_QUESTION_IDS);
            if (questionIds == null || questionIds.length == 0) {
                // Lấy 40 câu hỏi mẫu nếu chưa truyền
                questionIds = questionRepository.pickRandomIds(examCategory, 40);
                if (questionIds.length == 0) {
                    questionIds = questionRepository.pickRandomIds(null, 40);
                }
            }

            if (questionIds == null || questionIds.length == 0) {
                Toast.makeText(this, "Chưa có câu hỏi cho đề này!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            int totalSeconds = 45 * 60; // 45 phút chuẩn
            examSession = new ExamSession(questionIds, totalSeconds, SystemClock.elapsedRealtime());
            loadQuestionsAndDisplay();
        }

        startTimer();
    }

    private void initViews() {
        btnCloseExam = findViewById(R.id.btnCloseExam);
        tvQuestionProgressHeader = findViewById(R.id.tvQuestionProgressHeader);
        tvTimer = findViewById(R.id.tvTimer);
        tvAnsweredCount = findViewById(R.id.tvAnsweredCount);
        progressBar = findViewById(R.id.progressBar);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);

        layoutOptionA = findViewById(R.id.layoutOptionA);
        layoutOptionB = findViewById(R.id.layoutOptionB);
        layoutOptionC = findViewById(R.id.layoutOptionC);
        layoutOptionD = findViewById(R.id.layoutOptionD);

        rbOptionA = findViewById(R.id.rbOptionA);
        rbOptionB = findViewById(R.id.rbOptionB);
        rbOptionC = findViewById(R.id.rbOptionC);
        rbOptionD = findViewById(R.id.rbOptionD);

        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);

        btnFlagAction = findViewById(R.id.btnFlagAction);
        btnGridAction = findViewById(R.id.btnGridAction);
        ivFlagIcon = findViewById(R.id.ivFlagIcon);
        tvFlagText = findViewById(R.id.tvFlagText);

        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void setupListeners() {
        btnCloseExam.setOnClickListener(v -> showExitConfirmationDialog());

        layoutOptionA.setOnClickListener(v -> selectOption(0));
        layoutOptionB.setOnClickListener(v -> selectOption(1));
        layoutOptionC.setOnClickListener(v -> selectOption(2));
        layoutOptionD.setOnClickListener(v -> selectOption(3));

        btnFlagAction.setOnClickListener(v -> toggleFlag());
        btnGridAction.setOnClickListener(v -> showQuestionGridDialog());

        btnPrevious.setOnClickListener(v -> {
            if (examSession != null && examSession.getCurrentIndex() > 0) {
                examSession.setCurrentIndex(examSession.getCurrentIndex() - 1);
                displayCurrentQuestion();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (examSession != null) {
                if (examSession.getCurrentIndex() < examSession.getQuestionCount() - 1) {
                    examSession.setCurrentIndex(examSession.getCurrentIndex() + 1);
                    displayCurrentQuestion();
                } else {
                    showSubmitConfirmationDialog();
                }
            }
        });
    }

    private void loadQuestionsAndDisplay() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            long[] ids = examSession.getQuestionIds();
            List<Question> loaded = questionRepository.getByIds(ids);

            AppExecutors.getInstance().mainThread().execute(() -> {
                questionList = loaded;
                if (questionList.isEmpty()) {
                    Toast.makeText(this, "Không thể tải danh sách câu hỏi!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                progressBar.setMax(questionList.size());
                displayCurrentQuestion();
            });
        });
    }

    private void displayCurrentQuestion() {
        if (questionList.isEmpty() || examSession == null) return;

        int index = examSession.getCurrentIndex();
        int total = questionList.size();

        tvQuestionProgressHeader.setText(String.format(Locale.getDefault(), "Câu %d/%d", index + 1, total));
        tvQuestionNumber.setText(String.format(Locale.getDefault(), "Câu %d", index + 1));

        int answered = examSession.countAnsweredQuestions();
        tvAnsweredCount.setText(String.format(Locale.getDefault(), "Đã làm %d/%d", answered, total));
        progressBar.setProgress(answered);

        Question q = questionList.get(index);
        tvQuestionContent.setText(q.getContent());

        tvOptionA.setText(String.format("A. %s", q.getOptionA()));
        tvOptionB.setText(String.format("B. %s", q.getOptionB()));
        tvOptionC.setText(String.format("C. %s", q.getOptionC()));
        tvOptionD.setText(String.format("D. %s", q.getOptionD()));

        // Highlight selected option
        int selected = examSession.getSelectedAnswers()[index];
        resetOptionViews();

        if (selected == 0) highlightOption(layoutOptionA, rbOptionA);
        else if (selected == 1) highlightOption(layoutOptionB, rbOptionB);
        else if (selected == 2) highlightOption(layoutOptionC, rbOptionC);
        else if (selected == 3) highlightOption(layoutOptionD, rbOptionD);

        // Update Flag UI
        boolean isFlagged = examSession.getFlaggedQuestions()[index];
        if (isFlagged) {
            btnFlagAction.setBackgroundResource(R.drawable.bg_timer_badge);
            ivFlagIcon.setColorFilter(Color.parseColor("#FF9800"));
            tvFlagText.setText("Bỏ cờ");
            tvFlagText.setTextColor(Color.parseColor("#FF9800"));
        } else {
            btnFlagAction.setBackgroundResource(R.drawable.bg_option_default);
            ivFlagIcon.setColorFilter(Color.parseColor("#757575"));
            tvFlagText.setText("Gắn cờ");
            tvFlagText.setTextColor(Color.parseColor("#212121"));
        }

        // Navigation button text
        btnPrevious.setEnabled(index > 0);
        btnPrevious.setAlpha(index > 0 ? 1.0f : 0.4f);

        if (index == total - 1) {
            btnNext.setText("Nộp bài");
        } else {
            btnNext.setText("Sau >");
        }

        // Cập nhật card tiếp tục làm bài ở ngoài
        userSession.saveActiveExam(examTitle, examCategory, index + 1, total, calculateRemainingSeconds());
    }

    private void resetOptionViews() {
        LinearLayout[] layouts = new LinearLayout[]{layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD};
        RadioButton[] radios = new RadioButton[]{rbOptionA, rbOptionB, rbOptionC, rbOptionD};

        for (int i = 0; i < 4; i++) {
            layouts[i].setBackgroundResource(R.drawable.bg_option_default);
            radios[i].setChecked(false);
        }
    }

    private void highlightOption(LinearLayout layout, RadioButton radio) {
        layout.setBackgroundResource(R.drawable.bg_option_selected);
        radio.setChecked(true);
    }

    private void selectOption(int optionIndex) {
        if (examSession != null) {
            examSession.selectAnswer(examSession.getCurrentIndex(), optionIndex);
            displayCurrentQuestion();
        }
    }

    private void toggleFlag() {
        if (examSession != null) {
            examSession.toggleFlag(examSession.getCurrentIndex());
            displayCurrentQuestion();
        }
    }

    private void showQuestionGridDialog() {
        if (examSession == null) return;
        QuestionGridDialog dialog = new QuestionGridDialog(this, examSession, this);
        dialog.show();
    }

    @Override
    public void onQuestionSelected(int index) {
        if (examSession != null) {
            examSession.setCurrentIndex(index);
            displayCurrentQuestion();
        }
    }

    @Override
    public void onSubmitRequested() {
        showSubmitConfirmationDialog();
    }

    private void showSubmitConfirmationDialog() {
        int total = examSession.getQuestionCount();
        int answered = examSession.countAnsweredQuestions();
        int unAnswered = total - answered;

        String message = String.format(Locale.getDefault(),
                "Bạn đã hoàn thành %d/%d câu hỏi.\n%s\nBạn có chắc chắn muốn nộp bài không?",
                answered, total,
                unAnswered > 0 ? "⚠️ Còn " + unAnswered + " câu chưa trả lời!" : "Tất cả các câu đã được trả lời.");

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận nộp bài")
                .setMessage(message)
                .setPositiveButton("Nộp bài", (dialog, which) -> submitExam())
                .setNegativeButton("Làm tiếp", null)
                .show();
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát bài thi")
                .setMessage("Tiến trình bài thi sẽ được lưu tự động để bạn có thể tiếp tục làm sau. Bạn có muốn thoát?")
                .setPositiveButton("Thoát", (dialog, which) -> finish())
                .setNegativeButton("Ở lại", null)
                .show();
    }

    private int calculateRemainingSeconds() {
        if (examSession == null) return 0;
        long elapsedSec = (SystemClock.elapsedRealtime() - examSession.getStartElapsedRealtimeMs()) / 1000L;
        return (int) Math.max(0, examSession.getTotalTimeSeconds() - elapsedSec);
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isSubmitted) return;
                int remaining = calculateRemainingSeconds();
                tvTimer.setText(TimeFormatter.formatMMSS(remaining));

                if (remaining <= 0) {
                    Toast.makeText(ExamActivity.this, "Hết thời gian làm bài!", Toast.LENGTH_LONG).show();
                    submitExam();
                } else {
                    timerHandler.postDelayed(this, 1000);
                }
            }
        };
        timerHandler.post(timerRunnable);
    }

    private void submitExam() {
        if (isSubmitted) return;
        isSubmitted = true;
        timerHandler.removeCallbacks(timerRunnable);

        long elapsedSec = (SystemClock.elapsedRealtime() - examSession.getStartElapsedRealtimeMs()) / 1000L;
        int durationSeconds = (int) Math.min(elapsedSec, examSession.getTotalTimeSeconds());

        // Chấm điểm
        ExamEngine.GradingOutput output = ExamEngine.grade(questionList, examSession.getSelectedAnswers());

        // Lưu vào CSDL
        AppExecutors.getInstance().diskIO().execute(() -> {
            ExamHistory history = new ExamHistory(0, examTitle, examCategory,
                    output.getTotalQuestions(), output.getCorrectCount(),
                    output.getScorePercent(), durationSeconds,
                    System.currentTimeMillis(), ExamHistory.SYNC_PENDING);

            long historyId = historyRepository.insert(history);
            userSession.clearActiveExam();

            AppExecutors.getInstance().mainThread().execute(() -> {
                Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
                intent.putExtra(Constants.EXTRA_CATEGORY, examCategory);
                intent.putExtra("exam_title", examTitle);
                intent.putExtra("score_percent", output.getScorePercent());
                intent.putExtra("correct_count", output.getCorrectCount());
                intent.putExtra("total_count", output.getTotalQuestions());
                intent.putExtra("duration_seconds", durationSeconds);
                intent.putExtra("history_id", historyId);
                intent.putExtra(Constants.EXTRA_EXAM_RESULT, (Serializable) output.getDetails());
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (examSession != null) {
            outState.putSerializable(KEY_EXAM_SESSION, examSession);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
