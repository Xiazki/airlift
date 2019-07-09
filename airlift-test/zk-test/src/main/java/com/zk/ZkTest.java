package com.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

public class ZkTest {

    public static final String PATH = "/zk_test_123";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        ZookeeperConnection zkConnection = new ZookeeperConnection();
        ZooKeeper zooKeeper = zkConnection.connection("127.0.0.1");

//        System.out.println("create node ...");
//        try {
//            createNode(zooKeeper);
//        }catch (KeeperException.NodeExistsException e){
//            System.out.println(e.getMessage());
//        }
//
//
//        System.out.println("get node data ...");
//        String data = getData(zooKeeper);
//        System.out.println(data);
//
//        Thread.sleep(6000);

        System.out.println("get children list ...");
        List<String> strings = getChildren(zooKeeper);
        System.out.println(strings);


//
//        zkConnection.close();
//
//        zooKeeper = zkConnection.connection("127.0.0.1");
//
//        System.out.println("get children list ...");
//        strings = getChildren(zooKeeper);
//        System.out.println(strings);


    }

    public static void createNode(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        zooKeeper.create(PATH, "hello world".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    public static String getData(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(PATH, false, null);
        return new String(data);
    }

    public static List<String> getChildren(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        return zooKeeper.getChildren("/airlift/com.airlift.springtest.api.HelloWorldApi", false);
    }

}
