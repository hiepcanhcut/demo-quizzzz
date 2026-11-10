package com.example.quizapp.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.data.HistoryRepository;
import com.example.quizapp.model.ExamHistory;
import com.example.quizapp.ui.exam.ResultActivity;
import com.example.quizapp.util.AppExecutors;

import java.util.List;

/**
 * SCR-06: Fragment Lịch sử bài thi — tab 2 của Bottom Navigation.
 * Hiển thị danh sách bài thi đã làm, điểm số và trạng thái đồng bộ.
 */
public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemClickListener {

    private RecyclerView rvHistory;
    private View llHistoryEmpty;

    private HistoryRepository repository;
    private HistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new HistoryRepository(requireContext());

        rvHistory = view.findViewById(R.id.rvHistory);
        llHistoryEmpty = view.findViewById(R.id.llHistoryEmpty);

        adapter = new HistoryAdapter(this);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(adapter);

        loadHistory();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<ExamHistory> list = repository.getAll();
            AppExecutors.getInstance().mainThread().execute(() -> {
                adapter.setHistoryList(list);
                if (list.isEmpty()) {
                    llHistoryEmpty.setVisibility(View.VISIBLE);
                    rvHistory.setVisibility(View.GONE);
                } else {
                    llHistoryEmpty.setVisibility(View.GONE);
                    rvHistory.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    public void onHistoryClick(ExamHistory history) {
        Intent intent = new Intent(requireContext(), ResultActivity.class);
        intent.putExtra("history_id", history.getId());
        intent.putExtra("score", history.getCorrectCount());
        intent.putExtra("total", history.getTotalQuestions());
        intent.putExtra("duration", history.getDurationSeconds());
        intent.putExtra("exam_title", history.getExamTitle());
        intent.putExtra("sync_status", history.getSyncStatus());
        intent.putExtra("is_history_view", true);
        startActivity(intent);
    }
}
