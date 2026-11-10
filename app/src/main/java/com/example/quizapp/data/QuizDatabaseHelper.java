package com.example.quizapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Quản lý khởi tạo và nâng cấp CSDL SQLite của ứng dụng.
 */
public class QuizDatabaseHelper extends SQLiteOpenHelper {

    private static volatile QuizDatabaseHelper sInstance;

    public static synchronized QuizDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new QuizDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private QuizDatabaseHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng questions và index
        db.execSQL(DbContract.QuestionEntry.SQL_CREATE_TABLE);
        db.execSQL(DbContract.QuestionEntry.SQL_CREATE_INDEX_CATEGORY);

        // Tạo bảng exam_history và index
        db.execSQL(DbContract.HistoryEntry.SQL_CREATE_TABLE);
        db.execSQL(DbContract.HistoryEntry.SQL_CREATE_INDEX_TAKEN_AT);

        // Tự động nạp câu hỏi mẫu lần đầu
        SampleDataProvider.seed(db);

        // Nạp sẵn vài lịch sử mẫu để giao diện Lịch sử hiển thị phong phú như wireframe
        seedSampleHistory(db);
    }

    private void seedSampleHistory(SQLiteDatabase db) {
        try {
            long now = System.currentTimeMillis();
            long day = 24 * 3600 * 1000L;

            // 1. Giữa kỳ - Cơ sở dữ liệu: 8.5/10, Chờ đồng bộ
            db.execSQL("INSERT INTO " + DbContract.HistoryEntry.TABLE_NAME + " (" +
                    DbContract.HistoryEntry.COLUMN_EXAM_TITLE + ", " +
                    DbContract.HistoryEntry.COLUMN_CATEGORY + ", " +
                    DbContract.HistoryEntry.COLUMN_TOTAL_QUESTIONS + ", " +
                    DbContract.HistoryEntry.COLUMN_CORRECT_COUNT + ", " +
                    DbContract.HistoryEntry.COLUMN_SCORE_PERCENT + ", " +
                    DbContract.HistoryEntry.COLUMN_DURATION_SECONDS + ", " +
                    DbContract.HistoryEntry.COLUMN_TAKEN_AT + ", " +
                    DbContract.HistoryEntry.COLUMN_SYNC_STATUS +
                    ") VALUES ('Giữa kỳ – Cơ sở dữ liệu', 'Cơ sở dữ liệu', 40, 34, 85.0, 1720, " + (now - day) + ", 0)");

            // 2. Cuối kỳ - Mạng máy tính: 7.0/10, Đã đồng bộ
            db.execSQL("INSERT INTO " + DbContract.HistoryEntry.TABLE_NAME + " (" +
                    DbContract.HistoryEntry.COLUMN_EXAM_TITLE + ", " +
                    DbContract.HistoryEntry.COLUMN_CATEGORY + ", " +
                    DbContract.HistoryEntry.COLUMN_TOTAL_QUESTIONS + ", " +
                    DbContract.HistoryEntry.COLUMN_CORRECT_COUNT + ", " +
                    DbContract.HistoryEntry.COLUMN_SCORE_PERCENT + ", " +
                    DbContract.HistoryEntry.COLUMN_DURATION_SECONDS + ", " +
                    DbContract.HistoryEntry.COLUMN_TAKEN_AT + ", " +
                    DbContract.HistoryEntry.COLUMN_SYNC_STATUS +
                    ") VALUES ('Cuối kỳ – Mạng máy tính', 'Mạng máy tính', 50, 35, 70.0, 2465, " + (now - 2 * day) + ", 1)");

            // 3. Giữa kỳ - Hệ điều hành: 6.5/10, Đã đồng bộ
            db.execSQL("INSERT INTO " + DbContract.HistoryEntry.TABLE_NAME + " (" +
                    DbContract.HistoryEntry.COLUMN_EXAM_TITLE + ", " +
                    DbContract.HistoryEntry.COLUMN_CATEGORY + ", " +
                    DbContract.HistoryEntry.COLUMN_TOTAL_QUESTIONS + ", " +
                    DbContract.HistoryEntry.COLUMN_CORRECT_COUNT + ", " +
                    DbContract.HistoryEntry.COLUMN_SCORE_PERCENT + ", " +
                    DbContract.HistoryEntry.COLUMN_DURATION_SECONDS + ", " +
                    DbContract.HistoryEntry.COLUMN_TAKEN_AT + ", " +
                    DbContract.HistoryEntry.COLUMN_SYNC_STATUS +
                    ") VALUES ('Giữa kỳ – Hệ điều hành', 'Hệ điều hành', 30, 20, 66.7, 1992, " + (now - 4 * day) + ", 1)");

            // 4. Giữa kỳ - Cơ sở dữ liệu: 7.8/10, Lỗi đồng bộ
            db.execSQL("INSERT INTO " + DbContract.HistoryEntry.TABLE_NAME + " (" +
                    DbContract.HistoryEntry.COLUMN_EXAM_TITLE + ", " +
                    DbContract.HistoryEntry.COLUMN_CATEGORY + ", " +
                    DbContract.HistoryEntry.COLUMN_TOTAL_QUESTIONS + ", " +
                    DbContract.HistoryEntry.COLUMN_CORRECT_COUNT + ", " +
                    DbContract.HistoryEntry.COLUMN_SCORE_PERCENT + ", " +
                    DbContract.HistoryEntry.COLUMN_DURATION_SECONDS + ", " +
                    DbContract.HistoryEntry.COLUMN_TAKEN_AT + ", " +
                    DbContract.HistoryEntry.COLUMN_SYNC_STATUS +
                    ") VALUES ('Giữa kỳ – Cơ sở dữ liệu', 'Cơ sở dữ liệu', 40, 31, 77.5, 1847, " + (now - 6 * day) + ", 2)");
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE " + DbContract.HistoryEntry.TABLE_NAME +
                        " ADD COLUMN " + DbContract.HistoryEntry.COLUMN_EXAM_TITLE + " TEXT;");
                db.execSQL("ALTER TABLE " + DbContract.HistoryEntry.TABLE_NAME +
                        " ADD COLUMN " + DbContract.HistoryEntry.COLUMN_SYNC_STATUS + " INTEGER DEFAULT 0;");
            } catch (Exception ignored) {
            }
        }
    }

    public void restoreSampleQuestions() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DbContract.QuestionEntry.TABLE_NAME,
                    DbContract.QuestionEntry.COLUMN_IS_SAMPLE + " = 1", null);
            SampleDataProvider.seed(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
