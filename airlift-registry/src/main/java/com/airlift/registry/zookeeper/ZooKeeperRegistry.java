package com.airlift.registry.zookeeper;

import com.airlift.common.ConcurrentHashSet;
import com.airlift.registry.Registry;
import com.airlift.registry.URL;
import com.airlift.registry.exception.RegistryException;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ZooKeeperRegistry implements Registry {

    Logger logger = LoggerFactory.getLogger(ZooKeeperRegistry.class);

    private ZooKeeperCuratorConnection zooKeeperCuratorConnection;
    private CuratorFramework zkClient;
    private URL url;
    private static final String root = "/airlift";
    private Set<URL> urlSet = new ConcurrentHashSet<>();

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
            if (urlSet.isEmpty()) {
                List<String> provider = zkClient.getChildren().forPath(toServiceNodePath());
                List<URL> retUrls = provider.stream().map(this::toURL).collect(Collectors.toList());
                urlSet.addAll(retUrls);
                return retUrls;
            }
            return new ArrayList<>(urlSet);
        } catch (Throwable e) {
            throw new RegistryException("failed to lookup " + url + " from zookeeper,case: " + e.getMessage(), e);
        }
    }

    @Override
    public void subscribe() {
        try {
            CuratorWatcher curatorWatcher = event -> {
                if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    List<String> providers = zkClient.getChildren().forPath(toServiceNodePath());
                    if (providers == null || providers.isEmpty()) {
                        urlSet.clear();
                        return;
                    }
                    urlSet.addAll(providers.stream().map(this::toURL).collect(Collectors.toList()));
                }
            };
            zkClient.getChildren().usingWatcher(curatorWatcher).forPath(toServiceNodePath());
        } catch (Exception e) {
            throw new RegistryException("failed to subscribe :" + toServiceNodePath() + " from zookeeper ,case:" + e.getMessage(), e);
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
