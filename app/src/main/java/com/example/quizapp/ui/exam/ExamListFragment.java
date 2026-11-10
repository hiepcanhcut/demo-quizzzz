package com.example.quizapp.ui.exam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.data.QuestionRepository;
import com.example.quizapp.model.ExamItem;
import com.example.quizapp.model.Question;
import com.example.quizapp.ui.LoginActivity;
import com.example.quizapp.util.Constants;
import com.example.quizapp.util.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * SCR-02: Màn hình Danh sách đề thi (Hỗ trợ đầy đủ 5 trạng thái UI theo Sơ đồ 4).
 */
public class ExamListFragment extends Fragment implements ExamAdapter.OnExamItemClickListener {

    public static final int STATE_SUCCESS = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_OFFLINE = 4;

    private TextView tvUserNameHeader;
    private View btnProfileAvatar;
    private View btnSearchExam;
    private View btnRandomExam;
    private LinearLayout llOfflineBanner;
    private CardView cardResumeExam;
    private TextView tvResumeTitle, tvResumeProgress;
    private ProgressBar pbResumeProgress;
    private Button btnResumeAction;

    private TextView chipAll, chipCsdl, chipMang, chipHdh;
    private LinearLayout llStateLoading, llStateEmpty, llStateError;
    private Button btnEmptyRefresh, btnErrorRetry;
    private RecyclerView rvExams;

    private ExamAdapter adapter;
    private UserSession userSession;
    private QuestionRepository questionRepository;

    private List<ExamItem> fullExamList = new ArrayList<>();
    private String selectedCategory = "Tất cả";
    private int currentState = STATE_SUCCESS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userSession = new UserSession(requireContext());
        questionRepository = new QuestionRepository(requireContext());

        initViews(view);
        setupData();
        setupListeners();

        // Khôi phục trạng thái đã lưu
        setUiState(userSession.getDemoState());
    }

    private void initViews(View view) {
        tvUserNameHeader = view.findViewById(R.id.tvUserNameHeader);
        btnProfileAvatar = view.findViewById(R.id.btnProfileAvatar);
        btnSearchExam = view.findViewById(R.id.btnSearchExam);
        btnRandomExam = view.findViewById(R.id.btnRandomExam);
        llOfflineBanner = view.findViewById(R.id.llOfflineBanner);
        cardResumeExam = view.findViewById(R.id.cardResumeExam);
        tvResumeTitle = view.findViewById(R.id.tvResumeTitle);
        tvResumeProgress = view.findViewById(R.id.tvResumeProgress);
        pbResumeProgress = view.findViewById(R.id.pbResumeProgress);
        btnResumeAction = view.findViewById(R.id.btnResumeAction);

        chipAll = view.findViewById(R.id.chipAll);
        chipCsdl = view.findViewById(R.id.chipCsdl);
        chipMang = view.findViewById(R.id.chipMang);
        chipHdh = view.findViewById(R.id.chipHdh);

        llStateLoading = view.findViewById(R.id.llStateLoading);
        llStateEmpty = view.findViewById(R.id.llStateEmpty);
        llStateError = view.findViewById(R.id.llStateError);
        btnEmptyRefresh = view.findViewById(R.id.btnEmptyRefresh);
        btnErrorRetry = view.findViewById(R.id.btnErrorRetry);

        rvExams = view.findViewById(R.id.rvExams);
        rvExams.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExamAdapter(this);
        rvExams.setAdapter(adapter);

        tvUserNameHeader.setText(userSession.getUserName());
    }

    private void setupData() {
        // Danh sách đề thi chuẩn theo Wireframe SCR-02
        fullExamList.clear();
        fullExamList.add(new ExamItem(1, "Đề giữa kỳ – Cơ sở dữ liệu", "Cơ sở dữ liệu", 40, 45, true, 100, 2,
                "Đề thi trắc nghiệm giữa kỳ môn Cơ sở dữ liệu bao gồm kiến thức mô hình quan hệ, khóa chính/ngoại, đại số quan hệ và chuẩn hóa CSDL."));
        fullExamList.add(new ExamItem(2, "Đề cuối kỳ – Mạng máy tính", "Mạng máy tính", 50, 60, false, 0, 0,
                "Đề thi cuối kỳ môn Mạng máy tính với các câu hỏi về mô hình OSI, TCP/IP, địa chỉ IPv4/IPv6, định tuyến và các giao thức tầng ứng dụng."));
        fullExamList.add(new ExamItem(3, "Đề giữa kỳ – Hệ điều hành", "Hệ điều hành", 30, 40, true, 100, 1,
                "Đề kiểm tra trọng tâm về tiến trình, luồng, lập lịch CPU, bế tắc (Deadlock) và quản lý bộ nhớ ảo."));
        fullExamList.add(new ExamItem(4, "Đề ôn tập – Mạng máy tính", "Mạng máy tính", 40, 45, false, 0, 0,
                "Bộ câu hỏi trắc nghiệm rèn luyện kỹ năng phân tích gói tin, subnet mask và cấu hình mạng căn bản."));

        // Setup resume card
        tvResumeTitle.setText(userSession.getResumeTitle());
        tvResumeProgress.setText(String.format(Locale.getDefault(), "Câu %d/%d · còn 32:15",
                userSession.getResumeCurrentQuestion(), userSession.getResumeTotalQuestions()));
        pbResumeProgress.setMax(userSession.getResumeTotalQuestions());
        pbResumeProgress.setProgress(userSession.getResumeCurrentQuestion());
    }

    private void setupListeners() {
        // Nút avatar: Cho phép đổi trạng thái UI demo (Sơ đồ 4) & Đăng xuất
        btnProfileAvatar.setOnClickListener(v -> showDemoStateDialog());

        // Nút tìm kiếm / chọn nhanh trạng thái
        btnSearchExam.setOnClickListener(v -> showDemoStateDialog());

        // Nút tạo đề thi ngẫu nhiên từ ngân hàng câu hỏi
        if (btnRandomExam != null) {
            btnRandomExam.setOnClickListener(v -> showRandomExamDialog());
        }

        // Tiếp tục làm bài
        btnResumeAction.setOnClickListener(v -> {
            startExamFromCategory(userSession.getResumeCategory(), userSession.getResumeTitle());
        });

        // Filter chips
        chipAll.setOnClickListener(v -> selectFilter("Tất cả", chipAll));
        chipCsdl.setOnClickListener(v -> selectFilter("Cơ sở dữ liệu", chipCsdl));
        chipMang.setOnClickListener(v -> selectFilter("Mạng máy tính", chipMang));
        chipHdh.setOnClickListener(v -> selectFilter("Hệ điều hành", chipHdh));

        // State retry / refresh
        btnEmptyRefresh.setOnClickListener(v -> {
            selectFilter("Tất cả", chipAll);
            setUiState(STATE_SUCCESS);
        });

        btnErrorRetry.setOnClickListener(v -> {
            setUiState(STATE_LOADING);
            new Handler(Looper.getMainLooper()).postDelayed(() -> setUiState(STATE_SUCCESS), 1000);
        });
    }

    private void selectFilter(String category, TextView selectedChip) {
        this.selectedCategory = category;

        // Reset chip styles
        TextView[] chips = new TextView[]{chipAll, chipCsdl, chipMang, chipHdh};
        for (TextView c : chips) {
            c.setBackgroundResource(R.drawable.bg_chip_unselected);
            c.setTextColor(getResources().getColor(R.color.text_secondary, null));
        }

        selectedChip.setBackgroundResource(R.drawable.bg_chip_selected);
        selectedChip.setTextColor(0xFFFFFFFF);

        filterExams();
    }

    private void filterExams() {
        if (currentState == STATE_LOADING || currentState == STATE_ERROR) {
            return;
        }

        List<ExamItem> filtered = new ArrayList<>();
        for (ExamItem item : fullExamList) {
            if ("Tất cả".equalsIgnoreCase(selectedCategory) || item.getCategory().equalsIgnoreCase(selectedCategory)) {
                filtered.add(item);
            }
        }

        if (filtered.isEmpty()) {
            setUiState(STATE_EMPTY);
        } else {
            if (currentState != STATE_OFFLINE) {
                setUiState(STATE_SUCCESS);
            }
            adapter.setExamList(filtered);
        }
    }

    /**
     * Chuyển đổi giữa 5 trạng thái UI của SCR-02 theo Sơ đồ 4
     */
    public void setUiState(int state) {
        this.currentState = state;
        userSession.setDemoState(state);

        // Mặc định ẩn tất cả
        llStateLoading.setVisibility(View.GONE);
        llStateEmpty.setVisibility(View.GONE);
        llStateError.setVisibility(View.GONE);
        llOfflineBanner.setVisibility(View.GONE);
        rvExams.setVisibility(View.GONE);
        cardResumeExam.setVisibility(View.GONE);

        switch (state) {
            case STATE_LOADING:
                llStateLoading.setVisibility(View.VISIBLE);
                break;

            case STATE_SUCCESS:
                cardResumeExam.setVisibility(View.VISIBLE);
                rvExams.setVisibility(View.VISIBLE);
                filterExamsInternal();
                break;

            case STATE_EMPTY:
                llStateEmpty.setVisibility(View.VISIBLE);
                break;

            case STATE_ERROR:
                llStateError.setVisibility(View.VISIBLE);
                break;

            case STATE_OFFLINE:
                llOfflineBanner.setVisibility(View.VISIBLE);
                cardResumeExam.setVisibility(View.VISIBLE);
                rvExams.setVisibility(View.VISIBLE);
                filterExamsInternal();
                break;
        }
    }

    private void filterExamsInternal() {
        List<ExamItem> filtered = new ArrayList<>();
        for (ExamItem item : fullExamList) {
            if ("Tất cả".equalsIgnoreCase(selectedCategory) || item.getCategory().equalsIgnoreCase(selectedCategory)) {
                filtered.add(item);
            }
        }
        adapter.setExamList(filtered);
    }

    private void showRandomExamDialog() {
        android.app.Dialog dialog = new android.app.Dialog(requireContext());
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_random_exam_setup);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        android.widget.Spinner spnSubject = dialog.findViewById(R.id.spnRandomSubject);
        android.widget.RadioGroup rgCount = dialog.findViewById(R.id.rgQuestionCount);
        TextView tvHint = dialog.findViewById(R.id.tvDurationHint);
        Button btnStart = dialog.findViewById(R.id.btnStartRandomExam);
        View btnClose = dialog.findViewById(R.id.btnCloseDialog);

        String[] subjectOptions = new String[]{"Tất cả các môn", "Cơ sở dữ liệu", "Mạng máy tính", "Hệ điều hành"};
        android.widget.ArrayAdapter<String> spinnerAdapter = new android.widget.ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                subjectOptions
        );
        spnSubject.setAdapter(spinnerAdapter);

        rgCount.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbCount10) {
                tvHint.setText("⏱️ Thời gian làm bài: 15 phút (Lấy ngẫu nhiên 10 câu)");
            } else if (checkedId == R.id.rbCount20) {
                tvHint.setText("⏱️ Thời gian làm bài: 25 phút (Lấy ngẫu nhiên 20 câu)");
            } else {
                tvHint.setText("⏱️ Thời gian làm bài: 45 phút (Lấy ngẫu nhiên 40 câu)");
            }
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnStart.setOnClickListener(v -> {
            int selectedPosition = spnSubject.getSelectedItemPosition();
            String chosenCategory = selectedPosition == 0 ? null : subjectOptions[selectedPosition];

            int count = 20;
            int checkedId = rgCount.getCheckedRadioButtonId();
            if (checkedId == R.id.rbCount10) count = 10;
            else if (checkedId == R.id.rbCount40) count = 40;

            dialog.dismiss();
            startRandomExam(chosenCategory, count);
        });

        dialog.show();
    }

    private void startRandomExam(String category, int count) {
        long[] ids = questionRepository.pickRandomIds(category, count);
        if (ids.length == 0) {
            ids = questionRepository.pickRandomIds(null, count);
        }

        if (ids.length == 0) {
            Toast.makeText(requireContext(), "Ngân hàng chưa có câu hỏi nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = (category == null || category.isEmpty())
                ? "Đề ngẫu nhiên tổng hợp (" + ids.length + " câu)"
                : "Đề ngẫu nhiên – " + category + " (" + ids.length + " câu)";

        Intent intent = new Intent(requireContext(), ExamActivity.class);
        intent.putExtra(Constants.EXTRA_QUESTION_IDS, ids);
        intent.putExtra(Constants.EXTRA_CATEGORY, category != null ? category : "Tổng hợp");
        intent.putExtra("exam_title", title);
        startActivity(intent);
    }

    private void showDemoStateDialog() {
        String[] options = new String[]{
                "1. Loading (Shimmer chờ tải)",
                "2. Success (Danh sách đề đầy đủ)",
                "3. Empty (Chưa có đề)",
                "4. Error + Retry (Lỗi kết nối)",
                "5. Offline (Dùng cache)",
                "🚪 Đăng xuất tài khoản"
        };

        new AlertDialog.Builder(requireContext())
                .setTitle("Chọn trạng thái UI (Sơ đồ 4)")
                .setItems(options, (dialog, which) -> {
                    if (which == 5) {
                        userSession.logout();
                        startActivity(new Intent(requireContext(), LoginActivity.class));
                        requireActivity().finish();
                    } else {
                        setUiState(which);
                        Toast.makeText(requireContext(), "Đã chuyển sang: " + options[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    @Override
    public void onExamSelected(ExamItem item) {
        Intent intent = new Intent(requireContext(), ExamDetailActivity.class);
        intent.putExtra("exam_item", item);
        startActivity(intent);
    }

    @Override
    public void onExamActionClicked(ExamItem item) {
        if (item.isDownloaded()) {
            startExamFromCategory(item.getCategory(), item.getTitle());
        } else {
            // Mở chi tiết để tải
            onExamSelected(item);
        }
    }

    private void startExamFromCategory(String category, String title) {
        long[] ids = questionRepository.pickRandomIds(category, 40);
        if (ids.length == 0) {
            ids = questionRepository.pickRandomIds(null, 40);
        }

        Intent intent = new Intent(requireContext(), ExamActivity.class);
        intent.putExtra(Constants.EXTRA_QUESTION_IDS, ids);
        intent.putExtra(Constants.EXTRA_CATEGORY, category);
        intent.putExtra("exam_title", title);
        startActivity(intent);
    }
}

