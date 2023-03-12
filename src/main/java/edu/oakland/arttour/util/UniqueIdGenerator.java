package edu.oakland.arttour.util;

import java.util.UUID;

public class UniqueIdGenerator {

    public static String generateUniqueId(String prefix) {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Concatenate the prefix and UUID
        String id = prefix + uuid.toString();

        // Truncate the ID to 50 characters
        id = id.substring(0, Math.min(id.length(), 50));

        return id;
    }
}
