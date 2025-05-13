package com.waxjx.largescale.Config;

public class ReadWriteType {
    private static final ThreadLocal<Boolean> READ_ONLY = ThreadLocal.withInitial(() -> false);

    public static void markRead() {
        READ_ONLY.set(true);
    }
    public static void markWrite() {
        READ_ONLY.set(false);
    }
    public static boolean isRead() {
        return READ_ONLY.get();
    }
    public static void clear() {
        READ_ONLY.remove();
    }
} 