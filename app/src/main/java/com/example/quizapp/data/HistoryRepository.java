package com.example.quizapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.model.ExamHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository phụ trách lưu và đọc lịch sử làm bài thi từ bảng exam_history trong SQLite.
 */
public class HistoryRepository {

    private final QuizDatabaseHelper dbHelper;

    public HistoryRepository(Context context) {
        this.dbHelper = QuizDatabaseHelper.getInstance(context);
    }

    /**
     * Lưu kết quả một lần thi vào CSDL.
     */
    public long insert(ExamHistory history) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.HistoryEntry.COLUMN_EXAM_TITLE, history.getExamTitle());
        values.put(DbContract.HistoryEntry.COLUMN_CATEGORY, history.getCategory());
        values.put(DbContract.HistoryEntry.COLUMN_TOTAL_QUESTIONS, history.getTotalQuestions());
        values.put(DbContract.HistoryEntry.COLUMN_CORRECT_COUNT, history.getCorrectCount());
        values.put(DbContract.HistoryEntry.COLUMN_SCORE_PERCENT, history.getScorePercent());
        values.put(DbContract.HistoryEntry.COLUMN_DURATION_SECONDS, history.getDurationSeconds());
        values.put(DbContract.HistoryEntry.COLUMN_TAKEN_AT, history.getTakenAt());
        values.put(DbContract.HistoryEntry.COLUMN_SYNC_STATUS, history.getSyncStatus());

        return db.insert(DbContract.HistoryEntry.TABLE_NAME, null, values);
    }

    /**
     * Lấy toàn bộ lịch sử các lần thi, sắp xếp mới nhất lên đầu.
     */
    public List<ExamHistory> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<ExamHistory> list = new ArrayList<>();
        Cursor cursor = db.query(DbContract.HistoryEntry.TABLE_NAME,
                null, null, null, null, null,
                DbContract.HistoryEntry.COLUMN_TAKEN_AT + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToHistory(cursor));
            }
            cursor.close();
        }
        return list;
    }

    /**
     * Lấy một bản ghi lịch sử theo ID.
     */
    public ExamHistory getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.HistoryEntry.TABLE_NAME,
                null,
                DbContract.HistoryEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        ExamHistory history = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                history = cursorToHistory(cursor);
            }
            cursor.close();
        }
        return history;
    }

    private ExamHistory cursorToHistory(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry._ID));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry.COLUMN_CATEGORY));
        int totalQuestions = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry.COLUMN_TOTAL_QUESTIONS));
        int correctCount = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry.COLUMN_CORRECT_COUNT));
        double scorePercent = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry.COLUMN_SCORE_PERCENT));
        int durationSeconds = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry.COLUMN_DURATION_SECONDS));
        long takenAt = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.HistoryEntry.COLUMN_TAKEN_AT));

        String examTitle = "Đề thi – " + category;
        int titleColIdx = cursor.getColumnIndex(DbContract.HistoryEntry.COLUMN_EXAM_TITLE);
        if (titleColIdx != -1 && !cursor.isNull(titleColIdx)) {
            examTitle = cursor.getString(titleColIdx);
        }

        int syncStatus = ExamHistory.SYNC_SUCCESS;
        int syncColIdx = cursor.getColumnIndex(DbContract.HistoryEntry.COLUMN_SYNC_STATUS);
        if (syncColIdx != -1 && !cursor.isNull(syncColIdx)) {
            syncStatus = cursor.getInt(syncColIdx);
        }

        return new ExamHistory(id, examTitle, category, totalQuestions, correctCount, scorePercent, durationSeconds, takenAt, syncStatus);
    }
}
