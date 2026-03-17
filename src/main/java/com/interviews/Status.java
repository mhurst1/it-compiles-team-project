package com.interviews;

/**
 * MH and SE
 * This enum represents the different statuses that a user can have in the application. The possible statuses are USER, CONTRIBUTOR, and ADMIN. Each status may have different permissions and access levels within the application, allowing for role-based functionality and features. This enum can be used to manage user roles and control access to certain features based on the user's status.
 * USER: This status represents a regular user of the application who can view and interact with content but may have limited permissions for creating or modifying content.
 * CONTRIBUTOR: This status represents a user who has the ability to contribute content to the application, such as adding questions or comments. Contributors may have more permissions than regular users but may still have some restrictions compared to admins.
 * ADMIN: This status represents a user with administrative privileges who has full access to all features and functionalities of the application. Admins can manage users, content, and settings within the application, and they have the highest level of permissions.
 */
public enum Status {
    USER,
    CONTRIBUTOR,
    ADMIN
}
