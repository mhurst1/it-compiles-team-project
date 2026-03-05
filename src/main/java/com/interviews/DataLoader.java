package com.interviews;
// DataLoader.java
// Zero-external-packages DataLoader focused on getQuestions() and required helpers.
// NOTE: This uses only java.* packages and simple parsing logic (not a full JSON parser).
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;
import java.lang.reflect.Field;
import java.util.UUID;

public class DataLoader {
    private final Path usersPath;
    private final Path questionsPath;

    public DataLoader(String usersJsonPath, String questionsJsonPath) {
        this.usersPath = Path.of(usersJsonPath);
        this.questionsPath = Path.of(questionsJsonPath);
    }

    // --------------------------
    // Public API: getQuestions()
    // --------------------------
    /**
     * Read Questions.json, parse it with a tiny builtin parser, and return ArrayList<Question>.
     * Uses reflection to populate Question fields by converting kebab-case JSON keys to camelCase.
     *
     * If the file is missing or parsing fails, returns an empty list.
     */
    public ArrayList<Question> getQuestions() {
        try {
            if (!Files.exists(questionsPath)) {
                System.err.println("Questions file not found: " + questionsPath);
                return new ArrayList<>();
            }

            String json = Files.readString(questionsPath);

            // Step 1: split top-level array into per-object substrings
            List<String> objStrs = splitTopLevelObjects(json);
            ArrayList<Question> result = new ArrayList<>(objStrs.size());

            // Step 2: parse each object string into a map and instantiate a Question
            for (String obj : objStrs) {
                Map<String, Object> kv = parseJsonObject(obj);
                if (kv == null || kv.isEmpty()) continue;
                Question q = instantiateQuestionFromMap(kv);
                if (q != null) result.add(q);
            }

            // Optional: if you want to verify author user objects exist, you can load users:
            // Map<UUID, User> users = loadUsersMap(); and then attach to questions if necessary.

            return result;
        } catch (IOException e) {
            System.err.println("I/O reading questions file: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Unexpected error in getQuestions(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // --------------------------
    // Minimal JSON helpers
    // --------------------------

    /**
     * Splits a top-level JSON array string into individual object substrings.
     * E.g. "[ {..}, {..} ]" -> ["{..}", "{..}"]
     *
     * This scans char-by-char, counting braces to correctly extract objects even if they
     * contain arrays or inner braces.
     */
    private List<String> splitTopLevelObjects(String json) {
        List<String> objects = new ArrayList<>();
        if (json == null) return objects;

        int len = json.length();
        int i = 0;

        // find first '['
        while (i < len && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= len || json.charAt(i) != '[') {
            // not an array — maybe a single object
            String trimmed = json.trim();
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) objects.add(trimmed);
            return objects;
        }
        i++; // skip '['

        // scan for objects
        while (i < len) {
            // skip whitespace and commas
            while (i < len && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ',')) i++;
            if (i >= len) break;
            if (json.charAt(i) == ']') break; // end of array

            if (json.charAt(i) == '{') {
                int start = i;
                int braceDepth = 0;
                boolean inString = false;
                i--;
                while (++i < len) {
                    char c = json.charAt(i);
                    if (c == '"' && json.charAt(i-1) != '\\') {
                        inString = !inString;
                    } else if (!inString) {
                        if (c == '{') braceDepth++;
                        else if (c == '}') {
                            braceDepth--;
                            if (braceDepth == 0) {
                                // include substring from start..i
                                objects.add(json.substring(start, i + 1).trim());
                                break;
                            }
                        }
                    }
                }
            } else {
                // skip unknown token
                i++;
            }
        }
        return objects;
    }

    /**
     * Parse a single JSON object string (starting with '{', ending with '}') into a map.
     * Values can be:
     *  - String => stored as String
     *  - Number/boolean/null => stored as String (raw text)
     *  - Array of strings ["a","b"] => stored as List<String>
     *
     * This is intentionally minimal — it handles strings with backslash escapes and simple string arrays.
     */
    private Map<String, Object> parseJsonObject(String objJson) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (objJson == null) return map;
        String s = objJson.trim();
        if (!s.startsWith("{") || !s.endsWith("}")) return map;

        int len = s.length();
        int i = 1; // skip initial '{'

        while (i < len - 1) {
            // skip whitespace and commas
            while (i < len - 1 && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ',')) i++;
            if (i >= len - 1) break;

            // read key string
            if (s.charAt(i) != '"') {
                // invalid key start; try to recover by skipping to next quote
                int nextQ = s.indexOf('"', i);
                if (nextQ < 0) break;
                i = nextQ;
            }
            String key = readJsonString(s, i);
            if (key == null) break;
            // advance i past the closing quote
            i += 1 + key.length(); // rough — adjust to actual char pos: find next unescaped quote from i+1
            // safer: find the position of the quote we closed at
            int keyEnd = findClosingQuote(s, i - 1);
            if (keyEnd < 0) break;
            i = keyEnd + 1;

            // skip whitespace to colon
            while (i < len && Character.isWhitespace(s.charAt(i))) i++;
            if (i >= len || s.charAt(i) != ':') break;
            i++; // skip ':'
            while (i < len && Character.isWhitespace(s.charAt(i))) i++;
            if (i >= len) break;

            // read value
            char c = s.charAt(i);
            if (c == '"') {
                // string value
                String val = readJsonString(s, i);
                if (val == null) val = "";
                map.put(key, val);
                int valEnd = findClosingQuote(s, i);
                if (valEnd < 0) break;
                i = valEnd + 1;
            } else if (c == '[') {
                // array — we only support array of strings for this minimal parser
                int arrStart = i;
                int arrEnd = findMatchingBracket(s, i, '[', ']');
                if (arrEnd < 0) {
                    // malformed array
                    i++;
                    continue;
                }
                String arrContent = s.substring(arrStart + 1, arrEnd).trim();
                List<String> list = parseStringArray(arrContent);
                map.put(key, list);
                i = arrEnd + 1;
            } else {
                // number, boolean, or null — read until comma or closing brace
                int j = i;
                while (j < len && s.charAt(j) != ',' && s.charAt(j) != '}') j++;
                String raw = s.substring(i, j).trim();
                map.put(key, raw);
                i = j;
            }

            // loop continues; comma/whitespace handled at top
        }
        return map;
    }

    /**
     * Read a JSON string starting at index start which should point to the starting double-quote (").
     * Returns the unescaped string value or null on error.
     */
    private String readJsonString(String s, int startQuoteIndex) {
        if (s == null || startQuoteIndex < 0 || startQuoteIndex >= s.length() || s.charAt(startQuoteIndex) != '"') return null;
        StringBuilder sb = new StringBuilder();
        int i = startQuoteIndex + 1;
        boolean escaped = false;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (escaped) {
                // handle common escapes; keep unhandled escapes as-is
                if (c == '"' || c == '\\' || c == '/') sb.append(c);
                else if (c == 'b') sb.append('\b');
                else if (c == 'f') sb.append('\f');
                else if (c == 'n') sb.append('\n');
                else if (c == 'r') sb.append('\r');
                else if (c == 't') sb.append('\t');
                else sb.append(c);
                escaped = false;
            } else {
                if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    return sb.toString();
                } else {
                    sb.append(c);
                }
            }
            i++;
        }
        // if we reach here, no closing quote found
        return null;
    }

    /**
     * Find index of the closing quote for a string starting at or before startIndex.
     * This returns the index of the closing quote character, or -1 if none found.
     */
    private int findClosingQuote(String s, int startIndex) {
        int i = startIndex;
        if (i < 0) i = 0;
        // ensure we are at a quote: if not, find previous quote
        while (i < s.length() && s.charAt(i) != '"') i++;
        if (i >= s.length()) return -1;
        i++; // move to char after opening quote
        boolean escaped = false;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (escaped) {
                escaped = false;
            } else {
                if (c == '\\') escaped = true;
                else if (c == '"') return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Find matching closing bracket for an opening bracket at index 'start'.
     * Works for brackets like [ ... ] and accounts for nested brackets and strings.
     */
    private int findMatchingBracket(String s, int start, char open, char close) {
        int len = s.length();
        if (start < 0 || start >= len || s.charAt(start) != open) return -1;
        int depth = 0;
        boolean inString = false;
        for (int i = start; i < len; i++) {
            char c = s.charAt(i);
            if (c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            if (inString) continue;
            if (c == open) depth++;
            else if (c == close) {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    /**
     * Parse comma-separated string array content (no outer brackets) into List<String>.
     * Expects items like:  "id1","id2"
     */
    private List<String> parseStringArray(String content) {
        List<String> res = new ArrayList<>();
        if (content == null || content.isEmpty()) return res;
        int len = content.length();
        int i = 0;
        while (i < len) {
            // skip whitespace and commas
            while (i < len && (Character.isWhitespace(content.charAt(i)) || content.charAt(i) == ',')) i++;
            if (i >= len) break;
            if (content.charAt(i) == '"') {
                String val = readJsonString(content, i);
                if (val == null) break;
                res.add(val);
                int end = findClosingQuote(content, i);
                if (end < 0) break;
                i = end + 1;
            } else {
                // non-string array element: read until comma
                int j = i;
                while (j < len && content.charAt(j) != ',') j++;
                String raw = content.substring(i, j).trim();
                if (!raw.isEmpty()) res.add(raw);
                i = j + 1;
            }
        }
        return res;
    }

    // --------------------------
    // Object creation helpers
    // --------------------------

    /**
     * Instantiate a Question and set fields by mapping keys -> fields.
     * Key mapping: "kebab-case" -> camelCase. Example: "author-id" -> "authorId".
     * For values that look like UUIDs we convert to java.util.UUID if the corresponding field type is UUID.
     *
     * This uses reflection so it can work with group project's existing Question class without requiring a specific constructor.
     */
    private Question instantiateQuestionFromMap(Map<String, Object> kv) {
        try {
            Class<?> qClass = Question.class; // assumes Question is in same package or imported
            Object qObj = qClass.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, Object> e : kv.entrySet()) {
                String jsonKey = e.getKey();
                Object value = e.getValue();
                String fieldName = jsonKeyToFieldName(jsonKey);

                try {
                    Field f = getFieldRecursive(qClass, fieldName);
                    if (f == null) continue;
                    f.setAccessible(true);

                    Class<?> fieldType = f.getType();

                    // handle common types: String, UUID, List<String>, primitives
                    if (fieldType == String.class) {
                        f.set(qObj, value == null ? null : value.toString());
                    } else if (fieldType == UUID.class) {
                        UUID uid = parsePossibleUUID(value);
                        f.set(qObj, uid);
                    } else if (List.class.isAssignableFrom(fieldType)) {
                        // We assume field is List<String>. If it's typed differently, caller must adapt.
                        if (value instanceof List) {
                            f.set(qObj, value);
                        } else if (value != null) {
                            // single scalar -> wrap into list
                            List<Object> one = new ArrayList<>();
                            one.add(value);
                            f.set(qObj, one);
                        }
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        if (value != null) {
                            try {
                                int iv = Integer.parseInt(value.toString());
                                f.set(qObj, iv);
                            } catch (NumberFormatException ex) {
                                // ignore or leave default
                            }
                        }
                    } else {
                        // fallback: attempt to set string representation for unknown types
                        // if the field expects a UUID but is declared as Object/String elsewhere, above handles UUID
                        try {
                            f.set(qObj, value);
                        } catch (IllegalArgumentException iae) {
                            // ignore incompatible assignment
                        }
                    }
                } catch (Exception inner) {
                    // tolerate missing fields / incompatible types — log and continue
                    System.err.println("Warning: couldn't set field for key '" + jsonKey + "': " + inner.getMessage());
                }
            }

            return (Question) qObj;
        } catch (Exception ex) {
            System.err.println("Failed to instantiate Question: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Find field on class or its superclasses by name; returns null if not found.
     */
    private Field getFieldRecursive(Class<?> cls, String fieldName) {
        Class<?> cur = cls;
        while (cur != null) {
            try {
                Field f = cur.getDeclaredField(fieldName);
                return f;
            } catch (NoSuchFieldException nsf) {
                cur = cur.getSuperclass();
            }
        }
        return null;
    }

    private UUID parsePossibleUUID(Object value) {
        if (value == null) return null;
        String s = value.toString();
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Convert kebab-case JSON key to camelCase Java field name.
     * Example: "first-name" -> "firstName", "author-id" -> "authorId"
     */
    private String jsonKeyToFieldName(String key) {
        if (key == null) return null;
        String[] parts = key.split("-");
        if (parts.length == 1) return parts[0];
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String p = parts[i];
            if (p.length() == 0) continue;
            sb.append(Character.toUpperCase(p.charAt(0)));
            if (p.length() > 1) sb.append(p.substring(1));
        }
        return sb.toString();
    }

    // --------------------------
    // Users loader (same minimal parser)
    // --------------------------
    /**
     * Loads Users.json and returns Map<UUID, User>. Uses the same parser as above.
     * Useful for resolving authors if you want to attach full User objects to Questions.
     *
     * Assumes there is a User class in your project with fields matching camelCase names
     * (id, firstName, lastName, username, email, graduationYear, idUsc, starredQuestions, etc).
     */
    private Map<UUID, User> loadUsersMap() {
        try {
            if (!Files.exists(usersPath)) {
                System.err.println("Users file not found: " + usersPath);
                return Collections.emptyMap();
            }
            String json = Files.readString(usersPath);
            List<String> objStrs = splitTopLevelObjects(json);
            Map<UUID, User> map = new HashMap<>();
            for (String o : objStrs) {
                Map<String, Object> kv = parseJsonObject(o);
                if (kv == null || kv.isEmpty()) continue;
                // instantiate User via reflection similarly to questions
                try {
                    Class<?> uClass = User.class;
                    Object uObj = uClass.getDeclaredConstructor().newInstance();
                    for (Map.Entry<String, Object> en : kv.entrySet()) {
                        String fieldName = jsonKeyToFieldName(en.getKey());
                        Field f = getFieldRecursive(uClass, fieldName);
                        if (f == null) continue;
                        f.setAccessible(true);
                        if (f.getType() == UUID.class) {
                            f.set(uObj, parsePossibleUUID(en.getValue()));
                        } else if (f.getType() == int.class || f.getType() == Integer.class) {
                            try { f.set(uObj, Integer.parseInt(en.getValue().toString())); } catch (Exception ex) {}
                        } else if (List.class.isAssignableFrom(f.getType())) {
                            f.set(uObj, en.getValue());
                        } else {
                            f.set(uObj, en.getValue() == null ? null : en.getValue().toString());
                        }
                    }
                    // add to map by id if present
                    Field idField = getFieldRecursive(uClass, "id");
                    if (idField != null) {
                        idField.setAccessible(true);
                        Object idVal = idField.get(uObj);
                        if (idVal instanceof UUID) map.put((UUID) idVal, (User) uObj);
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to instantiate User: " + ex.getMessage());
                }
            }
            return map;
        } catch (IOException e) {
            System.err.println("I/O reading users file: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    // --------------------------
    // Quick self-test main
    // --------------------------
    public static void main(String[] args) {
        DataLoader d = new DataLoader("data/Users.json", "data/Questions.json");
        ArrayList<Question> qs = d.getQuestions();
        System.out.println("Loaded questions: " + qs.size());
        if (!qs.isEmpty()) System.out.println(qs.get(0).toString());
    }
}