package com.company.properties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Properties {

    public static final String host = "localhost";
    public static final int serverPort = 6001;
    public static final int serverMessageDelayMillis = 500;
    public static final int serverLoopDelayMillis = 500;
    public static final int clientLoopDelayMillis = 500;
    public static final boolean isBlocking = false;
    public static final int clientThreads = 1;

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final int BYTE_SIZE = 2048*128;

    public Properties() {
    }
}