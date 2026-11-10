package com.example.quizapp.ui.exam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.data.QuestionRepository;
import com.example.quizapp.model.ExamItem;
import com.example.quizapp.util.Constants;

import java.util.Locale;

/**
 * SCR-03: Màn hình Chi tiết đề thi & Tải đề về máy (Offline-first).
 */
public class ExamDetailActivity extends AppCompatActivity {

    private ImageButton btnBack, btnMore;
    private TextView tvDetailTitle, tvDetailCategory;
    private TextView tvMetricQuestionCount, tvMetricDuration, tvMetricAttempts;
    private ProgressBar pbDownloadProgress;
    private TextView tvDownloadPercent, tvDownloadCount, tvDownloadNote;
    private TextView tvDetailDescription;
    private TextView tvStartCaption;
    private Button btnStartExam;

    private ExamItem examItem;
    private QuestionRepository questionRepository;
    private boolean isDownloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);

        questionRepository = new QuestionRepository(this);

        examItem = (ExamItem) getIntent().getSerializableExtra("exam_item");
        if (examItem == null) {
            examItem = new ExamItem(1, "Đề giữa kỳ", "Cơ sở dữ liệu", 40, 45, true, 100, 2,
                    "Đề thi trắc nghiệm học phần Cơ sở dữ liệu PTIT.");
        }

        initViews();
        displayData();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnMore = findViewById(R.id.btnMore);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvMetricQuestionCount = findViewById(R.id.tvMetricQuestionCount);
        tvMetricDuration = findViewById(R.id.tvMetricDuration);
        tvMetricAttempts = findViewById(R.id.tvMetricAttempts);
        pbDownloadProgress = findViewById(R.id.pbDownloadProgress);
        tvDownloadPercent = findViewById(R.id.tvDownloadPercent);
        tvDownloadCount = findViewById(R.id.tvDownloadCount);
        tvDownloadNote = findViewById(R.id.tvDownloadNote);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvStartCaption = findViewById(R.id.tvStartCaption);
        btnStartExam = findViewById(R.id.btnStartExam);
    }

    private void displayData() {
        tvDetailTitle.setText(examItem.getTitle());
        tvDetailCategory.setText(examItem.getCategory());
        tvMetricQuestionCount.setText(String.valueOf(examItem.getQuestionCount()));
        tvMetricDuration.setText(String.valueOf(examItem.getDurationMinutes()));
        tvMetricAttempts.setText(String.valueOf(examItem.getAttemptCount()));

        if (examItem.getDescription() != null && !examItem.getDescription().isEmpty()) {
            tvDetailDescription.setText(examItem.getDescription());
        }

        isDownloaded = examItem.isDownloaded();
        updateDownloadState(isDownloaded);
    }

    private void updateDownloadState(boolean downloaded) {
        this.isDownloaded = downloaded;
        if (downloaded) {
            pbDownloadProgress.setProgress(100);
            tvDownloadPercent.setText("Đã tải xong 100%");
            tvDownloadCount.setText(String.format(Locale.getDefault(), "%d/%d câu", examItem.getQuestionCount(), examItem.getQuestionCount()));
            tvDownloadNote.setText("Đã lưu vào bộ nhớ máy. Có thể làm bài ngoại tuyến 100%.");

            tvStartCaption.setText("Đề thi đã sẵn sàng");
            btnStartExam.setText("Bắt đầu làm bài");
            btnStartExam.setBackgroundResource(R.drawable.bg_button_primary);
            btnStartExam.setEnabled(true);
        } else {
            pbDownloadProgress.setProgress(65);
            tvDownloadPercent.setText("Đang tải... 65%");
            int partial = (examItem.getQuestionCount() * 65) / 100;
            tvDownloadCount.setText(String.format(Locale.getDefault(), "%d/%d câu", partial, examItem.getQuestionCount()));
            tvDownloadNote.setText("Cần mạng để tải. Sau đó làm bài offline.");

            tvStartCaption.setText("Nhấn để hoàn tất tải và bắt đầu thi");
            btnStartExam.setText("Tiếp tục tải & Vào thi");
            btnStartExam.setBackgroundResource(R.drawable.bg_button_outline);
            btnStartExam.setTextColor(getColor(R.color.primary));
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnMore.setOnClickListener(v -> {
            Toast.makeText(this, "Tùy chọn: Chia sẻ đề thi, Tải lại", Toast.LENGTH_SHORT).show();
        });

        btnStartExam.setOnClickListener(v -> {
            if (!isDownloaded) {
                // Giả lập hoàn tất tải nhanh
                Toast.makeText(this, "Đang tải hoàn tất đề thi...", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    updateDownloadState(true);
                    launchExam();
                }, 600);
            } else {
                launchExam();
            }
        });
    }

    private void launchExam() {
        long[] ids = questionRepository.pickRandomIds(examItem.getCategory(), examItem.getQuestionCount());
        if (ids.length == 0) {
            ids = questionRepository.pickRandomIds(null, 40);
        }

        Intent intent = new Intent(this, ExamActivity.class);
        intent.putExtra(Constants.EXTRA_QUESTION_IDS, ids);
        intent.putExtra(Constants.EXTRA_CATEGORY, examItem.getCategory());
        intent.putExtra("exam_title", examItem.getTitle());
        startActivity(intent);
        finish();
    }
}

