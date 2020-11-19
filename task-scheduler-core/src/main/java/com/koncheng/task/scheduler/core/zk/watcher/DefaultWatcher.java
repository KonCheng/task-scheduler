package com.koncheng.task.scheduler.core.zk.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.proto.WatcherEvent;

import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.Watcher.Event.KeeperState.*;

/**
 * @author chenyong01@baijiahulian.com
 * @version 2020-11-19
 */
@Slf4j
public class DefaultWatcher implements Watcher {

    private final CountDownLatch latch;

    public DefaultWatcher(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info(String.valueOf(watchedEvent));

        final Event.KeeperState keeperState = watchedEvent.getState();
        final Event.EventType eventType = watchedEvent.getType();
        final WatcherEvent watcherEvent = watchedEvent.getWrapper();

        switch (keeperState) {
            case Unknown:
            case Expired:
            case ConnectedReadOnly:
            case SaslAuthenticated:
            case AuthFailed:
            case NoSyncConnected:
            case Disconnected:
                break;
            case SyncConnected:
                latch.countDown();
                break;
            default:
        }
    }
}
