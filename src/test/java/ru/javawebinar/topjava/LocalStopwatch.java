package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

public class LocalStopwatch extends Stopwatch {
    protected void succeeded(long nanos, Description description) {
        String msg = description.getMethodName() == null ? description.getClassName() : description.getMethodName();
        System.out.println(msg + " succeeded, time taken " + nanos / 1000000000.0 + " sec");
    }

    /**
     * Invoked when a test fails
     */
    protected void failed(long nanos, Throwable e, Description description) {
        String msg = description.getMethodName() == null ? description.getClassName() : description.getMethodName();
        System.out.println(msg + " failed, time taken " + nanos / 1000000000.0 + " sec");
    }

    /**
     * Invoked when a test is skipped due to a failed assumption.
     */
    protected void skipped(long nanos, AssumptionViolatedException e,
                           Description description) {
        String msg = description.getMethodName() == null ? description.getClassName() : description.getMethodName();
        System.out.println(msg + " skipped, time taken " + nanos / 1000000000.0 + " sec");
    }

    /**
     * Invoked when a test method finishes (whether passing or failing)
     */
    protected void finished(long nanos, Description description) {
        String msg = description.getMethodName() == null ? description.getClassName() : description.getMethodName();
        System.out.println(msg + " finished, time taken " + nanos / 1000000000.0 + " sec");
    }
}
