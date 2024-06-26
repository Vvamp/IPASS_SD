package org.vvamp.ingenscheveer.security.authentication;


public class ValidationResult {
    private final ValidationStatus status;
    private final String details;
    private final User user;

    public ValidationResult(ValidationStatus status, String details, User user) {
        this.status = status;
        this.details = details;
        this.user = user;
    }

    public ValidationResult(ValidationStatus status, String details) {
        this.status = status;
        this.details = details;
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public ValidationStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

}