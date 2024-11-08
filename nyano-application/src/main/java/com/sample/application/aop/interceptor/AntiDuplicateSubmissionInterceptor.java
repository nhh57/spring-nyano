package com.sample.application.aop.interceptor;

import cn.hutool.json.JSONUtil;

import com.sample.application.aop.annotation.AntiDuplicateSubmission;
import com.sample.application.cache.Cache;
import com.sample.application.enums.ResultCode;
import com.sample.application.exception.ServiceException;
import com.sample.application.security.context.UserContext;
import com.sample.application.token.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Ngăn chặn  trùng  lặp  nộp  gửi  dịch vụ
 */
@Aspect
@Component
@Slf4j
public class AntiDuplicateSubmissionInterceptor {

    @Autowired
    private Cache<String> cache;


    @Before("@annotation(antiDuplicateSubmission)")
    public void interceptor(AntiDuplicateSubmission antiDuplicateSubmission) {

        try {
            String redisKey = getParams(antiDuplicateSubmission.userSpecific());
            Long count = cache.increment(redisKey, antiDuplicateSubmission.expire());
            log.debug("Ngăn chặn trùng lặp nộp gửi: params-{},value-{}", redisKey, count);
            //Nếu vượt quá 0 hoặc thiết lập tham số, thì cho thấy đã trùng lặp nộp gửi
            if (count.intValue() > 0) {
                throw new ServiceException(ResultCode.RATE_LIMIT_ERROR);
            }
        }
        //Nếu tham số rỗng, thì cho thấy người dùng chưa đăng nhập, bỏ qua trực tiếp, không xử lý
        catch (NullPointerException e) {
            return;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Bộ lọc chống trùng lặp nộp gửi ngoại lệ", e);
            throw new ServiceException(ResultCode.ERROR);
        }
    }

    /**
     * Lấy tham số biểu mẫu
     *
     * @param userSpecific Người dùng có cách ly hay không
     * @return Khóa bộ đếm
     */
    private String getParams(Boolean userSpecific) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuilder paramBuilder = new StringBuilder();
        //Nối chuỗi địa chỉ yêu cầu
        paramBuilder.append(request.getRequestURI());

        //Tham số không rỗng thì nối chuỗi tham số
        if (!request.getParameterMap().isEmpty()) {
            paramBuilder.append(JSONUtil.toJsonStr(request.getParameterMap()));
        }
        //Cài đặt cách ly người dùng thành mở, thì chọn người dùng hiện tại
        if (userSpecific) {
            AuthUser authUser = UserContext.getCurrentUser();
            //Người dùng rỗng thì đưa ra cảnh báo, nhưng không nối chuỗi, nếu không thì nối chuỗi id người dùng
            if (authUser == null) {
                log.warn("Cài đặt cách ly người dùng được mở, nhưng người dùng hiện tại là rỗng");
            }
            // Không rỗng thì nối chuỗi id người dùng
            else {
                paramBuilder.append(authUser.getId());
            }
        }
        //Địa chỉ yêu cầu
        return paramBuilder.toString();
    }


}