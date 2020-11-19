package com.koncheng.task.scheduler.core.zk.watcher;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author chenyong01@baijiahulian.com
 * @version 2020-11-19
 */
@Slf4j
public class NodeWatcher implements Watcher {
    private final ZooKeeper zooKeeper;

    public NodeWatcher(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @SneakyThrows
    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info(String.valueOf(watchedEvent));

        final Event.EventType eventType = watchedEvent.getType();
        final String path = watchedEvent.getPath();
        switch (eventType) {

            case None:
                break;
            case NodeCreated:

                break;
            case NodeDeleted:
                break;
            case NodeDataChanged:
                log.info("NodeDataChanged, path: {}", path);
                final byte[] data = zooKeeper.getData(path, this, new Stat());
                log.info("new value for path: {} is {}", path, new String(data));
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
            default:
        }
    }
}
