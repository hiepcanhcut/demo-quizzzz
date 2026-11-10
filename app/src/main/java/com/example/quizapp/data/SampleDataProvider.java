package com.example.quizapp.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp bộ câu hỏi mẫu ban đầu cho ứng dụng chuẩn các môn PTIT:
 * - Cơ sở dữ liệu (CSDL)
 * - Mạng máy tính (Mạng)
 * - Hệ điều hành (HĐH)
 */
public class SampleDataProvider {

    public static List<Question> getSampleQuestions() {
        List<Question> list = new ArrayList<>();
        long now = System.currentTimeMillis();

        // Môn Cơ sở dữ liệu
        list.add(new Question(0, "Trong mô hình quan hệ, khóa chính dùng để làm gì?", "Cơ sở dữ liệu",
                "Xác định duy nhất mỗi bản ghi",
                "Sắp xếp dữ liệu theo thứ tự",
                "Mã hóa dữ liệu trong bảng",
                "Sao lưu dữ liệu định kỳ",
                0,
                "Khóa chính (Primary Key) là một thuộc tính hoặc tập thuộc tính dùng để định danh duy nhất mỗi bản ghi (bộ) trong bảng.",
                true, now, now));

        list.add(new Question(0, "Câu lệnh SQL nào dùng để xóa hoàn toàn cấu trúc một bảng?", "Cơ sở dữ liệu",
                "DELETE TABLE", "REMOVE TABLE", "DROP TABLE", "CLEAR TABLE",
                2,
                "Lệnh DROP TABLE xóa toàn bộ bảng bao gồm cả cấu trúc dữ liệu và ràng buộc liên quan.",
                true, now, now));

        list.add(new Question(0, "Ràng buộc toàn vẹn thực thể (Entity Integrity) yêu cầu điều gì?", "Cơ sở dữ liệu",
                "Khóa ngoại không được rỗng",
                "Khóa chính không được nhận giá trị NULL",
                "Mọi bảng phải có ít nhất 2 cột",
                "Không được trùng tên bảng",
                1,
                "Ràng buộc toàn vẹn thực thể quy định khóa chính của bảng không bao giờ được phép mang giá trị NULL.",
                true, now, now));

        list.add(new Question(0, "Thuộc tính nào của Giao dịch (Transaction) đảm bảo hoặc tất cả các thao tác thành công, hoặc không có thao tác nào diễn ra?", "Cơ sở dữ liệu",
                "Tính nguyên tố (Atomicity)",
                "Tính nhất quán (Consistency)",
                "Tính cô lập (Isolation)",
                "Tính bền vững (Durability)",
                0,
                "Thuộc tính Atomicity (Tính nguyên tử/nguyên tố) đảm bảo nguyên tắc 'All or Nothing' trong giao dịch.",
                true, now, now));

        // Môn Mạng máy tính
        list.add(new Question(0, "Giao thức nào hoạt động ở tầng giao vận trong mô hình TCP/IP?", "Mạng máy tính",
                "HTTP", "TCP", "IP", "Ethernet",
                1,
                "TCP và UDP là 2 giao thức chủ chốt hoạt động tại tầng Giao vận (Transport Layer).",
                true, now, now));

        list.add(new Question(0, "Địa chỉ IPv4 gồm bao nhiêu bit?", "Mạng máy tính",
                "16 bit", "32 bit", "64 bit", "128 bit",
                1,
                "Địa chỉ IPv4 có độ dài 32 bit (gồm 4 octet), còn IPv6 có độ dài 128 bit.",
                true, now, now));

        list.add(new Question(0, "Giao thức nào chịu trách nhiệm phân giải tên miền thành địa chỉ IP?", "Mạng máy tính",
                "DHCP", "FTP", "DNS", "SNMP",
                2,
                "DNS (Domain Name System) là hệ thống phân giải tên miền sang địa chỉ IP để định tuyến trên Internet.",
                true, now, now));

        list.add(new Question(0, "Thiết bị Switch hoạt động chủ yếu ở tầng nào của mô hình OSI?", "Mạng máy tính",
                "Tầng vật lý (Physical)",
                "Tầng liên kết dữ liệu (Data Link)",
                "Tầng mạng (Network)",
                "Tầng phiên (Session)",
                1,
                "Switch tiêu chuẩn hoạt động ở Tầng 2 (Data Link Layer) dựa trên địa chỉ MAC.",
                true, now, now));

        // Môn Hệ điều hành
        list.add(new Question(0, "Tiến trình (Process) khác chương trình (Program) ở điểm nào cơ bản nhất?", "Hệ điều hành",
                "Tiến trình là thực thể tĩnh, chương trình là động",
                "Tiến trình là một chương trình đang thực thi và có không gian bộ nhớ riêng",
                "Chương trình tiêu tốn chu kỳ CPU nhiều hơn tiến trình",
                "Hai khái niệm hoàn toàn tương đương nhau",
                1,
                "Chương trình là file tĩnh trên đĩa cứng, còn tiến trình là một thể hiện động đang chạy trong RAM kèm tài nguyên cấp phát.",
                true, now, now));

        list.add(new Question(0, "Trạng thái nào sau đây KHÔNG thuộc chu kỳ sống cơ bản của một tiến trình?", "Hệ điều hành",
                "Ready (Sẵn sàng)",
                "Running (Đang chạy)",
                "Waiting/Blocked (Chờ đợi)",
                "Archived (Lưu trữ)",
                3,
                "Các trạng thái cơ bản của tiến trình gồm: New, Ready, Running, Waiting (Blocked) và Terminated.",
                true, now, now));

        list.add(new Question(0, "Điều kiện nào sau đây KHÔNG phải là một trong 4 điều kiện gây ra Deadlock (Bế tắc)?", "Hệ điều hành",
                "Loại trừ lẫn nhau (Mutual Exclusion)",
                "Giữ và chờ (Hold and Wait)",
                "Có quyền trưng dụng tài nguyên (Preemption)",
                "Chờ đợi vòng tròn (Circular Wait)",
                2,
                "Điều kiện gây ra deadlock là 'Không thể trưng dụng' (No preemption).",
                true, now, now));

        list.add(new Question(0, "Giải thuật lập lịch CPU nào ưu tiên tiến trình có thời gian thực thi ngắn nhất?", "Hệ điều hành",
                "FCFS", "SJF (Shortest Job First)", "Round Robin", "Priority Scheduling",
                1,
                "SJF (Shortest Job First) chọn tiến trình có CPU burst time ngắn nhất để cấp phát CPU trước.",
                true, now, now));

        return list;
    }

    /**
     * Nạp dữ liệu mẫu vào database trong 1 Transaction
     */
    public static void seed(SQLiteDatabase db) {
        List<Question> samples = getSampleQuestions();
        db.beginTransaction();
        try {
            for (Question q : samples) {
                ContentValues values = new ContentValues();
                values.put(DbContract.QuestionEntry.COLUMN_CONTENT, q.getContent());
                values.put(DbContract.QuestionEntry.COLUMN_CATEGORY, q.getCategory());
                values.put(DbContract.QuestionEntry.COLUMN_OPTION_A, q.getOptionA());
                values.put(DbContract.QuestionEntry.COLUMN_OPTION_B, q.getOptionB());
                values.put(DbContract.QuestionEntry.COLUMN_OPTION_C, q.getOptionC());
                values.put(DbContract.QuestionEntry.COLUMN_OPTION_D, q.getOptionD());
                values.put(DbContract.QuestionEntry.COLUMN_CORRECT_ANSWER, q.getCorrectAnswer());
                values.put(DbContract.QuestionEntry.COLUMN_EXPLANATION, q.getExplanation());
                values.put(DbContract.QuestionEntry.COLUMN_IS_SAMPLE, 1);
                values.put(DbContract.QuestionEntry.COLUMN_CREATED_AT, q.getCreatedAt());
                values.put(DbContract.QuestionEntry.COLUMN_UPDATED_AT, q.getUpdatedAt());

                db.insert(DbContract.QuestionEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
