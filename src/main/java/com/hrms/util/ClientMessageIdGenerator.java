package com.hrms.util;

import java.util.UUID;

public final class ClientMessageIdGenerator {
    private ClientMessageIdGenerator() {}

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
