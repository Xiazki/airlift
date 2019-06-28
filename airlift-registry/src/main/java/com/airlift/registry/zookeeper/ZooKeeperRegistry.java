package com.airlift.registry.zookeeper;

import com.airlift.registry.Registry;
import com.airlift.registry.URL;
import com.airlift.registry.exception.RegistryException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ZooKeeperRegistry implements Registry {

    Logger logger = LoggerFactory.getLogger(ZooKeeperRegistry.class);

    private ZooKeeperCuratorConnection zooKeeperCuratorConnection;
    private CuratorFramework zkClient;
    private URL url;
    private static final String root = "/airlift";
    private List<URL> urlList;

    public ZooKeeperRegistry(URL url, ZooKeeperCuratorConnection zooKeeperCuratorConnection) {
        try {
            this.url = url;
            this.zooKeeperCuratorConnection = zooKeeperCuratorConnection;
            zkClient = zooKeeperCuratorConnection.connection(url.getRegistryUrls());
        } catch (Exception e) {
            throw new RegistryException("connect to zookeeper failed", e);
        }
    }

    public void doRegister() {
        createNode(toNodePath());
    }


    public void doUnRegister() {
        System.out.println(lookup());
        deleteNode(toNodePath());
        System.out.println(lookup());

    }

    public List<URL> lookup() {
        try {
            List<String> provider = zkClient.getChildren().forPath(toServiceNodePath());
            return provider.stream().map(this::toURL).collect(Collectors.toList());
//            return urlList;
        } catch (Throwable e) {
            throw new RegistryException("failed to lookup " + url + " from zookeeper,case: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        try {
            zkClient.close();
        } catch (Throwable e) {
            throw new RegistryException("failed to close zk client", e);
        }
    }

    private void createRootNode(String rootPath) {
        try {
            zkClient.create().forPath("airlift");
        } catch (Exception e) {
            throw new RegistryException("error creating root node ", e);
        }
    }

    private void createNode(String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().forPath(path);
        } catch (KeeperException.NodeExistsException e) {

        } catch (Exception e) {
            throw new RegistryException("error creating node ", e);
        }
    }

    private void deleteNode(String path) {
        try {
            zkClient.delete().forPath(path);
            System.out.println("delete " + path);
        } catch (Throwable e) {
            throw new RegistryException("error to delete node " + path + " from zookeeper", e);
        }
    }

    private void createEphemeralNode(String path) {

    }

    private URL toURL(String provider) {
        URL proUrl = new URL();
        String[] ipPort = provider.split(":");
        proUrl.setServiceInterface(url.getServiceInterface());
        proUrl.setRegistryUrls(url.getRegistryUrls());
        proUrl.setHost(ipPort[0]);
        proUrl.setPort(Integer.valueOf(ipPort[1]));
        return proUrl;
    }

    private String toServiceNodePath() {
        return root + "/" + url.getServiceInterface();
    }

    private String toNodePath() {
        return root + "/" + url.getServiceInterface() + "/" + url.getHost() + ":" + url.getPort();
    }

    private String toRootPath() {
        return url.getApplicationName();
    }

}
