package enums;

/**
 * Represents the different roles a user can have in the system.
 * 
 * - GUEST: A user without a subscription or special privileges.
 * - SUBSCRIBER: A registered user with subscription privileges.
 * - SUPERVISOR: A staff member with supervisory permissions.
 * - MANAGER: A staff member with full management permissions.
 */
public enum UserRole {
    /** A user without a subscription or special privileges. */
    GUEST,

    /** A registered user with subscription privileges. */
    SUBSCRIBER,

    /** A staff member with supervisory permissions. */
    SUPERVISOR,

    /** A staff member with full management permissions. */
    MANAGER
}
