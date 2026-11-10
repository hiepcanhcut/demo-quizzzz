package com.example.quizapp.ui.exam;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.AnswerResult;
import com.example.quizapp.model.ExamHistory;
import com.example.quizapp.model.Question;
import com.example.quizapp.ui.MainActivity;
import com.example.quizapp.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * SCR-05: Màn hình Kết quả và Lời giải chi tiết.
 */
public class ResultActivity extends AppCompatActivity {

    private ImageButton btnBackResult;
    private TextView tvScoreNumber;
    private TextView tvScoreSummary;
    private LinearLayout layoutSyncBadge;
    private TextView tvSyncIcon, tvSyncStatus;

    private RecyclerView rvQuestionResultGrid;
    private View viewDetailStatusStrip;
    private TextView tvDetailQuestionTitle, tvDetailUserSelection, tvDetailQuestionContent, tvDetailExplanation;

    private Button btnReviewAllAnswers, btnRetakeExam;

    private List<AnswerResult> resultList = new ArrayList<>();
    private String examCategory = "Cơ sở dữ liệu";
    private String examTitle = "Đề giữa kỳ – Cơ sở dữ liệu";
    private int selectedIndex = 0;
    private ResultGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViews();
        readIntentData();
        setupListeners();
    }

    private void initViews() {
        btnBackResult = findViewById(R.id.btnBackResult);
        tvScoreNumber = findViewById(R.id.tvScoreNumber);
        tvScoreSummary = findViewById(R.id.tvScoreSummary);
        layoutSyncBadge = findViewById(R.id.layoutSyncBadge);
        tvSyncIcon = findViewById(R.id.tvSyncIcon);
        tvSyncStatus = findViewById(R.id.tvSyncStatus);

        rvQuestionResultGrid = findViewById(R.id.rvQuestionResultGrid);
        viewDetailStatusStrip = findViewById(R.id.viewDetailStatusStrip);
        tvDetailQuestionTitle = findViewById(R.id.tvDetailQuestionTitle);
        tvDetailUserSelection = findViewById(R.id.tvDetailUserSelection);
        tvDetailQuestionContent = findViewById(R.id.tvDetailQuestionContent);
        tvDetailExplanation = findViewById(R.id.tvDetailExplanation);

        btnReviewAllAnswers = findViewById(R.id.btnReviewAllAnswers);
        btnRetakeExam = findViewById(R.id.btnRetakeExam);

        rvQuestionResultGrid.setLayoutManager(new GridLayoutManager(this, 8));
        gridAdapter = new ResultGridAdapter();
        rvQuestionResultGrid.setAdapter(gridAdapter);
    }

    private void readIntentData() {
        int correctCount = getIntent().getIntExtra("correct_count", 34);
        int totalCount = getIntent().getIntExtra("total_count", 40);
        int durationSec = getIntent().getIntExtra("duration_seconds", 1720); // 28m40s

        if (getIntent().hasExtra("exam_title")) {
            examTitle = getIntent().getStringExtra("exam_title");
        }
        if (getIntent().hasExtra(Constants.EXTRA_CATEGORY)) {
            examCategory = getIntent().getStringExtra(Constants.EXTRA_CATEGORY);
        }

        @SuppressWarnings("unchecked")
        List<AnswerResult> details = (List<AnswerResult>) getIntent().getSerializableExtra(Constants.EXTRA_EXAM_RESULT);
        if (details != null && !details.isEmpty()) {
            this.resultList = details;
        } else {
            // Khởi tạo danh sách mẫu nếu mở lại từ Lịch sử
            createSampleResults(totalCount, correctCount);
        }

        // Tính điểm thang 10
        double score10 = totalCount > 0 ? (correctCount * 10.0) / totalCount : 0.0;
        tvScoreNumber.setText(String.format(Locale.getDefault(), "%.1f", score10));

        int minutes = durationSec / 60;
        int seconds = durationSec % 60;
        tvScoreSummary.setText(String.format(Locale.getDefault(), "%d/%d câu đúng · %d phút %02d giây",
                correctCount, totalCount, minutes, seconds));

        // Trạng thái đồng bộ
        int syncStatus = getIntent().getIntExtra("sync_status", ExamHistory.SYNC_PENDING);
        if (syncStatus == ExamHistory.SYNC_SUCCESS) {
            tvSyncIcon.setText("✅");
            tvSyncStatus.setText("Đã đồng bộ");
            tvSyncStatus.setTextColor(Color.parseColor("#2E7D32"));
            layoutSyncBadge.setBackgroundResource(R.drawable.bg_option_correct);
        } else if (syncStatus == ExamHistory.SYNC_ERROR) {
            tvSyncIcon.setText("❌");
            tvSyncStatus.setText("Lỗi đồng bộ");
            tvSyncStatus.setTextColor(Color.parseColor("#C62828"));
            layoutSyncBadge.setBackgroundResource(R.drawable.bg_option_wrong);
        } else {
            tvSyncIcon.setText("⏳");
            tvSyncStatus.setText("Chờ đồng bộ");
            tvSyncStatus.setTextColor(Color.parseColor("#E65100"));
            layoutSyncBadge.setBackgroundResource(R.drawable.bg_warning_banner);
        }

        gridAdapter.notifyDataSetChanged();

        // Mặc định chọn câu sai đầu tiên hoặc câu 1
        int firstWrong = -1;
        for (int i = 0; i < resultList.size(); i++) {
            if (!resultList.get(i).isCorrect()) {
                firstWrong = i;
                break;
            }
        }
        selectQuestionDetail(firstWrong != -1 ? firstWrong : 0);
    }

    private void createSampleResults(int total, int correct) {
        resultList.clear();
        for (int i = 0; i < total; i++) {
            boolean isCorrect = i < correct;
            Question q = new Question(i + 1,
                    "Trong mô hình quan hệ, khóa chính (Primary Key) dùng để làm gì?",
                    examCategory,
                    "Xác định duy nhất mỗi bản ghi",
                    "Sắp xếp dữ liệu theo thứ tự",
                    "Mã hóa dữ liệu trong bảng",
                    "Sao lưu dữ liệu định kỳ",
                    0,
                    "Khóa chính dùng để định danh duy nhất mỗi bản ghi trong bảng cơ sở dữ liệu quan hệ.",
                    true, 0, 0);

            resultList.add(new AnswerResult(q, isCorrect ? 0 : 2, isCorrect));
        }
    }

    private void selectQuestionDetail(int index) {
        if (index < 0 || index >= resultList.size()) return;
        this.selectedIndex = index;
        gridAdapter.notifyDataSetChanged();

        AnswerResult item = resultList.get(index);
        Question q = item.getQuestion();

        String statusStr = item.isCorrect() ? "Đúng" : "Sai";
        tvDetailQuestionTitle.setText(String.format(Locale.getDefault(), "Câu %d – %s", index + 1, statusStr));

        if (item.isCorrect()) {
            tvDetailQuestionTitle.setTextColor(Color.parseColor("#2E7D32"));
            viewDetailStatusStrip.setBackgroundColor(Color.parseColor("#4CAF50"));
            tvDetailUserSelection.setText(String.format("Bạn chọn đúng: %s", getOptionLetter(item.getSelectedOption())));
            tvDetailUserSelection.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            tvDetailQuestionTitle.setTextColor(Color.parseColor("#C62828"));
            viewDetailStatusStrip.setBackgroundColor(Color.parseColor("#E53935"));
            String userOpt = item.getSelectedOption() >= 0 ? getOptionLetter(item.getSelectedOption()) : "Chưa chọn";
            String correctOpt = getOptionLetter(q.getCorrectAnswer());
            tvDetailUserSelection.setText(String.format("Bạn chọn: %s · Đáp án đúng: %s", userOpt, correctOpt));
            tvDetailUserSelection.setTextColor(Color.parseColor("#C62828"));
        }

        tvDetailQuestionContent.setText(q.getContent());

        if (q.getExplanation() != null && !q.getExplanation().trim().isEmpty()) {
            tvDetailExplanation.setText(q.getExplanation());
        } else {
            tvDetailExplanation.setText("Không có lời giải thích bổ sung cho câu này.");
        }
    }

    private String getOptionLetter(int index) {
        switch (index) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            default: return "Không";
        }
    }

    private void setupListeners() {
        btnBackResult.setOnClickListener(v -> finish());

        btnReviewAllAnswers.setOnClickListener(v -> {
            // Chuyển tới câu tiếp theo
            if (selectedIndex < resultList.size() - 1) {
                selectQuestionDetail(selectedIndex + 1);
            } else {
                selectQuestionDetail(0);
            }
        });

        btnRetakeExam.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ExamActivity.class);
            intent.putExtra(Constants.EXTRA_CATEGORY, examCategory);
            intent.putExtra("exam_title", examTitle);
            startActivity(intent);
            finish();
        });
    }

    private class ResultGridAdapter extends RecyclerView.Adapter<ResultGridAdapter.ResultGridViewHolder> {

        @NonNull
        @Override
        public ResultGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_grid, parent, false);
            return new ResultGridViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ResultGridViewHolder holder, int position) {
            holder.tvNumber.setText(String.valueOf(position + 1));
            AnswerResult result = resultList.get(position);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(16);

            if (result.isCorrect()) {
                // Đúng: Xanh lá nhạt, chữ xanh đậm
                drawable.setColor(Color.parseColor("#E8F5E9"));
                holder.tvNumber.setTextColor(Color.parseColor("#2E7D32"));
            } else {
                // Sai: Đỏ nhạt, chữ đỏ đậm
                drawable.setColor(Color.parseColor("#FFEBEE"));
                holder.tvNumber.setTextColor(Color.parseColor("#C62828"));
            }

            if (position == selectedIndex) {
                drawable.setStroke(4, Color.parseColor("#212121"));
            }

            holder.tvNumber.setBackground(drawable);
            holder.itemView.setOnClickListener(v -> selectQuestionDetail(position));
        }

        @Override
        public int getItemCount() {
            return resultList.size();
        }

        class ResultGridViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumber;

            public ResultGridViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNumber = itemView.findViewById(R.id.tvGridNumber);
            }
        }
    }
}
