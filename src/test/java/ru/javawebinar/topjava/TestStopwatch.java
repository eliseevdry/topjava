package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestStopwatch extends Stopwatch {
    private static final Map<String, Long> logInfoMap = new HashMap<>();

    public static Map<String, Long> getLogInfoMap() {
        return logInfoMap;
    }

    protected void finished(long nanos, Description description) {
        logInfoMap.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }
}
