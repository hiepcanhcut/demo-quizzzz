package com.example.quizapp.ui.exam;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.ExamSession;

import java.util.Locale;

/**
 * Hộp thoại hiển thị lưới câu hỏi nhanh (Question Grid) theo Wireframe SCR-04.
 */
public class QuestionGridDialog extends Dialog {

    public interface OnGridItemClickListener {
        void onQuestionSelected(int index);
        void onSubmitRequested();
    }

    private final ExamSession session;
    private final OnGridItemClickListener listener;

    public QuestionGridDialog(@NonNull Context context, ExamSession session, OnGridItemClickListener listener) {
        super(context);
        this.session = session;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_question_grid);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ImageButton btnClose = findViewById(R.id.btnCloseGrid);
        TextView tvHeaderProgress = findViewById(R.id.tvGridHeaderProgress);
        TextView tvStatusSummary = findViewById(R.id.tvGridStatusSummary);
        RecyclerView rvGrid = findViewById(R.id.rvQuestionGrid);
        Button btnSubmit = findViewById(R.id.btnSubmitFromDialog);

        int current = session.getCurrentIndex() + 1;
        int total = session.getQuestionCount();
        tvHeaderProgress.setText(String.format(Locale.getDefault(), "Câu %d/%d", current, total));

        int answered = session.countAnsweredQuestions();
        int flagged = countFlagged();
        tvStatusSummary.setText(String.format(Locale.getDefault(), "%d/%d câu đã làm · %d câu có cờ", answered, total, flagged));

        rvGrid.setLayoutManager(new GridLayoutManager(getContext(), 5));
        rvGrid.setAdapter(new GridAdapter());

        btnClose.setOnClickListener(v -> dismiss());

        btnSubmit.setOnClickListener(v -> {
            dismiss();
            if (listener != null) listener.onSubmitRequested();
        });
    }

    private int countFlagged() {
        if (session.getFlaggedQuestions() == null) return 0;
        int count = 0;
        for (boolean b : session.getFlaggedQuestions()) {
            if (b) count++;
        }
        return count;
    }

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

        @NonNull
        @Override
        public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_grid, parent, false);
            return new GridViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
            holder.tvNumber.setText(String.valueOf(position + 1));

            boolean isCurrent = (position == session.getCurrentIndex());
            boolean isAnswered = (session.getSelectedAnswers()[position] != -1);
            boolean isFlagged = session.getFlaggedQuestions()[position];

            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(24);

            if (isFlagged) {
                // Có cờ: Cam
                drawable.setColor(Color.parseColor("#FF9800"));
                holder.tvNumber.setTextColor(Color.WHITE);
            } else if (isAnswered) {
                // Đã làm: Đỏ PTIT
                drawable.setColor(Color.parseColor("#C62828"));
                holder.tvNumber.setTextColor(Color.WHITE);
            } else {
                // Chưa làm: Trắng viền xám
                drawable.setColor(Color.WHITE);
                drawable.setStroke(3, Color.parseColor("#E0E0E0"));
                holder.tvNumber.setTextColor(Color.parseColor("#424242"));
            }

            if (isCurrent) {
                // Viền đỏ dày nổi bật nếu đang chọn
                drawable.setStroke(6, Color.parseColor("#8E0000"));
            }

            holder.tvNumber.setBackground(drawable);

            holder.itemView.setOnClickListener(v -> {
                dismiss();
                if (listener != null) listener.onQuestionSelected(position);
            });
        }

        @Override
        public int getItemCount() {
            return session.getQuestionCount();
        }

        class GridViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumber;

            public GridViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNumber = itemView.findViewById(R.id.tvGridNumber);
            }
        }
    }
}
