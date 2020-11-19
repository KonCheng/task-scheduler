package com.koncheng.task.scheduler.core.zk.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ZkLockTest {

    @Test
    public void tryLock() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    String key = "package1";
                    Lock lock = new ZkLock(key);
                    String name = Thread.currentThread().getName();

                    try {
                        if (lock.tryLock()) {
                            log.info(name + " at work");
                            TimeUnit.SECONDS.sleep(10);
                            lock.unlock();
                        } else {
                            log.info(name + "didn't work");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
        while (true) {

        }

    }
}