package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class TestStopwatch extends Stopwatch {
    private static final Logger log = getLogger(TestStopwatch.class);

    protected void finished(long nanos, Description description) {
        String msg = description.getMethodName() == null ? description.getClassName() : description.getMethodName();
        log.info("{} finished, time taken {} millisecond", msg, TimeUnit.NANOSECONDS.toMillis(nanos));
    }
}
