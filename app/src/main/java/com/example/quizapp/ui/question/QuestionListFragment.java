package com.example.quizapp.ui.question;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.data.QuestionRepository;
import com.example.quizapp.model.Question;
import com.example.quizapp.util.AppExecutors;
import com.example.quizapp.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * SCR-07: Fragment Ngân hàng câu hỏi — Tab 3 của Bottom Navigation.
 * Kèm bộ lọc môn học, 3-dot menu, và Hộp thoại xác nhận xóa tuỳ chỉnh (SCR-07 Wireframe).
 */
public class QuestionListFragment extends Fragment implements QuestionAdapter.OnQuestionActionListener {

    private TextView chipQAll, chipQCsdl, chipQMang, chipQHdh;
    private RecyclerView rvQuestions;
    private View llQuestionEmpty;
    private FloatingActionButton fabAddQuestion;

    private QuestionRepository repository;
    private QuestionAdapter adapter;

    private String selectedCategory = "Tất cả";
    private List<Question> allQuestions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new QuestionRepository(requireContext());

        initViews(view);
        setupListeners();
        loadQuestions();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestions();
    }

    private void initViews(View view) {
        chipQAll = view.findViewById(R.id.chipQAll);
        chipQCsdl = view.findViewById(R.id.chipQCsdl);
        chipQMang = view.findViewById(R.id.chipQMang);
        chipQHdh = view.findViewById(R.id.chipQHdh);

        rvQuestions = view.findViewById(R.id.rvQuestions);
        llQuestionEmpty = view.findViewById(R.id.llQuestionEmpty);
        fabAddQuestion = view.findViewById(R.id.fabAddQuestion);

        adapter = new QuestionAdapter(this);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvQuestions.setAdapter(adapter);
    }

    private void setupListeners() {
        chipQAll.setOnClickListener(v -> selectFilter("Tất cả", chipQAll));
        chipQCsdl.setOnClickListener(v -> selectFilter("Cơ sở dữ liệu", chipQCsdl));
        chipQMang.setOnClickListener(v -> selectFilter("Mạng máy tính", chipQMang));
        chipQHdh.setOnClickListener(v -> selectFilter("Hệ điều hành", chipQHdh));

        fabAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuestionEditActivity.class);
            startActivity(intent);
        });
    }

    private void selectFilter(String category, TextView selectedChip) {
        this.selectedCategory = category;

        TextView[] chips = new TextView[]{chipQAll, chipQCsdl, chipQMang, chipQHdh};
        for (TextView c : chips) {
            c.setBackgroundResource(R.drawable.bg_chip_unselected);
            c.setTextColor(getResources().getColor(R.color.text_secondary, null));
        }

        selectedChip.setBackgroundResource(R.drawable.bg_chip_selected);
        selectedChip.setTextColor(0xFFFFFFFF);

        filterQuestions();
    }

    private void loadQuestions() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            allQuestions = repository.search(null, null);
            AppExecutors.getInstance().mainThread().execute(this::filterQuestions);
        });
    }

    private void filterQuestions() {
        List<Question> filtered = new ArrayList<>();
        for (Question q : allQuestions) {
            if ("Tất cả".equalsIgnoreCase(selectedCategory)) {
                filtered.add(q);
            } else if (q.getCategory() != null && q.getCategory().toLowerCase().contains(selectedCategory.toLowerCase())) {
                filtered.add(q);
            }
        }

        adapter.setQuestions(filtered);
        if (filtered.isEmpty()) {
            llQuestionEmpty.setVisibility(View.VISIBLE);
            rvQuestions.setVisibility(View.GONE);
        } else {
            llQuestionEmpty.setVisibility(View.GONE);
            rvQuestions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEdit(Question question) {
        Intent intent = new Intent(requireContext(), QuestionEditActivity.class);
        intent.putExtra(Constants.EXTRA_QUESTION_ID, question.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(Question question) {
        showCustomDeleteDialog(question);
    }

    /**
     * Hộp thoại xác nhận xóa theo đúng Wireframe SCR-07
     */
    private void showCustomDeleteDialog(Question question) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_confirm);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.85),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        TextView tvPreview = dialog.findViewById(R.id.tvDeleteQuestionPreview);
        Button btnCancel = dialog.findViewById(R.id.btnDeleteCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnDeleteConfirm);

        tvPreview.setText("Môn: " + question.getCategory() + " – " + question.getContent());

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            AppExecutors.getInstance().diskIO().execute(() -> {
                repository.delete(question.getId());
                AppExecutors.getInstance().mainThread().execute(() -> {
                    Toast.makeText(requireContext(), "Đã xóa câu hỏi", Toast.LENGTH_SHORT).show();
                    loadQuestions();
                });
            });
        });

        dialog.show();
    }
}

