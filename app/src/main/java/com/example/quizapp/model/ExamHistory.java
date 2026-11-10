package com.example.quizapp.model;

import java.io.Serializable;

/**
 * Domain Model: Bản ghi lịch sử của một lần làm bài thi trắc nghiệm (SCR-05 & SCR-06).
 */
public class ExamHistory implements Serializable {

    public static final int SYNC_PENDING = 0; // Chờ đồng bộ
    public static final int SYNC_SUCCESS = 1; // Đã đồng bộ
    public static final int SYNC_ERROR = 2;   // Lỗi đồng bộ

    private long id;
    private String examTitle;           // Tên đề thi (VD: Giữa kỳ - Cơ sở dữ liệu)
    private String category;            // Môn thi đã làm
    private int totalQuestions;         // Tổng số câu trong đề thi
    private int correctCount;           // Số câu trả lời đúng
    private double scorePercent;        // Tỷ lệ phần trăm đúng (0 - 100%)
    private int durationSeconds;        // Thời gian hoàn thành (giây)
    private long takenAt;               // Timestamp thời điểm nộp bài
    private int syncStatus = SYNC_PENDING; // Trạng thái đồng bộ

    public ExamHistory() {
    }

    public ExamHistory(long id, String category, int totalQuestions,
                       int correctCount, double scorePercent,
                       int durationSeconds, long takenAt) {
        this.id = id;
        this.examTitle = "Bài thi " + category;
        this.category = category;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.scorePercent = scorePercent;
        this.durationSeconds = durationSeconds;
        this.takenAt = takenAt;
        this.syncStatus = SYNC_SUCCESS;
    }

    public ExamHistory(long id, String examTitle, String category, int totalQuestions,
                       int correctCount, double scorePercent,
                       int durationSeconds, long takenAt, int syncStatus) {
        this.id = id;
        this.examTitle = examTitle;
        this.category = category;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.scorePercent = scorePercent;
        this.durationSeconds = durationSeconds;
        this.takenAt = takenAt;
        this.syncStatus = syncStatus;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getExamTitle() {
        if (examTitle == null || examTitle.isEmpty()) {
            return "Đề thi – " + (category != null ? category : "Tổng hợp");
        }
        return examTitle;
    }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }

    public double getScorePercent() { return scorePercent; }
    public void setScorePercent(double scorePercent) { this.scorePercent = scorePercent; }

    public double getScore10() {
        if (totalQuestions <= 0) return 0.0;
        return (correctCount * 10.0) / totalQuestions;
    }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public long getTakenAt() { return takenAt; }
    public void setTakenAt(long takenAt) { this.takenAt = takenAt; }

    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }

    public String getSyncStatusText() {
        switch (syncStatus) {
            case SYNC_SUCCESS: return "Đã đồng bộ";
            case SYNC_ERROR: return "Lỗi đồng bộ";
            case SYNC_PENDING:
            default: return "Chờ đồng bộ";
        }
    }
}
