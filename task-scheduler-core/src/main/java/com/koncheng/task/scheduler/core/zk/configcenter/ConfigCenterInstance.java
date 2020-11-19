package com.koncheng.task.scheduler.core.zk.configcenter;

import com.koncheng.task.scheduler.core.zk.ZooKeeperInstance;
import com.koncheng.task.scheduler.core.zk.watcher.NodeWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

/**
 * @author chenyong01@baijiahulian.com
 * @version 2020-11-19
 */
@Slf4j
public class ConfigCenterInstance {

    private static final String path = "/package1";

    public static void start() throws KeeperException, InterruptedException {
        ZooKeeper zookeeper = ZooKeeperInstance.getZooKeeper();

        Watcher nodeWatcher = new NodeWatcher(zookeeper);

        Stat stat = new Stat();
        final byte[] data = zookeeper.getData(path, nodeWatcher, stat);
        log.info("path: {} value is: {}", path, new String(data));

        while (true) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
