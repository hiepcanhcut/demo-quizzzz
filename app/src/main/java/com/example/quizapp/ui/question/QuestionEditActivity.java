package com.example.quizapp.ui.question;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.data.QuestionRepository;
import com.example.quizapp.logic.QuestionValidator;
import com.example.quizapp.model.Question;
import com.example.quizapp.util.AppExecutors;
import com.example.quizapp.util.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * SCR-08: Màn hình Thêm / Sửa câu hỏi theo đúng Wireframe.
 * Gồm Spinner chọn môn học (CSDL, Mạng, HĐH), visual radio card selection cho đáp án đúng,
 * và trường giải thích.
 */
public class QuestionEditActivity extends AppCompatActivity {

    private ImageButton btnBackEdit;
    private TextView tvHeaderTitle;
    private Spinner spnSubject;
    private EditText etContent;
    private EditText etOptionA, etOptionB, etOptionC, etOptionD;
    private LinearLayout cardOptA, cardOptB, cardOptC, cardOptD;
    private RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD;
    private EditText etExplanation;
    private Button btnSave;

    private QuestionRepository repository;
    private long editingQuestionId = -1;
    private Question currentQuestion;
    private int selectedCorrectAnswer = 0; // 0: A, 1: B, 2: C, 3: D

    private final List<String> subjects = Arrays.asList(
            "Cơ sở dữ liệu",
            "Mạng máy tính",
            "Hệ điều hành"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);

        repository = new QuestionRepository(this);
        initViews();
        setupSpinner();
        setupOptionSelection();

        if (getIntent().hasExtra(Constants.EXTRA_QUESTION_ID)) {
            editingQuestionId = getIntent().getLongExtra(Constants.EXTRA_QUESTION_ID, -1);
            tvHeaderTitle.setText("Sửa câu hỏi");
            loadQuestionData(editingQuestionId);
        } else {
            tvHeaderTitle.setText("Thêm câu hỏi");
        }

        btnBackEdit.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveQuestion());
    }

    private void initViews() {
        btnBackEdit = findViewById(R.id.btnBackEdit);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        spnSubject = findViewById(R.id.spnSubject);
        etContent = findViewById(R.id.etContent);

        cardOptA = findViewById(R.id.cardOptA);
        cardOptB = findViewById(R.id.cardOptB);
        cardOptC = findViewById(R.id.cardOptC);
        cardOptD = findViewById(R.id.cardOptD);

        rbOptionA = findViewById(R.id.rbOptionA);
        rbOptionB = findViewById(R.id.rbOptionB);
        rbOptionC = findViewById(R.id.rbOptionC);
        rbOptionD = findViewById(R.id.rbOptionD);

        etOptionA = findViewById(R.id.etOptionA);
        etOptionB = findViewById(R.id.etOptionB);
        etOptionC = findViewById(R.id.etOptionC);
        etOptionD = findViewById(R.id.etOptionD);

        etExplanation = findViewById(R.id.etExplanation);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                subjects
        );
        spnSubject.setAdapter(adapter);
    }

    private void setupOptionSelection() {
        View.OnClickListener selectA = v -> updateSelectedAnswer(0);
        View.OnClickListener selectB = v -> updateSelectedAnswer(1);
        View.OnClickListener selectC = v -> updateSelectedAnswer(2);
        View.OnClickListener selectD = v -> updateSelectedAnswer(3);

        cardOptA.setOnClickListener(selectA);
        rbOptionA.setOnClickListener(selectA);

        cardOptB.setOnClickListener(selectB);
        rbOptionB.setOnClickListener(selectB);

        cardOptC.setOnClickListener(selectC);
        rbOptionC.setOnClickListener(selectC);

        cardOptD.setOnClickListener(selectD);
        rbOptionD.setOnClickListener(selectD);

        updateSelectedAnswer(0);
    }

    private void updateSelectedAnswer(int index) {
        selectedCorrectAnswer = index;

        rbOptionA.setChecked(index == 0);
        rbOptionB.setChecked(index == 1);
        rbOptionC.setChecked(index == 2);
        rbOptionD.setChecked(index == 3);

        cardOptA.setBackgroundResource(index == 0 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
        cardOptB.setBackgroundResource(index == 1 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
        cardOptC.setBackgroundResource(index == 2 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
        cardOptD.setBackgroundResource(index == 3 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);

        int redColor = getResources().getColor(R.color.primary, null);
        int normalColor = getResources().getColor(R.color.text_primary, null);

        rbOptionA.setTextColor(index == 0 ? redColor : normalColor);
        rbOptionB.setTextColor(index == 1 ? redColor : normalColor);
        rbOptionC.setTextColor(index == 2 ? redColor : normalColor);
        rbOptionD.setTextColor(index == 3 ? redColor : normalColor);
    }

    private void loadQuestionData(long id) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            currentQuestion = repository.getById(id);
            if (currentQuestion != null) {
                AppExecutors.getInstance().mainThread().execute(() -> {
                    // Set category spinner
                    String cat = currentQuestion.getCategory();
                    for (int i = 0; i < subjects.size(); i++) {
                        if (subjects.get(i).equalsIgnoreCase(cat)) {
                            spnSubject.setSelection(i);
                            break;
                        }
                    }

                    etContent.setText(currentQuestion.getContent());
                    etOptionA.setText(currentQuestion.getOptionA());
                    etOptionB.setText(currentQuestion.getOptionB());
                    etOptionC.setText(currentQuestion.getOptionC());
                    etOptionD.setText(currentQuestion.getOptionD());
                    etExplanation.setText(currentQuestion.getExplanation());

                    updateSelectedAnswer(currentQuestion.getCorrectAnswer());
                });
            }
        });
    }

    private void saveQuestion() {
        String category = spnSubject.getSelectedItem() != null ? spnSubject.getSelectedItem().toString() : "Cơ sở dữ liệu";
        String content = etContent.getText().toString().trim();
        String optionA = etOptionA.getText().toString().trim();
        String optionB = etOptionB.getText().toString().trim();
        String optionC = etOptionC.getText().toString().trim();
        String optionD = etOptionD.getText().toString().trim();
        String explanation = etExplanation.getText().toString().trim();

        Question q = (currentQuestion != null) ? currentQuestion : new Question();
        q.setCategory(category);
        q.setContent(content);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectAnswer(selectedCorrectAnswer);
        q.setExplanation(explanation);

        QuestionValidator.ValidationResult result = QuestionValidator.validate(q);
        if (!result.isValid()) {
            Toast.makeText(this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            if (editingQuestionId != -1) {
                repository.update(q);
            } else {
                q.setSample(false);
                repository.insert(q);
            }

            AppExecutors.getInstance().mainThread().execute(() -> {
                Toast.makeText(this, "Lưu câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }
}
