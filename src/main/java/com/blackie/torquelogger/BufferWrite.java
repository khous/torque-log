package com.blackie.torquelogger;

import com.blackie.torquelogger.data.item.LogItem;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Cache incoming log records. Flush them to the database on an interval and a threshold.
 * Every five seconds maybe, write to DB
 * Every 2k Records, write to database
 * Batched writes to the database are quicker than single writes
 */
public class BufferWrite {
    private static BufferWrite ourInstance = new BufferWrite();
private final ConcurrentLinkedQueue<LogItem> logBuffer;

    public static BufferWrite getInstance() {
        return ourInstance;
    }

    private BufferWrite() {
        logBuffer = new ConcurrentLinkedQueue<>();
        Timer t =  new Timer();
        t.schedule(new FlusherBitch(), 0, 5000);
    }

    private class FlusherBitch extends TimerTask {

        @Override
        public void run() {

        }
    }
}
