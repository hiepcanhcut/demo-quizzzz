package com.example.quizapp.ui.history;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.ExamHistory;
import com.example.quizapp.util.TimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị danh sách các bài thi đã làm trong lịch sử (SCR-06).
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryItemClickListener {
        void onHistoryClick(ExamHistory history);
    }

    private List<ExamHistory> historyList = new ArrayList<>();
    private final OnHistoryItemClickListener listener;

    public HistoryAdapter(OnHistoryItemClickListener listener) {
        this.listener = listener;
    }

    public void setHistoryList(List<ExamHistory> list) {
        this.historyList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ExamHistory item = historyList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvHistoryTitle;
        private final TextView tvHistoryDateDuration;
        private final TextView tvHistorySyncStatus;
        private final TextView tvHistoryScore10;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryTitle = itemView.findViewById(R.id.tvHistoryTitle);
            tvHistoryDateDuration = itemView.findViewById(R.id.tvHistoryDateDuration);
            tvHistorySyncStatus = itemView.findViewById(R.id.tvHistorySyncStatus);
            tvHistoryScore10 = itemView.findViewById(R.id.tvHistoryScore10);
        }

        public void bind(ExamHistory item) {
            tvHistoryTitle.setText(item.getExamTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateStr = sdf.format(new Date(item.getTakenAt()));
            String durationStr = TimeFormatter.formatMMSS(item.getDurationSeconds());
            tvHistoryDateDuration.setText(String.format("%s · %s", dateStr, durationStr));

            tvHistoryScore10.setText(String.format(Locale.getDefault(), "%.1f", item.getScore10()));

            // Đồng bộ
            tvHistorySyncStatus.setText(item.getSyncStatusText());
            if (item.getSyncStatus() == ExamHistory.SYNC_SUCCESS) {
                tvHistorySyncStatus.setTextColor(Color.parseColor("#2E7D32"));
            } else if (item.getSyncStatus() == ExamHistory.SYNC_ERROR) {
                tvHistorySyncStatus.setTextColor(Color.parseColor("#C62828"));
            } else {
                tvHistorySyncStatus.setTextColor(Color.parseColor("#E65100"));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onHistoryClick(item);
            });
        }
    }
}
