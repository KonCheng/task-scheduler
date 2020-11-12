package com.koncheng.task.scheduler.core;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyong01@baijiahulian.com
 * @version 2020-11-12
 */
public class ZookeeperMain {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String connStr = "120.53.100.219:32001";
        CountDownLatch countDown = new CountDownLatch(1);

        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.err.println("eventType:" + event.getType());
                    if (event.getType() == Event.EventType.None) {
                        countDown.countDown();
                    } else if (event.getType() == Event.EventType.NodeCreated) {
                        System.out.println("listen:节点创建");
                    } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        System.out.println("listen:子节点修改");
                    }
                }
            }
        };

        ZooKeeper zookeeper = new ZooKeeper(connStr, 5000, watcher);
        countDown.await();

        //注册监听,每次都要重新注册，否则监听不到
        zookeeper.exists("/top/qiJin", watcher);

        // 创建节点
        String result;
//        result = zookeeper.create("/top/qiJin", "love".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(result);

        Thread.sleep(10);

        // 获取节点
        byte[] bs = zookeeper.getData("/top/qiJin", true, null);
        result = new String(bs);
        System.out.println("创建节点后的数据是:" + result);

        // 修改节点
        zookeeper.setData("/top/qiJin", "I love you forever".getBytes(), -1);

        Thread.sleep(10);

        bs = zookeeper.getData("/top/qiJin", true, null);
        result = new String(bs);
        System.out.println("修改节点后的数据是:" + result);

        // 删除节点
//        zookeeper.delete("/top/qiJin", -1);
//        System.out.println("节点删除成功");
    }

}
