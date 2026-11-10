package com.example.quizapp.ui.exam;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.ExamItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị danh sách đề thi (SCR-02).
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    public interface OnExamItemClickListener {
        void onExamSelected(ExamItem item);
        void onExamActionClicked(ExamItem item);
    }

    private List<ExamItem> examList = new ArrayList<>();
    private final OnExamItemClickListener listener;

    public ExamAdapter(OnExamItemClickListener listener) {
        this.listener = listener;
    }

    public void setExamList(List<ExamItem> list) {
        this.examList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        ExamItem item = examList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    class ExamViewHolder extends RecyclerView.ViewHolder {
        private final View viewCategoryStrip;
        private final TextView tvExamTitle;
        private final TextView tvExamMeta;
        private final TextView tvDownloadStatus;
        private final Button btnExamAction;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            viewCategoryStrip = itemView.findViewById(R.id.viewCategoryStrip);
            tvExamTitle = itemView.findViewById(R.id.tvExamTitle);
            tvExamMeta = itemView.findViewById(R.id.tvExamMeta);
            tvDownloadStatus = itemView.findViewById(R.id.tvDownloadStatus);
            btnExamAction = itemView.findViewById(R.id.btnExamAction);
        }

        public void bind(ExamItem item) {
            tvExamTitle.setText(item.getTitle());
            tvExamMeta.setText(String.format(Locale.getDefault(), "%d câu · %d phút", item.getQuestionCount(), item.getDurationMinutes()));

            // Strip color by subject
            if (item.getCategory() != null) {
                if (item.getCategory().contains("Cơ sở dữ liệu")) {
                    viewCategoryStrip.setBackgroundColor(Color.parseColor("#1976D2")); // Blue
                } else if (item.getCategory().contains("Mạng")) {
                    viewCategoryStrip.setBackgroundColor(Color.parseColor("#00897B")); // Teal
                } else if (item.getCategory().contains("Hệ điều hành")) {
                    viewCategoryStrip.setBackgroundColor(Color.parseColor("#D81B60")); // Pink/Purple
                } else {
                    viewCategoryStrip.setBackgroundColor(Color.parseColor("#C62828"));
                }
            }

            // Download status
            if (item.isDownloaded()) {
                tvDownloadStatus.setText("Đã tải");
                tvDownloadStatus.setTextColor(Color.parseColor("#1976D2"));

                btnExamAction.setText("Làm bài");
                btnExamAction.setBackgroundResource(R.drawable.bg_button_primary);
                btnExamAction.setTextColor(Color.WHITE);
            } else {
                tvDownloadStatus.setText("Chưa tải – cần mạng");
                tvDownloadStatus.setTextColor(Color.parseColor("#757575"));

                btnExamAction.setText("Tải về");
                btnExamAction.setBackgroundResource(R.drawable.bg_button_outline);
                btnExamAction.setTextColor(Color.parseColor("#C62828"));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onExamSelected(item);
            });

            btnExamAction.setOnClickListener(v -> {
                if (listener != null) listener.onExamActionClicked(item);
            });
        }
    }
}

