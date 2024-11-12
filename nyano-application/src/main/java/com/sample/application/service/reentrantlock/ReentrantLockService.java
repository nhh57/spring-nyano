package com.sample.application.service.reentrantlock;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ReentrantLockService {// Lưu trữ các khóa cho từng lệnh duyệt
    private final ConcurrentHashMap<String, Lock> approvalLocks = new ConcurrentHashMap<>();

    // Phương thức để xử lý lệnh duyệt
    public String processApproval(String approvalId) {
        System.out.println("approvalId::{" + approvalId + "}");
        // Tạo hoặc lấy khóa cho lệnh duyệt
        Lock lock = approvalLocks.computeIfAbsent(approvalId, k -> new ReentrantLock());

        // Kiểm tra xem khóa có đang bị giữ bởi luồng khác không
        if (lock.tryLock()) {
            try {
                // Xử lý lệnh duyệt
                Thread.sleep(1000); // Giả lập xử lý mất 2 giây
                return "Đã xử lý lệnh duyệt: " + approvalId;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Lỗi: bị gián đoạn khi xử lý lệnh " + approvalId;
            } finally {
                // Giải phóng khóa sau khi xử lý xong
                lock.unlock();
            }
        } else {
            // Nếu không lấy được khóa, báo rằng lệnh đang được xử lý
            return "Lệnh duyệt " + approvalId + " đang được xử lý bởi người khác.";
        }
    }
}