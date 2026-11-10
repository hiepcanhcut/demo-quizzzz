# PTIT PrepQuiz — Ứng dụng Luyện thi Trắc nghiệm (Offline-first)

Dự án Android luyện thi trắc nghiệm theo môn học dành cho sinh viên PTIT, xây dựng trên nền tảng **Java 11**, kiến trúc **Android MVC chuẩn (Activity – Adapter – SQLiteOpenHelper)**, hoạt động ngoại tuyến 100% với CSDL **SQLite**.

---

## 1. Cấu trúc thư mục dự án (Project Structure)

Được tổ chức chuẩn mực theo mô hình phân tầng (package-by-layer):

```text
app/
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/example/quizapp/
│   │   ├── ui/                         ← Tầng giao diện (Presentation)
│   │   │   ├── MainActivity.java       ← Menu chính điều hướng
│   │   │   ├── question/               ← Module 2 (Lê Đức Mạnh)
│   │   │   │   ├── QuestionListActivity.java
│   │   │   │   ├── QuestionEditActivity.java
│   │   │   │   └── QuestionAdapter.java
│   │   │   ├── exam/                   ← Module 3 (Đinh Đức Nghĩa)
│   │   │   │   ├── ExamSetupActivity.java
│   │   │   │   ├── ExamActivity.java
│   │   │   │   ├── QuestionGridDialog.java
│   │   │   │   ├── ResultActivity.java
│   │   │   │   └── ResultDetailAdapter.java
│   │   │   └── history/                ← Module 1 (Lê Đăng Hiệp)
│   │   │       ├── HistoryActivity.java
│   │   │       └── HistoryAdapter.java
│   │   ├── model/                      ← Tầng thực thể (Domain Model)
│   │   │   ├── Question.java           (Lê Đức Mạnh)
│   │   │   ├── ExamHistory.java        (Lê Đăng Hiệp)
│   │   │   ├── ExamSession.java        (Đinh Đức Nghĩa)
│   │   │   └── AnswerResult.java       (Đinh Đức Nghĩa)
│   │   ├── logic/                      ← Nghiệp vụ xử lý (Domain Logic)
│   │   │   ├── QuestionValidator.java  (Lê Đức Mạnh)
│   │   │   ├── ExamEngine.java         (Đinh Đức Nghĩa)
│   │   │   └── ScoreGrader.java        (Đinh Đức Nghĩa)
│   │   ├── data/                       ← Tầng dữ liệu SQLite (Data Layer)
│   │   │   ├── DbContract.java         (Lê Đức Mạnh)
│   │   │   ├── QuizDatabaseHelper.java (Lê Đức Mạnh)
│   │   │   ├── SampleDataProvider.java (Lê Đức Mạnh)
│   │   │   ├── QuestionRepository.java (Lê Đức Mạnh)
│   │   │   └── HistoryRepository.java  (Lê Đăng Hiệp)
│   │   └── util/                       ← Tiện ích dùng chung
│   │       ├── AppExecutors.java       (Luồng nền DiskIO + Handler Main Thread)
│   │       ├── TimeFormatter.java      (Định dạng thời gian MM:SS & ngày tháng)
│   │       └── Constants.java          (Hằng số Intent & cài đặt)
│   └── res/
│       ├── layout/                     ← Các file XML giao diện
│       ├── values/                     ← strings.xml, colors.xml, themes.xml
│       ├── drawable/                   ← Nền đáp án mặc định/chọn/đúng/sai
│       └── menu/
├── settings.gradle
├── build.gradle (root)
└── app/build.gradle                    (Java 11, Material, RecyclerView, CardView)
```

---

## 2. Phân chia công việc cho 3 thành viên (Vertical Slices)

Dự án đã được dựng sẵn toàn bộ **khung sườn (boilerplate, signatures, models, repositories, UI layouts và Activity navigation)** để 3 bạn làm việc độc lập mà không bị xung đột code:

### 👤 Thành viên 1: Lê Đăng Hiệp
* **Module sở hữu:** **Module 1: Quản lý Người dùng & Thống kê học tập (User & Analytics)**
* **Phạm vi code phụ trách:**
  - `model/ExamHistory.java`: Thực thể lưu trữ kết quả bài thi.
  - `data/HistoryRepository.java`: Thực hiện lưu bài thi, đọc danh sách lịch sử từ bảng `exam_history` trong SQLite.
  - `ui/history/HistoryActivity.java`: Màn hình hiển thị danh sách bài thi đã làm.
  - `ui/history/HistoryAdapter.java`: RecyclerView Adapter hiển thị các thẻ lịch sử (điểm %, số câu đúng, ngày làm, thời gian).
  - Mở rộng thêm: Tính năng biểu đồ tiến độ, xếp hạng, hồ sơ cá nhân hoặc quản lý tài khoản người dùng nếu cần.
* **Tài liệu phụ trách:** [`docs/testing-guide.md`](docs/testing-guide.md).

---

### 👤 Thành viên 2: Lê Đức Mạnh
* **Module sở hữu:** **Module 2: Quản lý Ngân hàng câu hỏi & Đề thi (Content & Admin CMS)**
* **Phạm vi code phụ trách:**
  - `model/Question.java`: Thực thể câu hỏi 4 đáp án chọn 1.
  - `logic/QuestionValidator.java`: Thuật toán kiểm tra hợp lệ dữ liệu câu hỏi nhập vào form.
  - `data/DbContract.java`: Định nghĩa lược đồ bảng SQLite (`questions`, `exam_history`), indexes.
  - `data/QuizDatabaseHelper.java`: Kế thừa `SQLiteOpenHelper`, xử lý tạo bảng và migration DB.
  - `data/SampleDataProvider.java`: Bộ câu hỏi mẫu mặc định (Toán, Lịch sử, Khoa học, Công nghệ).
  - `data/QuestionRepository.java`: Nghiệp vụ CRUD câu hỏi, tìm kiếm, lọc theo môn, khôi phục câu hỏi mẫu.
  - `ui/question/QuestionListActivity.java`: Màn hình danh sách câu hỏi, tìm kiếm và lọc.
  - `ui/question/QuestionEditActivity.java`: Màn hình thêm mới / chỉnh sửa câu hỏi.
  - `ui/question/QuestionAdapter.java`: RecyclerView Adapter câu hỏi.
* **Tài liệu phụ trách:** [`docs/architecture.md`](docs/architecture.md).

---

### 👤 Thành viên 3: Đinh Đức Nghĩa
* **Module sở hữu:** **Module 3: Trải nghiệm làm bài thi (Exam Engine)**
* **Phạm vi code phụ trách:**
  - `model/ExamSession.java`: Quản lý trạng thái phiên thi (đáp án đã chọn, cờ, câu hiện tại, thời gian).
  - `model/AnswerResult.java`: Thực thể lưu chi tiết từng câu sau khi chấm điểm.
  - `logic/ExamEngine.java`: Thuật toán chấm điểm bài thi.
  - `logic/ScoreGrader.java`: Xếp loại kết quả (Xuất sắc, Khá tốt, Đạt yêu cầu, Cần cố gắng).
  - `ui/exam/ExamSetupActivity.java`: Màn hình cấu hình bài thi (chọn môn, chọn số câu).
  - `ui/exam/ExamActivity.java`: Màn hình làm bài thi (Core Engine: đếm ngược, chuyển câu, gắn cờ, nộp bài, chống mất trạng thái khi xoay màn hình).
  - `ui/exam/QuestionGridDialog.java`: Lưới câu hỏi xem nhanh, chuyển câu tức thì.
  - `ui/exam/ResultActivity.java`: Màn hình tổng quan kết quả bài thi.
  - `ui/exam/ResultDetailAdapter.java`: Xem lại chi tiết đáp án đúng/sai kèm lời giải.
* **Tài liệu phụ trách:** [`docs/ui-design.md`](docs/ui-design.md).

---

## 3. Hướng dẫn mở và chạy dự án

1. Mở **Android Studio**.
2. Chọn **File -> Open...** và điều hướng đến thư mục `app trac nghiem`.
3. Đợi Android Studio đồng bộ Gradle (**Sync Project with Gradle Files**).
4. Chọn thiết bị giả lập (Emulator) hoặc máy thật Android (Android 7.0 / API 24 trở lên) và nhấn nút **Run 'app'** (`Shift + F10`).
5. Chạy thử nghiệm các Unit Test: Chuột phải vào thư mục `app/src/test/java/` và chọn **Run 'Tests in 'com.example.quizapp''**.

# Testing Guide & Test Evidence — PTIT PrepQuiz (QuizApp)

## Part 1 — How to Test

### 1.1 Automated Tests
- **Unit Tests**: Test core domain logic independently from the Android framework (e.g., scoring algorithm in `ExamEngine`, grade classification in `ScoreGrader`, time formatting in `TimeFormatter`, and validation rules in `QuestionValidator`).
- **Tools used**: JUnit 4 (`org.junit.Test`, `assertEquals`, `assertTrue`, `assertFalse`).
- **Execution**: Run via Gradle task `:app:testDebugUnitTest`.

### 1.2 Manual Scenarios
- **Offline**: Test app behavior with Airplane Mode enabled. Verify SQLite data reading/writing works offline.
- **Slow Network / Server Error**: As an offline-first app using local SQLite DB, verify asynchronous background thread execution via `AppExecutors.diskIO()` to prevent UI freezing (ANR).
- **Empty Data**: Test empty states for empty question bank and empty exam history.
- **Rotation & Process Death**: Rotate device during an active exam; toggle "Don't keep activities" in Android Developer Options to verify state preservation in `ExamSession`.
- **Screen Sizes & Dark Mode**: Verify responsiveness across small screens, tablets, and system theme changes.

---

## Part 2 — Our Test Evidence

### 2.1 Automated Tests Summary

**Command to run**:
```bash
./gradlew :app:testDebugUnitTest
```

**Results**:
- **Total Tests**: 6
- **Passed**: 6
- **Failed**: 0
- **Skipped**: 0

#### Test Suite Details:

| Test Class | Method | Focus | Result |
|---|---|---|---|
| `ExamEngineTest` | `testExamGrading()` | Correct score %, correct count, and partial/unanswered questions | **PASS** |
| `ExamEngineTest` | `testScoreGrader()` | Score classification (Xuất sắc, Khá tốt, Đạt yêu cầu, Cần cố gắng) | **PASS** |
| `ExamEngineTest` | `testTimeFormatter()` | Timer string formatting (`00:00`, `01:05`, `15:00`) | **PASS** |
| `QuestionValidatorTest` | `testValidQuestion()` | Valid 4-option question input | **PASS** |
| `QuestionValidatorTest` | `testInvalidEmptyContent()` | Rejection of empty question text | **PASS** |
| `QuestionValidatorTest` | `testInvalidCorrectAnswerIndex()` | Rejection of out-of-bounds answer index (>3) | **PASS** |

---

### 2.2 Manual Scenarios Checklist

| # | Scenario | Screen(s) | Result / Verification Note | Pass? |
|---|---|---|---|---|
| 1 | **Offline** | All screens (`MainActivity`, `ExamActivity`, `QuestionListActivity`, `HistoryActivity`) | 100% functional without internet connection. All CRUD operations run against local SQLite database (`QuizDatabaseHelper`). | **PASS** |
| 2 | **Slow Network / Timeout** | `ExamActivity`, `QuestionEditActivity` | N/A (Offline-first architecture). Disk I/O operations are offloaded to background threads using `AppExecutors.diskIO()` to prevent ANR. | **PASS** |
| 3 | **Server Error / Down** | Startup / DB Init | Default dataset is populated locally via `SampleDataProvider` on first run without external API calls. | **PASS** |
| 4 | **Empty Data** | `QuestionListActivity`, `HistoryActivity`, `ExamSetupActivity` | Clear empty state notifications displayed. `QuestionListActivity` provides "Khôi phục câu hỏi mẫu" (restore sample data) option. `HistoryActivity` shows "Chưa có lịch sử làm bài thi". | **PASS** |
| 5 | **Rotation & Process Death** | `ExamActivity` (Active Exam Session) | State restored perfectly via `onSaveInstanceState(Bundle)` saving `ExamSession` object. Timer, selected answers, flagged questions, and question index persist across orientation changes. | **PASS** |
| 6 | **Small Screen / Tablet / Dark Mode** | `ExamActivity`, `ResultActivity`, `QuestionGridDialog` | Responsive UI built with `ConstraintLayout` & `CardView`. Text, options, and quick navigation grid adapt smoothly. | **PASS** |

---

### 2.3 Notes on Bugs Found & Fixed

- **Bug**: Compilation failure in `ExamActivity.java` (and all UI activities calling `AppExecutors.getInstance().mainThread().execute(...)`).
  - **Error Message**: `error: cannot find symbol method execute(()->{...}) location: class Handler`
  - **Root Cause**: `AppExecutors.java` returned `android.os.Handler` for `mainThread()`, which lacks the `execute(Runnable)` method defined in `java.util.concurrent.Executor`.
  - **Fix Applied**: Refactored `AppExecutors.java` to return `Executor` using a `MainThreadExecutor` wrapper around `Handler.post(...)`.
  - **Verification**: Recompiled project with `./gradlew :app:assembleDebug` and executed unit tests with `./gradlew :app:testDebugUnitTest`. All 6 unit tests passed without errors.

