package com.koncheng.task.scheduler.core.zk;

import com.koncheng.task.scheduler.core.zk.watcher.DefaultWatcher;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyong01@baijiahulian.com
 * @version 2020-11-19
 */
public class ZooKeeperInstance {

    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private static final String SERVERS = "120.53.100.219:2181,120.53.102.226:2181,49.234.118.75:2181";
    private static ZooKeeper zooKeeper = null;
    private static final Watcher DEFAULT_WATCHER = new DefaultWatcher(LATCH);

    static {
        try {
            zooKeeper = new ZooKeeper(SERVERS, 1000, DEFAULT_WATCHER);
            LATCH.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}
