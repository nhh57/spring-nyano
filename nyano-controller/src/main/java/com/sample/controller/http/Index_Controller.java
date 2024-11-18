package com.sample.controller.http;

import com.sample.application.service.event.EventAppService;
import com.sample.application.service.event.EventAppServiceRedis;
import com.sample.application.service.reentrantlock.ReentrantLockService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/index")
public class Index_Controller {
    private final EventAppService eventAppService;
    private final EventAppServiceRedis eventAppServiceRedis;
    private final RedissonClient redissonClient;
    private final ReentrantLockService processApproval;
    private static final SecureRandom secureRandom = new SecureRandom();

    public Index_Controller(EventAppService eventAppService, EventAppServiceRedis eventAppServiceRedis,
                            RedissonClient redissonClient, ReentrantLockService processApproval) {
        this.eventAppService = eventAppService;
        this.eventAppServiceRedis = eventAppServiceRedis;
        this.redissonClient = redissonClient;
        this.processApproval = processApproval;
    }

    @GetMapping("/distributed")
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHello")
    public String index() {
        String key = "hainh";
        String data = (String) eventAppServiceRedis.getSayHiRedis(key);

        if (data != null) {
            return data;
        }

        // Sử dụng khóa phân tán Redis với Redisson
        RLock lock = redissonClient.getLock("lock:database:fetch:" + key);
        boolean isLocked = false;
        try {
            // Thử lấy khóa với thời gian chờ 1 giây và giữ khóa trong tối đa 30 giây
            isLocked = lock.tryLock(1, 3, TimeUnit.SECONDS);
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
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String approve(@PathVariable String approvalId) {
        String response = processApproval.processApproval(approvalId);
        System.out.println("response ::%s" + response);
        return response;
    }

    public String fallbackHello(Throwable throwable) {
        return "Too many requests.";
    }

    @GetMapping("/circuit/breaker")
    @CircuitBreaker(name = "checkRandom", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreaker() {
        int productId = secureRandom.nextInt(20) + 1;
        String url = "https://fakestoreapi.com/products/" + productId;
        return url;
    }

    public String fallbackCircuitBreaker(Throwable throwable) {
        return "Service fakestoreapi Error ";
    }
}
