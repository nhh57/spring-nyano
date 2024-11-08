package com.sample.application.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ném ngoại lệ này nếu cần retry
 *
 * @author hainh
 * @since 2024/10/05
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RetryException extends RuntimeException {
    // Biến serialVersionUID được sử dụng để xác định phiên bản của lớp này khi được serialize
    private static final long serialVersionUID = 7886918292771470846L;

    public RetryException(String msg) {
        super(msg);
    }
}