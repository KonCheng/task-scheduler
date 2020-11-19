package com.koncheng.task.scheduler.core.zk.configcenter;

import org.apache.zookeeper.KeeperException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigCenterInstanceTest {

    @Test
    public void start() throws KeeperException, InterruptedException {
        ConfigCenterInstance.start();
    }
}