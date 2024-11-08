package com.sample.application.security.enums;

/**
 * Token Role Type
 *
 * @author hainh
 * @since 2024/10/05
 */
public enum UserEnums {
    /**
     * Vai trò
     */
    MEMBER("member"),
    STORE("store"),
    MANAGER("manager"),
    SYSTEM("system");
    private final String role;

    UserEnums(String r) {
        this.role = r;
    }

    public String getRole() {
        return role;
    }
}