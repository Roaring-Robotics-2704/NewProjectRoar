// Copyright 2021-2026 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.drive;

import com.revrobotics.REVLibError;
import com.revrobotics.spark.SparkBase;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.DoubleSupplier;

/**
 * Provides an interface for asynchronously reading high-frequency measurements
 * to a set of queues.
 *
 * <p>Spark signals are sampled from a dedicated Notifier thread. If any Spark
 * measurement in a sample reports a REVLib error, the entire sample is
 * discarded so that all registered queues remain time-aligned.
 *
 * <p>Generic signals are included in the same synchronized sample. If a
 * generic signal throws an exception or produces a non-finite value, the
 * entire sample is discarded.
 */
public final class SparkOdometryThread {
    private static final int QUEUE_CAPACITY = 20;

    private static final SparkOdometryThread INSTANCE =
            new SparkOdometryThread();

    private final List<SparkBase> sparks = new ArrayList<>();
    private final List<DoubleSupplier> sparkSignals = new ArrayList<>();
    private final List<DoubleSupplier> genericSignals = new ArrayList<>();

    private final List<Queue<Double>> sparkQueues = new ArrayList<>();
    private final List<Queue<Double>> genericQueues = new ArrayList<>();
    private final List<Queue<Double>> timestampQueues = new ArrayList<>();

    private final ReentrantLock odometryLock = new ReentrantLock();

    private final Notifier notifier = new Notifier(this::run);

    private boolean started = false;

    /**
     * Returns the global odometry thread instance.
     */
    public static SparkOdometryThread getInstance() {
        return INSTANCE;
    }

    private SparkOdometryThread() {
        notifier.setName("OdometryThread");
    }

    /**
     * Starts the odometry sampling thread.
     *
     * <p>This method is safe to call multiple times. Only the first call
     * actually starts the Notifier.
     */
    public void start() {
        odometryLock.lock();

        try {
            if (started) {
                return;
            }

            if (timestampQueues.isEmpty()) {
                return;
            }

            double frequency = DriveConstants.odometryFrequency;

            if (!Double.isFinite(frequency) || frequency <= 0.0) {
                throw new IllegalStateException(
                        "DriveConstants.odometryFrequency must be finite and > 0");
            }

            started = true;

            notifier.startPeriodic(1.0 / frequency);
        } finally {
            odometryLock.unlock();
        }
    }

    /**
     * Stops the odometry sampling thread.
     *
     * <p>The thread can be restarted later with {@link #start()}.
     */
    public void stop() {
        odometryLock.lock();

        try {
            if (!started) {
                return;
            }

            notifier.stop();
            started = false;
        } finally {
            odometryLock.unlock();
        }
    }

    /**
     * Registers a Spark signal to be read from the odometry thread.
     *
     * @param spark Spark device which owns the signal
     * @param signal Signal supplier
     * @return Queue containing sampled values
     */
    public Queue<Double> registerSignal(
            SparkBase spark,
            DoubleSupplier signal) {

        if (spark == null) {
            throw new NullPointerException("spark cannot be null");
        }

        if (signal == null) {
            throw new NullPointerException("signal cannot be null");
        }

        Queue<Double> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        odometryLock.lock();

        try {
            sparks.add(spark);
            sparkSignals.add(signal);
            sparkQueues.add(queue);
        } finally {
            odometryLock.unlock();
        }

        return queue;
    }

    /**
     * Registers a generic signal to be read from the odometry thread.
     *
     * @param signal Signal supplier
     * @return Queue containing sampled values
     */
    public Queue<Double> registerSignal(DoubleSupplier signal) {
        if (signal == null) {
            throw new NullPointerException("signal cannot be null");
        }

        Queue<Double> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        odometryLock.lock();

        try {
            genericSignals.add(signal);
            genericQueues.add(queue);
        } finally {
            odometryLock.unlock();
        }

        return queue;
    }

    /**
     * Creates a queue which receives the timestamp associated with every valid
     * odometry sample.
     *
     * @return Timestamp queue
     */
    public Queue<Double> makeTimestampQueue() {
        Queue<Double> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        odometryLock.lock();

        try {
            timestampQueues.add(queue);
        } finally {
            odometryLock.unlock();
        }

        return queue;
    }

    /**
     * Samples all registered signals.
     *
     * <p>A sample is considered valid only if:
     *
     * <ul>
     *   <li>Every Spark signal successfully returns a value.
     *   <li>Every Spark signal reports {@link REVLibError#kOk}.
     *   <li>Every generic signal successfully returns a value.
     *   <li>Every value is finite.
     *   <li>All queues have room for the complete sample.
     * </ul>
     *
     * <p>If any condition fails, no queue receives a value for that sample.
     */
    private void run() {
        odometryLock.lock();

        try {
            final double timestamp = Timer.getFPGATimestamp();

            final double[] sparkValues =
                    new double[sparkSignals.size()];

            final double[] genericValues =
                    new double[genericSignals.size()];

            // -------------------------------------------------------------
            // Read all Spark signals.
            //
            // IMPORTANT:
            // REVLib documents getLastError() as thread-local and intended
            // to be called immediately after the operation that could
            // generate the error.
            // -------------------------------------------------------------

            for (int i = 0; i < sparkSignals.size(); i++) {
                try {
                    sparkValues[i] =
                            sparkSignals.get(i).getAsDouble();

                    REVLibError error =
                            sparks.get(i).getLastError();

                    if (error != REVLibError.kOk) {
                        return;
                    }

                    if (!Double.isFinite(sparkValues[i])) {
                        return;
                    }
                } catch (RuntimeException exception) {
                    // A bad signal must not kill the Notifier callback.
                    return;
                }
            }

            // -------------------------------------------------------------
            // Read all generic signals.
            // -------------------------------------------------------------

            for (int i = 0; i < genericSignals.size(); i++) {
                try {
                    genericValues[i] =
                            genericSignals.get(i).getAsDouble();

                    if (!Double.isFinite(genericValues[i])) {
                        return;
                    }
                } catch (RuntimeException exception) {
                    // Do not publish a partially valid sample.
                    return;
                }
            }

            // -------------------------------------------------------------
            // Verify that every queue has room before adding anything.
            //
            // This is important because ArrayBlockingQueue.offer() can fail
            // silently when the queue is full.
            // -------------------------------------------------------------

            for (Queue<Double> queue : sparkQueues) {
                if (queue.size() >= QUEUE_CAPACITY) {
                    return;
                }
            }

            for (Queue<Double> queue : genericQueues) {
                if (queue.size() >= QUEUE_CAPACITY) {
                    return;
                }
            }

            for (Queue<Double> queue : timestampQueues) {
                if (queue.size() >= QUEUE_CAPACITY) {
                    return;
                }
            }

            // -------------------------------------------------------------
            // Publish the complete sample.
            //
            // Everything above this point is validation. Once we get here,
            // every registered queue receives exactly one value.
            // -------------------------------------------------------------

            for (int i = 0; i < sparkQueues.size(); i++) {
                sparkQueues.get(i).offer(sparkValues[i]);
            }

            for (int i = 0; i < genericQueues.size(); i++) {
                genericQueues.get(i).offer(genericValues[i]);
            }

            for (int i = 0; i < timestampQueues.size(); i++) {
                timestampQueues.get(i).offer(timestamp);
            }
        } finally {
            odometryLock.unlock();
        }
    }
}