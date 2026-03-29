package com.model;

import com.interviews.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.UUID;

/*
+-------------------------------------------------------+-------------------------------------------------------------------+
| Test                                                  | Reasoning                                                         |
+-------------------------------------------------------+-------------------------------------------------------------------+
| username = null                                       | a username shouldn't be null it should be a string                                                              |
| password = null                                       | a password shouldn't be null it should be a string                                                                  |
| firstName = null                                      | a first name shouldn't be null it should be a string                                                                  |
| lastName = null                                       | a last name shouldn't be null it should be a string                                                                  |
| email = null                                          | an email shouldn't be null it should be a string                                                                  |
| idUSC = null                                          | a usc id shouldn't be null it should be a string                                                                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| username = ""                                         | a username shouldn't be empty it should be a string                                                                  |
| password = ""                                         | a password shouldn't be empty it should be a string                                                               |
| firstName = ""                                        | a first name shouldn't be empty it should be a string                                                                |
| lastName = ""                                         | a last name shouldn't be empty it should be a string                                                                 |
| password = "   " (spaces only)                        | an email shouldn't be only spaces it should be a string                                                                  |
| username = "   " (spaces only)                        | a usc id shouldn't be only spaces it should be a string                                                                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| password = "a" (1 char)                               | passwords should be atleast 7 chars, have atleast one uppercase, and atleast one special char                                                                  |
| password = "abcdef!" (no uppercase)                   | passwords need atleast one uppercase                                                                   |
| password = "Abcdefg" (no special character)           | passwords need atleast one special character
| password = "A!" (uppercase + special, under 7 chars)  | passwords need to be atleast 7 chars                                                                                          |
+-------------------------------------------------------+-------------------------------------------------------------------+
| email = "notanemail" (no @)                           | emails need to have an @ symbol                                                                  |
| email = "user@" (no domain)                           | the email needs to have a correct domain                                                                  |
| email = "@" (just @ symbol)                           | emails need to have a proper beginning and ending                                                                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| graduationYear = -1                                   | graduation years cannot be negative                                                                  |
| graduationYear = 0                                    | graduation year cannot be 0                                                                  |
| graduationYear = 1                                    | graduation years must start with 20                                                                   |
+-------------------------------------------------------+-------------------------------------------------------------------+
| setUsername(null)                                     | a username cannot be set to a null value, it must be a string or smth of substance                                                                 |
| setPassword(null)                                     | a password cannot be set to a null value, it must be a string or smth of substance                                                                  |
| setUsername("")                                       | a username cannot be empty, it must be a string or smth of substance                                                                  |
| setPassword("")                                       | a password cannot be empty, it must be a string or smth of substance                                                                  |
| setEmail("bademail")                                  | an email cannot be just anything it has to pass the email req mentioned before                                                                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| user with null username added, then getUser(username) | a null pointer exception will be called                                                                  |
| user with null username added, then getUser(u, pw)    | a null pointer exception will be called                                                              |
| user with null password added, then getUser(u, pw)    | a null pointer exception will be called                                                                 |
| isMatch() called with null username                   | a null pointer exception will be called                                                                  |
| isMatch() called with null password                   | a null pointer exception will be called                                                                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| add same user object twice                            | every user must be unique                                                                  |
| add two users with same username                      | there cannot be 2 users with the same username
| add two users with same uscID                         | there cannot be 2 users with the same uscID
| add two users with same email                         | there cannot be 2 users with the same email
+-------------------------------------------------------+-------------------------------------------------------------------+
| editUser() on user not in list                        | a user should be notified that they are not in our database                                                                  |
| editUser() sets username to null                      | a username should not be allowed to be set to null, it must be of substance                                                                  |
| editUser() sets password to null                      | a password should not be allowed to be set to null, it must be of substance                                                                  |
| editUser() sets username to ""                        | a username should not be allowed to be set to empty, it must be of substance                                                                  |
| editUser() sets username to existing username "bob"   | a password should not be allowed to be set to empty, it must be of substance                                                                  |
+-------------------------------------------------------+-------------------------------------------------------------------+
| deleteUser() with a copy object (same UUID, new ref)  |                                                                   |
+-------------------------------------------------------+-------------------------------------------------------------------+

*/

/**
 * Comprehensive bug-finding tests for UserList and User.
 *
 * Tests are grouped into sections. Every test marked "FAILS" asserts
 * correct/expected behavior that the code does NOT currently satisfy.
 * Tests marked "PASSES" confirm working behavior.
 *
 * Bugs found:
 *  1.  No input validation on any field — null, empty, garbage all accepted
 *  2.  Null username/password causes NullPointerException inside UserList methods
 *  3.  No minimum password length
 *  4.  No email format check
 *  5.  Negative / impossible graduation year accepted
 *  6.  No addUser() method — internal list is fully exposed
 *  7.  Duplicate usernames are allowed with no check
 *  8.  editUser() silently no-ops when user is not in the list
 *  9.  deleteUser() uses reference equality, not UUID equality
 *  10. editUser() uses reference equality, not UUID equality
 *  11. editUser() can overwrite a username with null or empty string
 *  12. editUser() can create duplicate usernames
 */
public class UserListTest {

    private UserList userList;
    private User alice;
    private User bob;

    @Before
    public void setUp() {
        userList = UserList.getInstance();
        userList.getUsers().clear();

        alice = new User("Alice", "A", "alice", "pass1", "alice@test.com", 2025, "USC001");
        bob   = new User("Bob",   "B", "bob",   "pass2", "bob@test.com",   2026, "USC002");

        userList.getUsers().add(alice);
        userList.getUsers().add(bob);
    }

    // =========================================================================
    // SECTION 1 — NULL FIELD VALIDATION (all FAIL — nulls are accepted freely)
    // =========================================================================

    /** BUG: null username is accepted with no error. FAILS — assertNotNull on null value. */
    @Test
    public void testUsernameCannotBeNull() {
        User u = new User("First", "Last", null, "pass123", "email@test.com", 2025, "USC001");
        assertNotNull(u.getUsername()); // FAILS — null stored
    }

    /** BUG: null password is accepted with no error. FAILS. */
    @Test
    public void testPasswordCannotBeNull() {
        User u = new User("First", "Last", "user1", null, "email@test.com", 2025, "USC001");
        assertNotNull(u.getPassword()); // FAILS — null stored
    }

    /** BUG: null first name is accepted. FAILS. */
    @Test
    public void testFirstNameCannotBeNull() {
        User u = new User(null, "Last", "user1", "pass123", "email@test.com", 2025, "USC001");
        assertNotNull(u.getFirstName()); // FAILS — null stored
    }

    /** BUG: null last name is accepted. FAILS. */
    @Test
    public void testLastNameCannotBeNull() {
        User u = new User("First", null, "user1", "pass123", "email@test.com", 2025, "USC001");
        assertNotNull(u.getLastName()); // FAILS — null stored
    }

    /** BUG: null email is accepted. FAILS. */
    @Test
    public void testEmailCannotBeNull() {
        User u = new User("First", "Last", "user1", "pass123", null, 2025, "USC001");
        assertNotNull(u.getEmail()); // FAILS — null stored
    }

    /** BUG: null USC ID is accepted. FAILS. */
    @Test
    public void testIdUSCCannotBeNull() {
        User u = new User("First", "Last", "user1", "pass123", "email@test.com", 2025, null);
        assertNotNull(u.getIdUSC()); // FAILS — null stored
    }

    // =========================================================================
    // SECTION 2 — EMPTY STRING VALIDATION (all FAIL — empty strings accepted)
    // =========================================================================

    /** BUG: empty username is accepted. FAILS. */
    @Test
    public void testUsernameCannotBeEmpty() {
        User u = new User("First", "Last", "", "pass123", "email@test.com", 2025, "USC001");
        assertFalse(u.getUsername().isEmpty()); // FAILS — empty string stored
    }

    /** BUG: empty password is accepted. FAILS. */
    @Test
    public void testPasswordCannotBeEmpty() {
        User u = new User("First", "Last", "user1", "", "email@test.com", 2025, "USC001");
        assertFalse(u.getPassword().isEmpty()); // FAILS — empty string stored
    }

    /** BUG: empty first name is accepted. FAILS. */
    @Test
    public void testFirstNameCannotBeEmpty() {
        User u = new User("", "Last", "user1", "pass123", "email@test.com", 2025, "USC001");
        assertFalse(u.getFirstName().isEmpty()); // FAILS — empty string stored
    }

    /** BUG: empty last name is accepted. FAILS. */
    @Test
    public void testLastNameCannotBeEmpty() {
        User u = new User("First", "", "user1", "pass123", "email@test.com", 2025, "USC001");
        assertFalse(u.getLastName().isEmpty()); // FAILS — empty string stored
    }

    /** BUG: a password that is only spaces is accepted. FAILS. */
    @Test
    public void testPasswordCannotBeOnlySpaces() {
        User u = new User("First", "Last", "user1", "   ", "email@test.com", 2025, "USC001");
        assertFalse(u.getPassword().trim().isEmpty()); // FAILS — "   " stored
    }

    /** BUG: a username that is only spaces is accepted. FAILS. */
    @Test
    public void testUsernameCannotBeOnlySpaces() {
        User u = new User("First", "Last", "   ", "pass123", "email@test.com", 2025, "USC001");
        assertFalse(u.getUsername().trim().isEmpty()); // FAILS — "   " stored
    }

    // =========================================================================
// SECTION 3 — PASSWORD STRENGTH (FAILS — no length, uppercase, or special character enforced)
// =========================================================================

/** BUG: single-character password is accepted. FAILS. */
@Test
public void testPasswordTooShort() {
    User u = new User("First", "Last", "user1", "a", "email@test.com", 2025, "USC001");
    assertTrue(u.getPassword().length() >= 7); // FAILS — "a" (length 1) accepted
}

/** BUG: password with no uppercase letter is accepted. FAILS. */
@Test
public void testPasswordMustHaveUppercase() {
    User u = new User("First", "Last", "user1", "abcdef!", "email@test.com", 2025, "USC001");
    assertTrue(u.getPassword().chars().anyMatch(Character::isUpperCase)); // FAILS — no uppercase
}

/** BUG: password with no special character is accepted. FAILS. */
@Test
public void testPasswordMustHaveSpecialCharacter() {
    User u = new User("First", "Last", "user1", "Abcdefg", "email@test.com", 2025, "USC001");
    assertTrue(u.getPassword().chars().anyMatch(c -> !Character.isLetterOrDigit(c))); // FAILS — no special char
}

/** BUG: password with uppercase and special character but under 7 chars is accepted. FAILS. */
@Test
public void testPasswordMustMeetAllRequirements() {
    User u = new User("First", "Last", "user1", "A!", "email@test.com", 2025, "USC001");
    assertTrue(u.getPassword().length() >= 7); // FAILS — "A!" (length 2) accepted
}

    // =========================================================================
    // SECTION 4 — EMAIL FORMAT VALIDATION (FAILS — no format check exists)
    // =========================================================================

    /** BUG: email with no @ symbol is accepted. FAILS. */
    @Test
    public void testEmailMustContainAtSymbol() {
        User u = new User("First", "Last", "user1", "pass123", "notanemail", 2025, "USC001");
        assertTrue(u.getEmail().contains("@")); // FAILS — "notanemail" accepted
    }

    /** BUG: email with no domain after @ is accepted. FAILS. */
    @Test
    public void testEmailMustHaveDomainAfterAt() {
        User u = new User("First", "Last", "user1", "pass123", "user@", 2025, "USC001");
        assertTrue(u.getEmail().indexOf("@") < u.getEmail().length() - 1); // FAILS — "user@" accepted
    }

    /** BUG: a single @ with nothing else is accepted as a valid email. FAILS. */
    @Test
    public void testEmailCannotBeJustAtSymbol() {
        User u = new User("First", "Last", "user1", "pass123", "@", 2025, "USC001");
        assertTrue(u.getEmail().length() > 3); // FAILS — "@" (length 1) accepted
    }

    // =========================================================================
// SECTION 5 — GRADUATION YEAR VALIDATION (FAILS — any int accepted)
// =========================================================================

/** BUG: negative graduation year is accepted. FAILS. */
@Test
public void testGraduationYearCannotBeNegative() {
    User u = new User("First", "Last", "user1", "pass123", "email@test.com", -1, "USC001");
    assertTrue(u.getGraduationYear() >= 2000); // FAILS — -1 stored
}

/** BUG: graduation year 0 is accepted. FAILS. */
@Test
public void testGraduationYearCannotBeZero() {
    User u = new User("First", "Last", "user1", "pass123", "email@test.com", 0, "USC001");
    assertTrue(u.getGraduationYear() >= 2000); // FAILS — 0 stored
}

/** BUG: graduation year not starting with 20 is accepted. FAILS. */
@Test
public void testGraduationYearMustStartWith20() {
    User u = new User("First", "Last", "user1", "pass123", "email@test.com", 1999, "USC001");
    assertTrue(u.getGraduationYear() >= 2000 && u.getGraduationYear() <= 2099); // FAILS — 1999 stored
}


    // =========================================================================
    // SECTION 6 — SETTER VALIDATION (FAILS — setters accept anything)
    // =========================================================================

    /** BUG: setUsername(null) is accepted. FAILS. */
    @Test
    public void testSetUsernameCannotBeNull() {
        alice.setUsername(null);
        assertNotNull(alice.getUsername()); // FAILS — null stored
    }

    /** BUG: setPassword(null) is accepted. FAILS. */
    @Test
    public void testSetPasswordCannotBeNull() {
        alice.setPassword(null);
        assertNotNull(alice.getPassword()); // FAILS — null stored
    }

    /** BUG: setUsername("") is accepted. FAILS. */
    @Test
    public void testSetUsernameCannotBeEmpty() {
        alice.setUsername("");
        assertFalse(alice.getUsername().isEmpty()); // FAILS — "" stored
    }

    /** BUG: setPassword("") is accepted. FAILS. */
    @Test
    public void testSetPasswordCannotBeEmpty() {
        alice.setPassword("");
        assertFalse(alice.getPassword().isEmpty()); // FAILS — "" stored
    }

    /** BUG: setEmail with no @ is accepted. FAILS. */
    @Test
    public void testSetEmailMustBeValid() {
        alice.setEmail("bademail");
        assertTrue(alice.getEmail().contains("@")); // FAILS — "bademail" stored
    }

    // =========================================================================
    // SECTION 7 — NULL DATA CAUSES NPE CRASHES IN USERLIST
    // These tests FAIL with a NullPointerException — the code crashes instead
    // of handling bad data gracefully.
    // =========================================================================

    /**
     * BUG: a User with null username added to the list causes a crash.
     * getUser(String) calls user.getUsername().equals(...) — null.equals() → NPE.
     * FAILS with NullPointerException.
     */
    @Test
    public void testNullUsernameInListDoesNotCrashGetUserByUsername() {
        User nullUser = new User("Ghost", "G", null, "pass123", "g@test.com", 2025, "USC003");
        userList.getUsers().add(nullUser);
        User found = userList.getUser("alice"); // FAILS — NPE: null.equals("alice")
        assertNull(found);
    }

    /**
     * BUG: a User with null username in the list causes getUser(username, password) to crash.
     * isMatch() calls this.username.equals(username) → null.equals(...) → NPE.
     * FAILS with NullPointerException.
     */
    @Test
    public void testNullUsernameInListDoesNotCrashGetUserWithCredentials() {
        User nullUser = new User("Ghost", "G", null, "pass123", "g@test.com", 2025, "USC003");
        userList.getUsers().add(nullUser);
        User found = userList.getUser("alice", "pass1"); // FAILS — NPE
        assertNull(found);
    }

    /**
     * BUG: a User with null password in the list causes getUser(username, password) to crash.
     * isMatch() calls this.password.equals(password) → null.equals(...) → NPE.
     * FAILS with NullPointerException.
     */
    @Test
    public void testNullPasswordInListDoesNotCrashGetUserWithCredentials() {
        User nullPassUser = new User("Ghost", "G", "ghost", null, "g@test.com", 2025, "USC003");
        userList.getUsers().add(nullPassUser);
        User found = userList.getUser("ghost", "pass1"); // FAILS — NPE
        assertNull(found);
    }

    /**
     * BUG: calling isMatch() directly on a user with null username crashes.
     * FAILS with NullPointerException.
     */
    @Test
    public void testIsMatchWithNullUsernameDoesNotCrash() {
        User nullUser = new User("Ghost", "G", null, "pass123", "g@test.com", 2025, "USC003");
        boolean result = nullUser.isMatch("ghost", "pass123"); // FAILS — NPE
        assertFalse(result);
    }

    /**
     * BUG: calling isMatch() on a user with null password crashes.
     * FAILS with NullPointerException.
     */
    @Test
    public void testIsMatchWithNullPasswordDoesNotCrash() {
        User nullPassUser = new User("Ghost", "G", "ghost", null, "g@test.com", 2025, "USC003");
        boolean result = nullPassUser.isMatch("ghost", "somepass"); // FAILS — NPE
        assertFalse(result);
    }

    // =========================================================================
    // SECTION 8 — MISSING addUser() / DUPLICATE PREVENTION
    // =========================================================================

    /**
 * BUG: two users with the same USC ID can both be added.
 * There is no uniqueness check on USC ID.
 * FAILS — count of users with USC ID "USC001" is 2, not 1.
 */
@Test
public void testDuplicateUSCIdIsNotAllowed() {
    User alice2 = new User("Alice2", "B", "alice2", "otherpass", "alice2@test.com", 2025, "USC001");
    userList.getUsers().add(alice2);
    long count = userList.getUsers().stream()
            .filter(u -> "USC001".equals(u.getIdUSC()))
            .count();
    assertEquals(1, count); // FAILS — count is 2
}

/**
 * BUG: two users with the same email can both be added.
 * There is no uniqueness check on email.
 * FAILS — count of users with email "alice@test.com" is 2, not 1.
 */
@Test
public void testDuplicateEmailIsNotAllowed() {
    User alice2 = new User("Alice2", "B", "alice2", "otherpass", "alice@test.com", 2025, "USC003");
    userList.getUsers().add(alice2);
    long count = userList.getUsers().stream()
            .filter(u -> "alice@test.com".equals(u.getEmail()))
            .count();
    assertEquals(1, count); // FAILS — count is 2
}
    /**
     * BUG: there is no addUser() method on UserList.
     * The only way to add a user is to reach into getUsers() directly.
     * This means the same user object can be added multiple times — no dedup check.
     * FAILS — actual size after duplicate add is 3, not 2.
     */
    @Test
    public void testSameUserCannotBeAddedTwice() {
        userList.getUsers().add(alice); // alice is already in list
        assertEquals(2, userList.getUsers().size()); // FAILS — size is 3
    }

    /**
     * BUG: two different User objects with the same username can both be added.
     * There is no uniqueness check on username.
     * FAILS — count of users named "alice" is 2, not 1.
     */
    @Test
    public void testDuplicateUsernameIsNotAllowed() {
        User alice2 = new User("Alice2", "B", "alice", "otherpass", "alice2@test.com", 2025, "USC003");
        userList.getUsers().add(alice2);
        long count = userList.getUsers().stream()
                .filter(u -> "alice".equals(u.getUsername()))
                .count();
        assertEquals(1, count); // FAILS — count is 2
    }

    // =========================================================================
    // SECTION 9 — editUser() BUGS
    // =========================================================================

    /**
     * BUG: editUser() silently does nothing when the user is not in the list.
     * No exception is thrown, no return value indicates failure.
     * FAILS — firstName stays "Ghost", expected "UpdatedName".
     */
    @Test
    public void testEditUserNotInListShouldUpdate() {
        User notInList = new User("Ghost", "G", "ghost", "pw", "g@test.com", 2024, "USC000");
        userList.editUser(notInList, "UpdatedName", "G", "ghost", "pw", "g@test.com", 2024, "USC000");
        assertEquals("UpdatedName", notInList.getFirstName()); // FAILS — still "Ghost"
    }

    /**
     * BUG: editUser() can set username to null, poisoning future lookups.
     * FAILS — after edit, alice.getUsername() is null (assertNotNull fails).
     */
    @Test
    public void testEditUserCannotSetUsernameToNull() {
        userList.editUser(alice, "Alice", "A", null, "pass1", "alice@test.com", 2025, "USC001");
        assertNotNull(alice.getUsername()); // FAILS — null stored
    }

    /**
     * BUG: editUser() can set password to null.
     * FAILS — after edit, alice.getPassword() is null.
     */
    @Test
    public void testEditUserCannotSetPasswordToNull() {
        userList.editUser(alice, "Alice", "A", "alice", null, "alice@test.com", 2025, "USC001");
        assertNotNull(alice.getPassword()); // FAILS — null stored
    }

    /**
     * BUG: editUser() can set username to empty string.
     * FAILS — after edit, alice.getUsername() is "".
     */
    @Test
    public void testEditUserCannotSetUsernameToEmpty() {
        userList.editUser(alice, "Alice", "A", "", "pass1", "alice@test.com", 2025, "USC001");
        assertFalse(alice.getUsername().isEmpty()); // FAILS — "" stored
    }

    /**
     * BUG: editUser() can create a duplicate username.
     * After editing alice's username to "bob", two users share the same username.
     * FAILS — expected exactly 1 user named "bob", but there are 2.
     */
    @Test
    public void testEditUserCannotCreateDuplicateUsername() {
        userList.editUser(alice, "Alice", "A", "bob", "pass1", "alice@test.com", 2025, "USC001");
        long count = userList.getUsers().stream()
                .filter(u -> "bob".equals(u.getUsername()))
                .count();
        assertEquals(1, count); // FAILS — count is 2
    }

    // =========================================================================
    // SECTION 10 — deleteUser() / reference equality BUG
    // =========================================================================

    /**
     * BUG: deleteUser() uses ArrayList.remove() with reference equality.
     * A reconstructed User with the same UUID is treated as a different object,
     * so nothing is removed from the list.
     * FAILS — alice is still in the list after deleteUser(aliceCopy).
     */
    @Test
    public void testDeleteUserByEquivalentObjectShouldRemoveUser() {
        User aliceCopy = new User(alice.getId(), "Alice", "A", "alice", "pass1",
                "alice@test.com", new java.util.ArrayList<>(), new java.util.ArrayList<>(),
                new java.util.ArrayList<>(), Status.USER, 2025, "USC001");
        userList.deleteUser(aliceCopy);
        assertFalse(userList.getUsers().contains(alice)); // FAILS — alice still present
    }

    // =========================================================================
    // SECTION 11 — PASSING TESTS (correct behavior that already works)
    // =========================================================================

    /** PASSES */
    @Test
    public void testGetUserByIDReturnsCorrectUser() {
        assertTrue(userList.getUserByID(alice.getId()) == alice);
    }

    /** PASSES */
    @Test
    public void testGetUserByIDUnknownReturnsNull() {
        assertNull(userList.getUserByID(UUID.randomUUID()));
    }

    /** PASSES */
    @Test
    public void testGetUserCorrectCredentials() {
        assertTrue(userList.getUser("alice", "pass1") == alice);
    }

    /** PASSES */
    @Test
    public void testGetUserWrongPasswordReturnsNull() {
        assertNull(userList.getUser("alice", "wrongpass"));
    }

    /** PASSES */
    @Test
    public void testGetUserByUsernameReturnsCorrectUser() {
        assertTrue(userList.getUser("bob") == bob);
    }

    /** PASSES */
    @Test
    public void testGetUserByUsernameUnknownReturnsNull() {
        assertNull(userList.getUser("ghost"));
    }

    /** PASSES */
    @Test
    public void testDeleteUserRemovesFromList() {
        userList.deleteUser(alice);
        assertFalse(userList.getUsers().contains(alice));
    }

    /** PASSES */
    @Test
    public void testEditUserUpdatesFields() {
        userList.editUser(alice, "Ally", "Z", "ally", "newpass", "ally@test.com", 2030, "USC999");
        assertTrue(alice.getFirstName().equals("Ally"));
        assertTrue(alice.getUsername().equals("ally"));
    }
}
