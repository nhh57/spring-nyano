package com.sample.application.security.context;


import com.google.gson.Gson;
import com.sample.application.cache.Cache;
import com.sample.application.cache.CachePrefix;
import com.sample.application.enums.ResultCode;
import com.sample.application.exception.ServiceException;
import com.sample.application.security.enums.SecurityEnum;
import com.sample.application.token.AuthUser;
import com.sample.application.token.SecretKeyUtil;
import com.sample.application.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * User context
 */
public class UserContext {

    /**
     * Lấy thông tin người dùng từ request
     *
     * @return Người dùng được ủy quyền
     */
    public static AuthUser getCurrentUser() {
        // Kiểm tra xem thuộc tính request có tồn tại hay không
        if (RequestContextHolder.getRequestAttributes() != null) {
            // Lấy đối tượng HttpServletRequest từ thuộc tính request
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // Lấy giá trị token từ header của request, sử dụng SecurityEnum để lấy tên header
            String accessToken = request.getHeader(SecurityEnum.AUTHORIZATION_HEADER.getValue());
            // Gọi hàm getAuthUser() để xác thực token và lấy thông tin người dùng
            return getAuthUser(accessToken);
        }
        // Nếu không có thuộc tính request hoặc không thể xác thực token, trả về null
        return null;
    }

    /**
     * Lấy thông tin người dùng từ request
     *
     * @return Người dùng được ủy quyền
     */
    public static String getUuid() {
        // Kiểm tra xem thuộc tính request có tồn tại hay không
        if (RequestContextHolder.getRequestAttributes() != null) {
            // Lấy đối tượng HttpServletRequest từ thuộc tính request
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // Lấy giá trị header có tên là SecurityEnum.UUID.getValue() từ request và trả về
            return request.getHeader(SecurityEnum.UUID.getValue());
        }
        // Nếu không có thuộc tính request, trả về null
        return null;
    }


    /**
     * Lấy thông tin người dùng từ token trong bộ nhớ cache
     *
     * @param cache       Bộ nhớ cache
     * @param accessToken Token
     * @return Người dùng được ủy quyền
     */
    public static AuthUser getAuthUser(Cache cache, String accessToken) {
        try {
            // Gọi phương thức getAuthUser để lấy thông tin người dùng từ token
            AuthUser authUser = getAuthUser(accessToken);
            // Khẳng định rằng authUser không null (nếu null thì sẽ throw AssertionError)
            assert authUser != null;

            // Kiểm tra xem token đã được lưu trong cache hay chưa
            if (!cache.hasKey(CachePrefix.ACCESS_TOKEN.getPrefix(authUser.getRole(), authUser.getId()) + accessToken)) {
                // Nếu token không có trong cache, ném ngoại lệ ServiceException với mã lỗi USER_PERMISSION_ERROR
                throw new ServiceException(ResultCode.USER_PERMISSION_ERROR);
            }
            // Nếu token hợp lệ và đã được lưu trong cache, trả về đối tượng AuthUser
            return authUser;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentUserToken() {
        // Kiểm tra xem thuộc tính request có tồn tại hay không
        if (RequestContextHolder.getRequestAttributes() != null) {
            // Lấy đối tượng HttpServletRequest từ thuộc tính request
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // Lấy giá trị header có tên là SecurityEnum.HEADER_TOKEN.getValue() từ request và trả về
            return request.getHeader(SecurityEnum.AUTHORIZATION_HEADER.getValue());
        }
        // Nếu không có thuộc tính request, trả về null
        return null;
    }

    /**
     * Lấy thông tin người dùng từ token trong JWT
     *
     * @param accessToken Token
     * @return Người dùng được ủy quyền
     */
    public static AuthUser getAuthUser(String accessToken) {
        try {
            // Lấy thông tin từ token
            Claims claims = Jwts.parser().setSigningKey(SecretKeyUtil.generalKeyByDecoders()).parseClaimsJws(accessToken).getBody();
            // Lấy thông tin người dùng được lưu trữ trong claims
            String json = claims.get(SecurityEnum.USER_CONTEXT_KEY.getValue()).toString();
            // Chuyển đổi chuỗi JSON thành đối tượng AuthUser và trả về
            return new Gson().fromJson(json, AuthUser.class);
        } catch (Exception e) {
            // Nếu có ngoại lệ xảy ra, trả về null
            return null;
        }
    }


    /**
     * Ghi thông tin người mời
     */
    public static void settingInviter(String memberId, Cache cache) {
        if (RequestContextHolder.getRequestAttributes() != null) {
            // Lấy đối tượng HttpServletRequest từ thuộc tính request
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // Lấy giá trị header có tên là SecurityEnum.INVITER.getValue() từ request
            String inviterId = request.getHeader(SecurityEnum.INVITER.getValue());
            // Kiểm tra xem inviterId có phải là chuỗi rỗng hoặc null hay không
            if (StringUtils.isNotEmpty(inviterId)) {
                // Lưu thông tin inviterId vào cache với key là CachePrefix.INVITER.getPrefix() + memberId
                cache.put(CachePrefix.INVITER.getPrefix() + memberId, inviterId);
            }
        }
    }


}