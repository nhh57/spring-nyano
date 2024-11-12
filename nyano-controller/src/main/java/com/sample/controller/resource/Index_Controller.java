package com.sample.controller.resource;

import com.sample.application.service.event.EventAppService;
import com.sample.application.service.event.EventAppServiceRedis;
import com.sample.application.cache.impl.RedisCache;
import com.sample.application.service.reentrantlock.ReentrantLockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/index")
public class Index_Controller {
    private final EventAppService eventAppService;
    private final EventAppServiceRedis eventAppServiceRedis;
    private final RedisCache redisCache;
    private final RedissonClient redissonClient;
    private final ReentrantLockService processApproval;

    public Index_Controller(EventAppService eventAppService, EventAppServiceRedis eventAppServiceRedis,
                            RedisCache redisCache, RedissonClient redissonClient, ReentrantLockService processApproval) {
        this.eventAppService = eventAppService;
        this.eventAppServiceRedis = eventAppServiceRedis;
        this.redisCache = redisCache;
        this.redissonClient = redissonClient;
        this.processApproval = processApproval;
    }

    @GetMapping("/distributed")
    public String index() {
        Object key = "hainh";
        String data = (String) eventAppServiceRedis.getSayHiRedis(key);

        if (data != null) {
            return data;
        }

        // Sử dụng khóa phân tán Redis với Redisson
        RLock lock = redissonClient.getLock("lock:database:fetch:" + key);
        boolean isLocked = false;
        try {
            // Thử lấy khóa với thời gian chờ 1 giây và giữ khóa trong tối đa 30 giây
            isLocked = lock.tryLock(1, 30, TimeUnit.SECONDS);
            if (isLocked) {
                // Kiểm tra lại trong Redis để tránh truy cập database không cần thiết
                data = (String) eventAppServiceRedis.getSayHiRedis(key);
                if (data == null) {
                    // Nếu không có trong cache, truy cập database và lưu vào cache
                    data = eventAppService.SayHi("hainh");
                    System.out.println("data::" + data);
                    eventAppServiceRedis.saveSayHi("hainh");
                }
            } else {
                // Nếu không thể lấy được khóa, trả về thông báo hoặc xử lý khác
                return "Request đang được xử lý, vui lòng thử lại sau.";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Có lỗi xảy ra khi truy cập khóa.";
        } finally {
            // Giải phóng khóa nếu đã lấy
            if (isLocked) {
                lock.unlock();
            }
        }
        return data;
    }


    @GetMapping("/{approvalId}")
    public String approve(@PathVariable String approvalId) {
        String response = processApproval.processApproval(approvalId);
        System.out.println("response ::%s" + response);
        return response;
    }
}
