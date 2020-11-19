package com.koncheng.task.scheduler.core.zk.lock;

import com.koncheng.task.scheduler.core.zk.ZooKeeperInstance;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import javax.naming.OperationNotSupportedException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ZooKeeper Lock
 *
 * @author chenyong01@baijiahulian.com
 * @version 2020-11-19
 */
@Slf4j
public class ZkLock implements Lock {

    private static final long DEF_WAIT_TIME_IN_MILLISECONDS = 100L;
    private static final String LOCK_PATH_PREFIX = "/lock/";

    private final String key;

    public ZkLock(String key) {
        this.key = key;
    }

    @SneakyThrows
    @Override
    public void lock() {
        if (!this.tryLock(-1L, null)) {
            throw new RuntimeException("获取锁失败");
        }
    }

    @SneakyThrows
    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new OperationNotSupportedException("不支持的方法!");
    }

    @SneakyThrows
    @Override
    public boolean tryLock() {
        return this.tryLock(0L, null);
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        long start = System.nanoTime();

        try {
            while (!this.lock(key)) {

                if (timeout != 0L) {
                    TimeUnit.MILLISECONDS.sleep(DEF_WAIT_TIME_IN_MILLISECONDS);
                    if (timeout < 0L || System.nanoTime() - start < unit.toNanos(timeout)) {
                        continue;
                    }
                }

                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("获取锁失败, key: {}, exception: \n{}", key, e);
            return false;
        }
    }

    @Override
    public void unlock() {
        this.unlock(key);
    }

    @SneakyThrows
    @Override
    public Condition newCondition() {
        throw new OperationNotSupportedException("不支持的方法!");
    }

    private boolean lock(String key) {
        String path = LOCK_PATH_PREFIX + key;
        ZooKeeper zooKeeper = ZooKeeperInstance.getZooKeeper();
        try {
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void unlock(String key) {
        String path = LOCK_PATH_PREFIX + key;
        ZooKeeper zooKeeper = ZooKeeperInstance.getZooKeeper();
        try {
            zooKeeper.delete(path, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
