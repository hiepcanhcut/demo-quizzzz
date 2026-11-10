package com.example.quizapp.model;

import java.io.Serializable;

/**
 * Domain Model: Đại diện cho một bộ đề thi mẫu (SCR-02 & SCR-03).
 */
public class ExamItem implements Serializable {

    private long id;
    private String title;
    private String category;
    private int questionCount;
    private int durationMinutes;
    private boolean isDownloaded;
    private int downloadPercent;
    private int attemptCount;
    private String description;

    public ExamItem() {}

    public ExamItem(long id, String title, String category, int questionCount,
                    int durationMinutes, boolean isDownloaded, int downloadPercent,
                    int attemptCount, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.questionCount = questionCount;
        this.durationMinutes = durationMinutes;
        this.isDownloaded = isDownloaded;
        this.downloadPercent = downloadPercent;
        this.attemptCount = attemptCount;
        this.description = description;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isDownloaded() { return isDownloaded; }
    public void setDownloaded(boolean downloaded) { isDownloaded = downloaded; }

    public int getDownloadPercent() { return downloadPercent; }
    public void setDownloadPercent(int downloadPercent) { this.downloadPercent = downloadPercent; }

    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

