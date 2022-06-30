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
        String format = String.format("%-40s%-5d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        logInfoBuilder.append("\n")
                .append(format)
                .append("\tms");
    }
}
