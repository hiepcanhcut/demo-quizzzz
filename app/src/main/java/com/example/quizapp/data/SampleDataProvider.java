package com.example.quizapp.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp bộ câu hỏi mẫu phong phú cho ứng dụng chuẩn các môn PTIT:
 * - Cơ sở dữ liệu (CSDL)
 * - Mạng máy tính (Mạng)
 * - Hệ điều hành (HĐH)
 */
public class SampleDataProvider {

    public static List<Question> getSampleQuestions() {
        List<Question> list = new ArrayList<>();
        long now = System.currentTimeMillis();

        // ==========================================
        // MÔN 1: CƠ SỞ DỮ LIỆU (CSDL) - 20 CÂU HỎI
        // ==========================================
        list.add(new Question(0, "Trong mô hình quan hệ, khóa chính dùng để làm gì?", "Cơ sở dữ liệu",
                "Xác định duy nhất mỗi bản ghi trong bảng",
                "Sắp xếp dữ liệu theo thứ tự tăng dần",
                "Mã hóa dữ liệu trong bảng",
                "Sao lưu dữ liệu định kỳ",
                0,
                "Khóa chính (Primary Key) là một thuộc tính hoặc tập thuộc tính dùng để định danh duy nhất mỗi bản ghi (bộ) trong bảng.",
                true, now, now));

        list.add(new Question(0, "Câu lệnh SQL nào dùng để xóa hoàn toàn cấu trúc một bảng?", "Cơ sở dữ liệu",
                "DELETE TABLE", "REMOVE TABLE", "DROP TABLE", "CLEAR TABLE",
                2,
                "Lệnh DROP TABLE xóa toàn bộ bảng bao gồm cả định nghĩa bảng, cấu trúc dữ liệu và ràng buộc liên quan.",
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
                "Thuộc tính Atomicity (Tính nguyên tố/nguyên tử) trong chuẩn ACID đảm bảo nguyên tắc 'All or Nothing'.",
                true, now, now));

        list.add(new Question(0, "Một quan hệ ở dạng chuẩn 1 (1NF) khi và chỉ khi nào?", "Cơ sở dữ liệu",
                "Không có thuộc tính đa trị hay phức hợp (các giá trị thuộc tính đều nguyên tố)",
                "Mọi thuộc tính không khóa phụ thuộc hàm đầy đủ vào khóa chính",
                "Không có phụ thuộc bắc cầu",
                "Mọi phụ thuộc hàm đều có vế trái là siêu khóa",
                0,
                "Dạng chuẩn 1 (1NF) yêu cầu miền giá trị của mọi thuộc tính chỉ chứa các giá trị nguyên tố (nguyên tử), không chứa đa trị.",
                true, now, now));

        list.add(new Question(0, "Một quan hệ ở dạng chuẩn 2 (2NF) khi nào?", "Cơ sở dữ liệu",
                "Đã đạt 1NF và mọi thuộc tính không khóa phụ thuộc hàm đầy đủ vào khóa chính",
                "Chỉ cần đạt 1NF",
                "Không chứa bất kỳ khóa ngoại nào",
                "Không có phụ thuộc hàm bắc cầu",
                0,
                "2NF yêu cầu đạt 1NF và loại bỏ phụ thuộc bộ phận, tức mọi thuộc tính không khóa phải phụ thuộc đầy đủ vào khóa chính.",
                true, now, now));

        list.add(new Question(0, "Dạng chuẩn 3 (3NF) giải quyết vấn đề phụ thuộc nào?", "Cơ sở dữ liệu",
                "Phụ thuộc hàm bộ phận",
                "Phụ thuộc hàm bắc cầu giữa các thuộc tính không khóa",
                "Phụ thuộc đa trị",
                "Phụ thuộc nối",
                1,
                "Dạng chuẩn 3 (3NF) yêu cầu đạt 2NF và loại bỏ các phụ thuộc hàm bắc cầu qua các thuộc tính không khóa.",
                true, now, now));

        list.add(new Question(0, "Từ khóa SQL nào dùng để loại bỏ các bản ghi trùng lặp trong kết quả SELECT?", "Cơ sở dữ liệu",
                "UNIQUE", "DISTINCT", "DIFFERENT", "NO_DUPLICATE",
                1,
                "Từ khóa DISTINCT trong câu lệnh SELECT DISTINCT loại bỏ tất cả các dòng trùng lặp trong tập kết quả trả về.",
                true, now, now));

        list.add(new Question(0, "Mệnh đề nào trong SQL được dùng để lọc kết quả của các hàm tổng hợp (Aggregate Functions)?", "Cơ sở dữ liệu",
                "WHERE", "HAVING", "GROUP BY", "ORDER BY",
                1,
                "Mệnh đề HAVING dùng để lọc các nhóm sau khi đã gom nhóm (GROUP BY) và tính toán với hàm tổng hợp như COUNT, SUM, AVG.",
                true, now, now));

        list.add(new Question(0, "Phép nối bảng nào trả về tất cả các hàng từ bảng bên trái và các hàng khớp từ bảng bên phải?", "Cơ sở dữ liệu",
                "INNER JOIN", "RIGHT JOIN", "LEFT JOIN", "FULL JOIN",
                2,
                "LEFT JOIN (hoặc LEFT OUTER JOIN) giữ lại tất cả bản ghi của bảng bên trái, điền NULL cho cột bảng phải nếu không khớp.",
                true, now, now));

        list.add(new Question(0, "Chỉ mục (Index) trong cơ sở dữ liệu có tác dụng chính là gì?", "Cơ sở dữ liệu",
                "Tăng tốc độ truy vấn và tìm kiếm dữ liệu",
                "Tiết kiệm dung lượng lưu trữ trên ổ đĩa",
                "Tăng tốc độ thêm, sửa, xóa dữ liệu",
                "Mã hóa bảo mật cho dữ liệu",
                0,
                "Index giúp hệ quản trị CSDL định vị và tìm kiếm dữ liệu nhanh hơn, tuy nhiên làm giảm nhẹ tốc độ ghi (INSERT/UPDATE).",
                true, now, now));

        list.add(new Question(0, "Khóa ngoại (Foreign Key) được sử dụng để làm gì?", "Cơ sở dữ liệu",
                "Tăng tốc độ câu truy vấn SELECT",
                "Thiết lập mối quan hệ và đảm bảo toàn vẹn tham chiếu giữa hai bảng",
                "Tự động tăng giá trị của cột số",
                "Bảo vệ dữ liệu không bị xóa ngoài ý muốn",
                1,
                "Khóa ngoại liên kết tới khóa chính/khóa duy nhất của bảng khác nhằm duy trì tính toàn vẹn tham chiếu (Referential Integrity).",
                true, now, now));

        list.add(new Question(0, "Mức độ cô lập (Isolation Level) nào sau đây cao nhất trong hệ quản trị CSDL?", "Cơ sở dữ liệu",
                "Read Uncommitted", "Read Committed", "Repeatable Read", "Serializable",
                3,
                "Serializable là mức cô lập cao nhất, thực thi các giao dịch tuần tự và ngăn ngừa hiện tượng đọc dữ liệu bẩn, bóng ma.",
                true, now, now));

        list.add(new Question(0, "Hiện tượng 'Dirty Read' (Đọc bẩn) xảy ra khi nào?", "Cơ sở dữ liệu",
                "Một giao dịch đọc dữ liệu chưa được commit từ một giao dịch khác",
                "Hai giao dịch đồng thời cập nhật một hàng",
                "Một giao dịch đọc dữ liệu đã bị xóa vĩnh viễn",
                "Một giao dịch lặp lại câu lệnh đọc và nhận giá trị khác nhau",
                0,
                "Dirty Read xảy ra khi Giao dịch A đọc một bản ghi mà Giao dịch B đã sửa đổi nhưng chưa COMMIT (có thể bị Rollback).",
                true, now, now));

        list.add(new Question(0, "Lệnh nào dùng để hoàn tác các thao tác trong một giao dịch chưa được lưu?", "Cơ sở dữ liệu",
                "COMMIT", "SAVEPOINT", "ROLLBACK", "REVOKE",
                2,
                "Lệnh ROLLBACK hủy bỏ mọi thay đổi kể từ đầu giao dịch hoặc từ một điểm lưu (SAVEPOINT).",
                true, now, now));

        list.add(new Question(0, "Cơ sở dữ liệu quan hệ tổ chức dữ liệu dưới dạng cấu trúc nào?", "Cơ sở dữ liệu",
                "Cây phân cấp (Hierarchical Tree)",
                "Bảng 2 chiều gồm các hàng và cột (Relations/Tables)",
                "Đồ thị gồm các đỉnh và cạnh (Graph)",
                "Tài liệu JSON không cấu trúc",
                1,
                "Mô hình dữ liệu quan hệ (Relational Model) do E.F. Codd đề xuất tổ chức dữ liệu dưới dạng các bảng hai chiều.",
                true, now, now));

        list.add(new Question(0, "Hàm tổng hợp nào trong SQL dùng để tính trung bình cộng của một cột kiểu số?", "Cơ sở dữ liệu",
                "TOTAL()", "SUM()", "AVG()", "MEDIAN()",
                2,
                "Hàm AVG() tính giá trị trung bình của tập hợp các giá trị số trong một cột.",
                true, now, now));

        list.add(new Question(0, "Phép toán đại số quan hệ nào tương ứng với việc chọn ra một số cột nhất định từ bảng?", "Cơ sở dữ liệu",
                "Phép chọn (Select - σ)",
                "Phép chiếu (Project - π)",
                "Phép tích Đề-các (Cartesian Product - ×)",
                "Phép hợp (Union - ∪)",
                1,
                "Phép chiếu (Project - ký hiệu π) giữ lại một tập các thuộc tính (cột) và loại bỏ các cột còn lại.",
                true, now, now));

        list.add(new Question(0, "Để cấp quyền truy cập trên bảng cho người dùng, người quản trị sử dụng lệnh nào?", "Cơ sở dữ liệu",
                "ALLOW", "GRANT", "SET PERMISSION", "ENABLE",
                1,
                "Lệnh GRANT trong phân hệ DCL (Data Control Language) dùng để cấp quyền thao tác trên đối tượng CSDL.",
                true, now, now));

        list.add(new Question(0, "Câu lệnh nào dùng để cập nhật giá trị dữ liệu hiện có trong một bảng?", "Cơ sở dữ liệu",
                "MODIFY", "CHANGE", "UPDATE", "ALTER",
                2,
                "Lệnh UPDATE dùng để thay đổi dữ liệu bên trong các hàng hiện có của bảng.",
                true, now, now));


        // ==========================================
        // MÔN 2: MẠNG MÁY TÍNH (MẠNG) - 20 CÂU HỎI
        // ==========================================
        list.add(new Question(0, "Giao thức nào hoạt động ở tầng giao vận trong mô hình TCP/IP?", "Mạng máy tính",
                "HTTP", "TCP", "IP", "Ethernet",
                1,
                "TCP và UDP là 2 giao thức chủ chốt hoạt động tại tầng Giao vận (Transport Layer).",
                true, now, now));

        list.add(new Question(0, "Địa chỉ IPv4 gồm bao nhiêu bit?", "Mạng máy tính",
                "16 bit", "32 bit", "64 bit", "128 bit",
                1,
                "Địa chỉ IPv4 có độ dài 32 bit (gồm 4 octet biểu diễn bằng hệ thập phân phân tách dấu chấm).",
                true, now, now));

        list.add(new Question(0, "Giao thức nào chịu trách nhiệm phân giải tên miền thành địa chỉ IP?", "Mạng máy tính",
                "DHCP", "FTP", "DNS", "SNMP",
                2,
                "DNS (Domain Name System) là hệ thống phân giải tên miền dễ nhớ (như google.com) sang địa chỉ IP dạng số.",
                true, now, now));

        list.add(new Question(0, "Thiết bị Switch hoạt động chủ yếu ở tầng nào của mô hình OSI?", "Mạng máy tính",
                "Tầng vật lý (Physical Layer)",
                "Tầng liên kết dữ liệu (Data Link Layer)",
                "Tầng mạng (Network Layer)",
                "Tầng phiên (Session Layer)",
                1,
                "Switch tiêu chuẩn hoạt động ở Tầng 2 (Data Link Layer) chuyển tiếp gói tin dựa trên địa chỉ MAC.",
                true, now, now));

        list.add(new Question(0, "Giao thức nào cung cấp dịch vụ truyền dữ liệu tin cậy, có hướng kết nối?", "Mạng máy tính",
                "UDP", "IP", "TCP", "ICMP",
                2,
                "TCP (Transmission Control Protocol) là giao thức hướng kết nối, đảm bảo gửi dữ liệu đúng thứ tự và không thất lạc.",
                true, now, now));

        list.add(new Question(0, "Cổng mặc định (Port number) của giao thức HTTPS là bao nhiêu?", "Mạng máy tính",
                "80", "443", "8080", "22",
                1,
                "Cổng tiêu chuẩn cho HTTP là 80, còn HTTPS (bảo mật SSL/TLS) sử dụng cổng 443.",
                true, now, now));

        list.add(new Question(0, "Quá trình bắt tay 3 bước (Three-way handshake) được sử dụng để làm gì?", "Mạng máy tính",
                "Thiết lập kết nối TCP giữa Client và Server (SYN, SYN-ACK, ACK)",
                "Đóng kết nối HTTP",
                "Truyền tải tập tin qua FTP",
                "Phân giải địa chỉ MAC qua ARP",
                0,
                "Bắt tay 3 bước (SYN -> SYN/ACK -> ACK) dùng để khởi tạo kết nối TCP tin cậy giữa hai máy tính.",
                true, now, now));

        list.add(new Question(0, "Mô hình OSI tiêu chuẩn bao gồm bao nhiêu tầng?", "Mạng máy tính",
                "4 tầng", "5 tầng", "7 tầng", "9 tầng",
                2,
                "Mô hình OSI có 7 tầng: Vật lý, Liên kết dữ liệu, Mạng, Giao vận, Phiên, Trình diễn và Ứng dụng.",
                true, now, now));

        list.add(new Question(0, "Giao thức ARP (Address Resolution Protocol) dùng để làm gì?", "Mạng máy tính",
                "Phân giải địa chỉ IP thành địa chỉ MAC vật lý",
                "Phân giải tên miền thành địa chỉ IP",
                "Cấp phát địa chỉ IP động cho máy trạm",
                "Gửi thông điệp báo lỗi mạng",
                0,
                "ARP tìm kiếm địa chỉ phần cứng (MAC address) tương ứng với một địa chỉ IP đã biết trong mạng cục bộ.",
                true, now, now));

        list.add(new Question(0, "Dải địa chỉ mạng nào sau đây là địa chỉ IP Private (mạng riêng) lớp C?", "Mạng máy tính",
                "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16", "127.0.0.0/8",
                2,
                "Địa chỉ IP Private gồm: Lớp A: 10.0.0.0/8; Lớp B: 172.16.0.0/12; Lớp C: 192.168.0.0/16.",
                true, now, now));

        list.add(new Question(0, "Mặt nạ mạng mặc định (Default Subnet Mask) của địa chỉ IP thuộc Lớp B là gì?", "Mạng máy tính",
                "255.0.0.0", "255.255.0.0", "255.255.255.0", "255.255.255.255",
                1,
                "Lớp A mặc định /8 (255.0.0.0), Lớp B là /16 (255.255.0.0), Lớp C là /24 (255.255.255.0).",
                true, now, now));

        list.add(new Question(0, "Lệnh nào trong dòng lệnh dùng để kiểm tra tính thông suốt kết nối giữa hai host qua giao thức ICMP?", "Mạng máy tính",
                "tracert", "netstat", "ping", "ipconfig",
                2,
                "Lệnh ping gửi các gói tin ICMP Echo Request để kiểm tra độ trễ và khả năng kết nối tới host đích.",
                true, now, now));

        list.add(new Question(0, "Giao thức nào cho phép tự động gán địa chỉ IP và cấu hình mạng cho các thiết bị khách?", "Mạng máy tính",
                "DNS", "DHCP", "FTP", "NAT",
                1,
                "DHCP (Dynamic Host Configuration Protocol) cấp phát địa chỉ IP tự động và thông số mạng cho thiết bị kết nối.",
                true, now, now));

        list.add(new Question(0, "Thiết bị Router hoạt động chính tại tầng nào của mô hình OSI?", "Mạng máy tính",
                "Tầng 1 (Physical)", "Tầng 2 (Data Link)", "Tầng 3 (Network)", "Tầng 4 (Transport)",
                2,
                "Router là thiết bị Tầng 3 (Network Layer) chịu trách nhiệm định tuyến gói tin (packet) dựa trên địa chỉ IP.",
                true, now, now));

        list.add(new Question(0, "Đặc điểm nổi bật của giao thức UDP so với TCP là gì?", "Mạng máy tính",
                "Tốc độ nhanh hơn vì không cần thiết lập kết nối và không kiểm tra thất lạc",
                "Độ tin cậy cao hơn tuyệt đối",
                "Có kiểm soát tắc nghẽn luồng truyền",
                "Đảm bảo các gói tin luôn đến đúng thứ tự",
                0,
                "UDP không hướng kết nối, phi trạng thái và không có overhead bắt tay, thích hợp cho video streaming, game online.",
                true, now, now));

        list.add(new Question(0, "Kỹ thuật NAT (Network Address Translation) có vai trò chính là gì?", "Mạng máy tính",
                "Chuyển đổi giữa địa chỉ IP riêng (Private) và địa chỉ IP công cộng (Public)",
                "Mã hóa các gói tin truyền trên đường truyền cáp quang",
                "Tăng tốc độ truy cập Internet gấp đôi",
                "Ngăn chặn hoàn toàn mọi virus xâm nhập mạng",
                0,
                "NAT giúp tiết kiệm không gian địa chỉ IPv4 bằng cách cho phép nhiều máy dùng IP riêng cùng truy cập Internet qua một IP công cộng.",
                true, now, now));

        list.add(new Question(0, "Độ dài của địa chỉ IPv6 là bao nhiêu bit?", "Mạng máy tính",
                "32 bit", "64 bit", "128 bit", "256 bit",
                2,
                "Địa chỉ IPv6 dài 128 bit, được viết dưới dạng 8 nhóm số hexa phân tách nhau bằng dấu hai chấm (:).",
                true, now, now));

        list.add(new Question(0, "Giao thức nào dùng để truyền tải tệp tin giữa máy tính Client và Server qua mạng?", "Mạng máy tính",
                "FTP (File Transfer Protocol)",
                "SMTP (Simple Mail Transfer Protocol)",
                "SNMP (Simple Network Management Protocol)",
                "POP3 (Post Office Protocol)",
                0,
                "FTP hoạt động trên tầng ứng dụng, sử dụng cổng 20 (dữ liệu) và 21 (lệnh) để gửi nhận file.",
                true, now, now));

        list.add(new Question(0, "Đơn vị dữ liệu (PDU) tại Tầng Giao vận (Transport Layer) được gọi là gì?", "Mạng máy tính",
                "Frame (Khung)", "Packet (Gói tin)", "Segment (Phân đoạn)", "Bit",
                2,
                "PDU: Tầng 1 là Bit, Tầng 2 là Frame, Tầng 3 là Packet, Tầng 4 là Segment (TCP) hoặc Datagram (UDP).",
                true, now, now));

        list.add(new Question(0, "Cáp xoắn đôi Cat6 sử dụng đầu nối chuẩn nào phổ biến nhất?", "Mạng máy tính",
                "RJ-11", "RJ-45", "BNC", "LC Fiber",
                1,
                "Đầu nối mạng chuẩn RJ-45 gồm 8 chân tiếp xúc dùng phổ biến cho mạng Ethernet cáp đồng UTP/STP.",
                true, now, now));


        // ==========================================
        // MÔN 3: HỆ ĐIỀU HÀNH (HĐH) - 20 CÂU HỎI
        // ==========================================
        list.add(new Question(0, "Tiến trình (Process) khác chương trình (Program) ở điểm nào cơ bản nhất?", "Hệ điều hành",
                "Tiến trình là thực thể tĩnh, chương trình là động",
                "Tiến trình là một chương trình đang trong trạng thái thực thi và có không gian nhớ riêng",
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
                "Các trạng thái chuẩn gồm: New (Mới tạo), Ready (Sẵn sàng), Running (Đang chạy), Waiting (Chờ I/O) và Terminated (Kết thúc).",
                true, now, now));

        list.add(new Question(0, "Điều kiện nào sau đây KHÔNG phải là một trong 4 điều kiện cần gây ra Deadlock (Bế tắc)?", "Hệ điều hành",
                "Loại trừ lẫn nhau (Mutual Exclusion)",
                "Giữ và chờ (Hold and Wait)",
                "Có quyền trưng dụng tài nguyên (Preemption)",
                "Chờ đợi vòng tròn (Circular Wait)",
                2,
                "Điều kiện gây ra deadlock là 'Không thể trưng dụng tài nguyên' (No preemption). Có quyền trưng dụng sẽ phá vỡ Deadlock.",
                true, now, now));

        list.add(new Question(0, "Giải thuật lập lịch CPU nào ưu tiên tiến trình có thời gian thực thi ngắn nhất?", "Hệ điều hành",
                "FCFS (First Come First Served)",
                "SJF (Shortest Job First)",
                "Round Robin (RR)",
                "Priority Scheduling",
                1,
                "SJF (Shortest Job First) chọn tiến trình có CPU burst time ngắn nhất để cấp phát CPU, giúp tối ưu thời gian chờ trung bình.",
                true, now, now));

        list.add(new Question(0, "Giải thuật lập lịch Round Robin (RR) sử dụng khái niệm cốt lõi nào?", "Hệ điều hành",
                "Định mức thời gian (Time Quantum / Time Slice)",
                "Độ ưu tiên tĩnh dựa trên PID",
                "Dung lượng bộ nhớ tiến trình chiếm dụng",
                "Số lượng tệp tiến trình đang mở",
                0,
                "Round Robin chia thời gian CPU thành các lát cắt nhỏ (time quantum), xoay vòng luân phiên giữa các tiến trình trong Ready Queue.",
                true, now, now));

        list.add(new Question(0, "Bộ nhớ ảo (Virtual Memory) mang lại lợi ích lớn nhất nào sau đây?", "Hệ điều hành",
                "Tăng tốc độ đọc ghi đĩa cứng",
                "Cho phép thực thi các chương trình có kích thước lớn hơn dung lượng bộ nhớ vật lý (RAM)",
                "Loại bỏ hoàn toàn sự cố phân mảnh bộ nhớ",
                "Giúp CPU chạy ở xung nhịp cao hơn",
                1,
                "Bộ nhớ ảo sử dụng một phần ổ cứng làm không gian hoán đổi (Swap/Page file), cho phép chạy chương trình lớn hơn RAM vật lý.",
                true, now, now));

        list.add(new Question(0, "Khối điều khiển tiến trình PCB (Process Control Block) chứa thông tin gì?", "Hệ điều hành",
                "Mã định danh tiến trình (PID), trạng thái CPU, thanh ghi và con trỏ lệnh",
                "Chỉ chứa tên của tác giả lập trình phần mềm",
                "Toàn bộ mã nguồn viết bằng ngôn ngữ C của hệ điều hành",
                "Thông số nhiệt độ của vi xử lý CPU",
                0,
                "PCB là cấu trúc dữ liệu của HĐH lưu trữ trạng thái tiến trình, con trỏ lệnh (PC), thanh ghi, thông tin bộ nhớ và I/O.",
                true, now, now));

        list.add(new Question(0, "Thao tác chuyển đổi ngữ cảnh (Context Switch) diễn ra khi nào?", "Hệ điều hành",
                "Khi CPU chuyển từ việc thực thi tiến trình này sang tiến trình khác",
                "Khi người dùng cắm thêm thanh RAM mới vào máy",
                "Khi máy tính chuyển sang chế độ Hibernate",
                "Khi ổ đĩa cứng được định dạng phân vùng lại",
                0,
                "Context Switch là quá trình HĐH lưu lại ngữ cảnh của tiến trình đang chạy và nạp ngữ cảnh của tiến trình tiếp theo để CPU thực thi.",
                true, now, now));

        list.add(new Question(0, "Hiện tượng Thrashing (Trì trệ trang) trong hệ điều hành là gì?", "Hệ điều hành",
                "Hiện tượng CPU dành phần lớn thời gian cho việc tráo trang (Paging) thay vì thực thi lệnh",
                "Ổ đĩa cứng bị đầy dung lượng đột ngột",
                "Màn hình xanh chết chóc (BSOD)",
                "Quạt tản nhiệt quay ở tốc độ tối đa gây tiếng ồn",
                0,
                "Thrashing xảy ra khi hệ thống liên tục thiếu bộ nhớ làm phát sinh Page Fault liên tục, khiến hệ thống tê liệt vì tráo trang.",
                true, now, now));

        list.add(new Question(0, "Thuật toán thay thế trang nào thay thế trang không được sử dụng trong thời gian dài nhất vừa qua?", "Hệ điều hành",
                "FIFO (First In First Out)",
                "LRU (Least Recently Used)",
                "OPT (Optimal Page Replacement)",
                "LFU (Least Frequently Used)",
                1,
                "LRU (Least Recently Used) ghi nhận thời điểm sử dụng gần nhất và chọn trang lâu nhất chưa được truy cập để hoán đổi ra đĩa.",
                true, now, now));

        list.add(new Question(0, "Đoạn găng (Critical Section) trong bài toán đồng bộ tiến trình là gì?", "Hệ điều hành",
                "Đoạn mã chương trình có khả năng gây lỗi bộ nhớ",
                "Đoạn mã truy cập và thao tác trên tài nguyên dùng chung giữa các tiến trình",
                "Thời gian CPU chuyển đổi giữa Kernel Mode và User Mode",
                "Khu vực bộ nhớ chứa mã lệnh của BIOS",
                1,
                "Critical Section là đoạn code truy cập tài nguyên chia sẻ, đòi hỏi phải đảm bảo nguyên tắc loại trừ lẫn nhau (Mutual Exclusion).",
                true, now, now));

        list.add(new Question(0, "Cơ chế Semaphore được dùng cho mục đích gì?", "Hệ điều hành",
                "Đồng bộ hóa các tiến trình và điều phối truy cập tài nguyên chia sẻ",
                "Lập chỉ mục tìm kiếm tập tin trên đĩa cứng",
                "Nén dữ liệu để tiết kiệm RAM",
                "Tăng tốc độ mạng Internet",
                0,
                "Semaphore (do Dijkstra đề xuất) là một biến nguyên dùng cho việc đồng bộ tiến trình thông qua 2 thao tác cơ bản: wait() và signal().",
                true, now, now));

        list.add(new Question(0, "Lỗi ngắt trang (Page Fault) xảy ra khi nào?", "Hệ điều hành",
                "Trang bộ nhớ cần truy cập chưa được nạp vào bộ nhớ vật lý (RAM)",
                "Bộ nhớ RAM bị hỏng chip nhớ phần cứng",
                "CPU gặp chỉ thị lệnh không hợp lệ",
                "Địa chỉ ảo vượt quá giới hạn 64 bit",
                0,
                "Page Fault là một bẫy lỗi (trap) xuất hiện khi tiến trình tham chiếu đến một trang nhớ hiện đang nằm trên đĩa (chưa có trong RAM).",
                true, now, now));

        list.add(new Question(0, "Phân mảnh ngoại vi (External Fragmentation) trong quản lý bộ nhớ là gì?", "Hệ điều hành",
                "Khoảng trống nhớ thừa bên trong một khối nhớ được cấp phát",
                "Tổng dung lượng bộ nhớ trống đủ lớn nhưng bị phân tán thành nhiều mảnh nhỏ không liên tục",
                "Ổ đĩa cứng bị hỏng sector bên ngoài",
                "Dữ liệu của tiến trình bị tràn ra ngoài bộ nhớ",
                1,
                "External Fragmentation xảy ra khi có đủ tổng dung lượng trống nhưng không có khối trống liền kề nào đủ lớn để chứa tiến trình.",
                true, now, now));

        list.add(new Question(0, "Trong mô hình đa luồng (Multithreading), các luồng (Thread) trong cùng một tiến trình chia sẻ tài nguyên nào?", "Hệ điều hành",
                "Không gian địa chỉ nhớ, biến toàn cục và các tập tin đang mở",
                "Mỗi luồng có không gian nhớ độc lập riêng biệt",
                "Các luồng chỉ chia sẻ thanh ghi con trỏ lệnh",
                "Không chia sẻ bất kỳ tài nguyên nào với nhau",
                0,
                "Các Thread thuộc cùng một Process chia sẻ chung không gian địa chỉ bộ nhớ (code, data, heap) nhưng có Call Stack riêng biệt.",
                true, now, now));

        list.add(new Question(0, "Thuật toán tránh bế tắc kinh điển của Dijkstra có tên là gì?", "Hệ điều hành",
                "Thuật toán Banker (Thuật toán Người coi ngân hàng)",
                "Thuật toán Round Robin",
                "Thuật toán Thang máy (SCAN)",
                "Thuật toán Spanning Tree",
                0,
                "Thuật toán Banker kiểm tra trạng thái an toàn (Safe State) trước khi quyết định cấp phát tài nguyên nhằm tránh Deadlock.",
                true, now, now));

        list.add(new Question(0, "Chế độ Kernel Mode (chế độ nhân) của CPU cho phép thực thi điều gì?", "Hệ điều hành",
                "Toàn quyền truy cập phần cứng và mọi chỉ thị đặc quyền của máy tính",
                "Chỉ được thực thi các lệnh tính toán thông thường của phần mềm ứng dụng",
                "Không thể can thiệp vào bộ nhớ RAM",
                "Chỉ dùng khi máy tính đang khởi động",
                0,
                "Kernel Mode là chế độ đặc quyền cao nhất của vi xử lý, dành riêng cho nhân hệ điều hành kiểm soát phần cứng an toàn.",
                true, now, now));

        list.add(new Question(0, "Lời gọi hệ thống (System Call) đóng vai trò gì?", "Hệ điều hành",
                "Cung cấp giao diện để chương trình ứng dụng yêu cầu các dịch vụ từ nhân hệ điều hành",
                "Gọi điện thoại quốc tế thông qua modem mạng",
                "Dọn dẹp các tệp rác trên ổ cứng tự động",
                "Kiểm tra nhiệt độ CPU định kỳ",
                0,
                "System Call là cơ chế giúp ứng dụng người dùng chuyển từ User Mode sang Kernel Mode để yêu cầu các thao tác I/O, tạo tiến trình,...",
                true, now, now));

        list.add(new Question(0, "Giải thuật lập lịch ổ đĩa nào di chuyển đầu đọc từ đầu này sang đầu kia đĩa như một chiếc thang máy?", "Hệ điều hành",
                "FCFS", "SSTF (Shortest Seek Time First)", "SCAN (Elevator Algorithm)", "LOOK",
                2,
                "Thuật toán SCAN (thường gọi là thuật toán Thang máy) quét đầu đọc đĩa theo một chiều phục vụ yêu cầu rồi đảo chiều quét lại.",
                true, now, now));

        list.add(new Question(0, "Cơ chế Spooling trong hệ điều hành thường được ứng dụng phổ biến nhất cho thiết bị nào?", "Hệ điều hành",
                "Máy in (Printer)", "Bàn phím máy tính", "Màn hình hiển thị", "Loa âm thanh",
                0,
                "Spooling (Simultaneous Peripheral Operations On-Line) xếp hàng dữ liệu gửi ra bộ đệm tạm thời trước khi chuyển tiếp cho máy in.",
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
