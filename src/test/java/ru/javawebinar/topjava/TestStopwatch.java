package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

public class TestStopwatch extends Stopwatch {
    private static final StringBuilder logInfoBuilder = new StringBuilder();

    public static String getLogInfo() {
        return logInfoBuilder.toString();
    }

    protected void finished(long nanos, Description description) {
        logInfoBuilder.append("\n")
                .append(description.getMethodName())
                .append(" finished, time taken ")
                .append(TimeUnit.NANOSECONDS.toMillis(nanos))
                .append(" millisecond");
    }
}
