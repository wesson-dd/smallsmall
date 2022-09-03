package com.small.util;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author wesson
 * Create at 2021/12/16 22:52 周四
 */
public class ThreadView extends JPanel {
    private final MyThreadInfo tableModel;

    public ThreadView() {
        tableModel = new MyThreadInfo();
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumnModel colModel = table.getColumnModel();
        int numColumns = colModel.getColumnCount();
        // manually size all but the last column
        for (int i = 0; i < numColumns - 1; i++) {
            TableColumn col = colModel.getColumn(i);
            col.sizeWidthToFit();
            col.setPreferredWidth(col.getWidth() + 5);
            col.setMaxWidth(col.getWidth() + 5);
        }
        JScrollPane sp = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);
    }

    public void dispose() {
        tableModel.stopRequest();
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
    }

    public static JFrame createFramedInstance() {
        final ThreadView viewer = new ThreadView();
        final JFrame f = new JFrame("ThreadViewer");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                f.setVisible(false);
                f.dispose();
                viewer.dispose();
            }
        });
        f.setContentPane(viewer);
        f.setSize(500, 300);
        f.setVisible(true);
        return f;
    }

    public static void main(String[] args) {
        JFrame f = ThreadView.createFramedInstance();
        // For this example, exit the VM when the viewer
        // frame is closed.
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // Keep the main thread from exiting by blocking
        // on wait() for a notification that never comes.
        Object lock = new Object();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException x) {
                Thread.currentThread().interrupt();
            }
        }
    }
}