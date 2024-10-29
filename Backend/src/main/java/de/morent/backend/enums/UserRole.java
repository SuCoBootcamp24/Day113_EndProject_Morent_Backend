package de.morent.backend.enums;

public enum UserRole {
    USER("User"),
    VIP("VIP"),
    ADMIN("Admin"),
    MANAGER("Manager"),
    ACCOUNTANT("Accountant");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}