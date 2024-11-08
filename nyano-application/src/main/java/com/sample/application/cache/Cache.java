package com.sample.application.cache;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interface cache
 *
 * @author hainh
 */
public interface Cache<T> {

    /**
     * Lấy một mục từ cache, không giao dịch.
     *
     * @param key Khóa cache
     * @return Đối tượng được lưu trong cache hoặc <tt>null</tt>
     */
    T get(Object key);

    /**
     * Thêm một mục vào bộ nhớ cache, phi giao dịch, với
     * ngữ nghĩa thất bại nhanh
     *
     * @param key   Khóa cache
     * @param value Giá trị cache
     */
    void put(Object key, T value);

    /**
     * Ghi nội dung vào bộ nhớ cache
     *
     * @param key   Khóa cache
     * @param value Giá trị cache
     * @param exp   Thời gian hết hạn, đơn vị là giây
     */
    void put(Object key, T value, Long exp);

    /**
     * Ghi nội dung vào bộ nhớ cache
     *
     * @param key      Khóa cache
     * @param value    Giá trị cache
     * @param exp      Thời gian hết hạn
     * @param timeUnit Đơn vị thời gian hết hạn
     */
    void put(Object key, T value, Long exp, TimeUnit timeUnit);

    /**
     * Kiểm tra xem có chứa khóa hay không
     *
     * @param key Khóa cache
     * @return True nếu có chứa khóa, False nếu không
     */
    boolean hasKey(Object key);

    /**
     * Xóa
     *
     * @param key Khóa cache
     */
    Boolean remove(Object key);

    /**
     * Bộ đếm redis tăng thêm
     * Lưu ý: Sau khi đạt đến liveTime, lần tăng thêm này sẽ bị hủy bỏ, tức là tự động -1, thay vì giá trị redis rỗng
     *
     * @param key      Là key cộng dồn, cùng một key mỗi lần gọi thì giá trị +1
     * @param liveTime Đơn vị giây sau khi hết hạn
     * @return Kết quả bộ đếm
     */
    Long increment(String key, long liveTime);

    /**
     * Xóa hàng loạt
     *
     * @param keys Tập hợp các khóa cần xóa
     */
    void batchDelete(Collection keys);
}