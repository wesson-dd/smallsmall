package com.small.util;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wesson
 * Create at 2021/12/16 22:50 周四
 */
public class MyThreadInfo extends AbstractTableModel {
    private final Object dataLock;
    private int rowCount;
    private Object[][] cellData;
    private Object[][] pendingCellData;
    // the column information remains constant
    private final int columnCount;
    private final String[] columnName;
    private final Class[] columnClass;
    // self-running object control variables
    private Thread internalThread;
    private volatile boolean noStopRequested;

    public MyThreadInfo() {
        rowCount = 0;
        cellData = new Object[0][0];
        // JTable uses this information for the column headers
        columnName = new String[]{
                "Priority", "Alive",
                "Daemon", "Interrupted",
                "ThreadGroup", "Thread Name"};

        // JTable uses this information for cell rendering
        columnClass = new Class[]{
                Integer.class, Boolean.class,
                Boolean.class, Boolean.class,
                String.class, String.class};
        columnCount = columnName.length;
        // used to control concurrent access
        dataLock = new Object();
        noStopRequested = true;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    runWork();
                } catch (Exception x) {
                    // in case ANY exception slips through
                    x.printStackTrace();
                }
            }
        };
        internalThread = new Thread(r, "ThreadViewer");
        internalThread.setPriority(Thread.MAX_PRIORITY - 2);
        internalThread.setDaemon(true);
        internalThread.start();
    }

    private void runWork() {
        // The run() method of transferPending is called by
        // the event handling thread for safe concurrency.
        Runnable transferPending = new Runnable() {
            @Override
            public void run() {
                transferPendingCellData();
                // Method of AbstractTableModel that
                // causes the table to be updated.
                fireTableDataChanged();
            }
        };
        while (noStopRequested) {
            try {
                createPendingCellData();
                SwingUtilities.invokeAndWait(transferPending);
                Thread.sleep(2000);
            } catch (InvocationTargetException tx) {
                tx.printStackTrace();
                stopRequest();
            } catch (InterruptedException x) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopRequest() {
        noStopRequested = false;
        internalThread.interrupt();
    }

    public boolean isAlive() {
        return internalThread.isAlive();
    }

    private void createPendingCellData() {
        // this method is called by the internal thread
        Thread[] thread = findAllThreads();
        Object[][] cell = new Object[thread.length][columnCount];
        for (int i = 0; i < thread.length; i++) {
            Thread t = thread[i];
            Object[] rowCell = cell[i];
            rowCell[0] = t.getPriority();
            rowCell[1] = t.isAlive();
            rowCell[2] = t.isDaemon();
            rowCell[3] = t.isInterrupted();
            rowCell[4] = t.getThreadGroup().getName();
            rowCell[5] = t.getName();
        }
        synchronized (dataLock) {
            pendingCellData = cell;
        }
    }

    private void transferPendingCellData() {
        // this method is called by the event thread
        synchronized (dataLock) {
            cellData = pendingCellData;
            rowCount = cellData.length;
        }
    }

    @Override
    public int getRowCount() {
        // this method is called by the event thread
        return rowCount;
    }

    @Override
    public Object getValueAt(int row, int col) {
        // this method is called by the event thread
        return cellData[row][col];
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Class<?> getColumnClass(int columnIdx) {
        return columnClass[columnIdx];
    }

    @Override
    public String getColumnName(int columnIdx) {
        return columnName[columnIdx];
    }

    public static Thread[] findAllThreads() {
        ThreadGroup group =
                Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // traverse the ThreadGroup tree to the top
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // Create a destination array that is about
        // twice as big as needed to be very confident
        // that none are clipped.
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];
        // Load the thread references into the oversized
        // array. The actual number of threads loaded
        // is returned.
        int actualSize = topGroup.enumerate(slackList);
        // copy into a list that is the exact size
        Thread[] list = new Thread[actualSize];
        System.arraycopy(slackList, 0, list, 0, actualSize);
        return list;
    }
}
