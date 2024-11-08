package com.sample.application.exception;

import com.sample.application.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Lớp ngoại lệ nghiệp vụ toàn cục
 *
 * @author hainh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    // Biến serialVersionUID được sử dụng để xác định phiên bản của lớp này khi được serialize
    private static final long serialVersionUID = 3447728300174142127L;

    public static final String DEFAULT_MESSAGE = "Lỗi mạng, vui lòng thử lại sau!";

    /**
     * Thông báo lỗi
     */
    private String msg = DEFAULT_MESSAGE;

    /**
     * Mã lỗi
     */
    private ResultCode resultCode;

    public ServiceException(String msg) {
        // Mặc định mã lỗi là ERROR
        this.resultCode = ResultCode.ERROR;
        this.msg = msg;
    }

    /**
     * Constructor tạo ngoại lệ mặc định.
     */
    public ServiceException() {
        super();
    }

    /**
     * Constructor tạo ngoại lệ với mã lỗi được cung cấp.
     *
     * @param code Mã lỗi
     */
    public ServiceException(ResultCode code) {
        this.resultCode = code;
    }

    /**
     * Constructor tạo ngoại lệ với mã lỗi và thông báo lỗi được cung cấp.
     *
     * @param resultCode Mã lỗi
     * @param msg    Thông báo lỗi
     */
    public ServiceException(ResultCode resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }
}