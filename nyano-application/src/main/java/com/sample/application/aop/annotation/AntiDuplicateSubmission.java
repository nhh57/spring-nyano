package com.sample.application.aop.annotation;


import java.lang.annotation.*;

/**
 * Chống  tránh  trùng  lặp  nộp  gửi  chú thích
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AntiDuplicateSubmission {


    /**
     * Thời gian hết hạn mặc định là 3 giây, tức là không thể nhấp lại trong vòng 3 giây.
     */
    long expire() default 3;

    /**
     * Cách ly giữa các người dùng, mặc định là false.
     * Nếu là true, thì giới hạn toàn cục, nếu là true thì cần trạng thái đăng nhập của người dùng, nếu không thì cách ly toàn cục
     */
    boolean userSpecific() default false;
}