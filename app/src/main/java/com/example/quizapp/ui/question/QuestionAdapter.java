package com.example.quizapp.ui.question;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter hiển thị danh sách câu hỏi theo Wireframe SCR-07.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    public interface OnQuestionActionListener {
        void onEdit(Question question);
        void onDelete(Question question);
    }

    private List<Question> questionList = new ArrayList<>();
    private final OnQuestionActionListener actionListener;

    public QuestionAdapter(OnQuestionActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setQuestions(List<Question> questions) {
        this.questionList = questions != null ? questions : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questionList.get(position);
        holder.bind(q, actionListener);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategory;
        private final TextView tvContent;
        private final TextView tvSampleBadge;
        private final ImageButton btnMoreOptions;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvSampleBadge = itemView.findViewById(R.id.tvSampleBadge);
            btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
        }

        public void bind(Question q, OnQuestionActionListener listener) {
            tvContent.setText(q.getContent());
            tvSampleBadge.setVisibility(q.isSample() ? View.VISIBLE : View.GONE);

            // Style category tag
            String category = q.getCategory() != null ? q.getCategory() : "Khác";
            String tagText;
            int tagBgColor;
            int tagTextColor;

            if (category.toLowerCase().contains("dữ liệu") || category.equalsIgnoreCase("csdl")) {
                tagText = "CSDL";
                tagBgColor = Color.parseColor("#E3F2FD");
                tagTextColor = Color.parseColor("#1565C0");
            } else if (category.toLowerCase().contains("mạng")) {
                tagText = "Mạng";
                tagBgColor = Color.parseColor("#E0F2F1");
                tagTextColor = Color.parseColor("#00796B");
            } else if (category.toLowerCase().contains("hành") || category.equalsIgnoreCase("hdh") || category.equalsIgnoreCase("hđh")) {
                tagText = "HĐH";
                tagBgColor = Color.parseColor("#FBE9E7");
                tagTextColor = Color.parseColor("#D84315");
            } else {
                tagText = category;
                tagBgColor = Color.parseColor("#F5F5F5");
                tagTextColor = Color.parseColor("#616161");
            }

            tvCategory.setText(tagText);
            tvCategory.setTextColor(tagTextColor);
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(12f);
            gd.setColor(tagBgColor);
            tvCategory.setBackground(gd);

            // 3-dot popup menu
            btnMoreOptions.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenu().add(0, 1, 0, "Sửa câu hỏi");
                popup.getMenu().add(0, 2, 1, "Xóa câu hỏi");
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == 1) {
                        if (listener != null) listener.onEdit(q);
                        return true;
                    } else if (item.getItemId() == 2) {
                        if (listener != null) listener.onDelete(q);
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }
}
