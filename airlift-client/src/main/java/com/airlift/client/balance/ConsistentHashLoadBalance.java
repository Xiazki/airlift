package com.airlift.client.balance;

import com.airlift.client.Invocation;
import com.airlift.registry.URL;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConsistentHashLoadBalance implements LoadBalance {


    private ConcurrentHashMap<String, ConsistentHash> selectorsMap = new ConcurrentHashMap<>();


    @Override
    public URL select(List<URL> urls, Invocation invocation) {
        String key = urls.get(0).toKey();
        ConsistentHash consistentHash = selectorsMap.get(key);
        int identityHashCode = System.identityHashCode(urls);
        if (consistentHash == null || identityHashCode != consistentHash.identityHashCode) {
            //url个数小于5个位每个多创建2个虚拟节点
            int num = urls.size() > 5 ? 0 : 2;
            selectorsMap.putIfAbsent(key, new ConsistentHash(num, urls, identityHashCode));
            consistentHash = selectorsMap.get(key);
        }
        return consistentHash.get(invocation);
    }


    public static class ConsistentHash {

        HashFunc hashFunc;

        private final int numberOfReplicas;

        private final int identityHashCode;
        /**
         * 一致性Hash环
         */
        private final SortedMap<Long, URL> circle = new TreeMap<>();

        /**
         * 构造，使用Java默认的Hash算法
         *
         * @param numberOfReplicas 复制的节点个数，增加每个节点的复制节点有利于负载均衡
         * @param nodes            节点对象
         * @param identityHashCode
         */
        public ConsistentHash(int numberOfReplicas, Collection<URL> nodes, int identityHashCode) {
            this.numberOfReplicas = numberOfReplicas;
            this.identityHashCode = identityHashCode;
            this.hashFunc = key -> {
//                return fnv1HashingAlg(key.toString());
                return md5HashingAlg(key.toString());
            };
            //初始化节点
            for (URL node : nodes) {
                add(node);
            }
        }

        /**
         * 构造
         *
         * @param hashFunc         hash算法对象
         * @param numberOfReplicas 复制的节点个数，增加每个节点的复制节点有利于负载均衡
         * @param nodes            节点对象
         * @param identityHashCode
         */
        public ConsistentHash(HashFunc hashFunc, int numberOfReplicas, Collection<URL> nodes, int identityHashCode) {
            this.numberOfReplicas = numberOfReplicas;
            this.hashFunc = hashFunc;
            this.identityHashCode = identityHashCode;
            //初始化节点
            for (URL node : nodes) {
                add(node);
            }
        }

        /**
         * 增加节点<br>
         * 每增加一个节点，就会在闭环上增加给定复制节点数<br>
         * 例如复制节点数是2，则每调用此方法一次，增加两个虚拟节点，这两个节点指向同一Node
         * 由于hash算法会调用node的toString方法，故按照toString去重
         *
         * @param node 节点对象
         */
        public void add(URL node) {
            for (int i = 0; i < numberOfReplicas; i++) {
                circle.put(hashFunc.hash(node.toString() + i), node);
            }
        }

        /**
         * 移除节点的同时移除相应的虚拟节点
         *
         * @param node 节点对象
         */
        public void remove(URL node) {
            for (int i = 0; i < numberOfReplicas; i++) {
                circle.remove(hashFunc.hash(node.toString() + i));
            }
        }

        /**
         * 获得一个最近的顺时针节点
         *
         * @param key 为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
         * @return 节点对象
         */
        public URL get(Object key) {
            if (circle.isEmpty()) {
                return null;
            }
            long hash = hashFunc.hash(key);
            if (!circle.containsKey(hash)) {
                SortedMap<Long, URL> tailMap = circle.tailMap(hash); //返回此映射的部分视图，其键大于等于 hash
                hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
            }
            //正好命中
            return circle.get(hash);
        }

        /**
         * 使用MD5算法
         *
         * @param key
         * @return
         */
        private static long md5HashingAlg(String key) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                md5.update(key.getBytes());
                byte[] bKey = md5.digest();
                long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
                return res;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return 0l;
        }

        /**
         * 使用FNV1hash算法
         *
         * @param key
         * @return
         */
        private static long fnv1HashingAlg(String key) {
            final int p = 16777619;
            int hash = (int) 2166136261L;
            for (int i = 0; i < key.length(); i++)
                hash = (hash ^ key.charAt(i)) * p;
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
            return hash;
        }

        /**
         * Hash算法对象，用于自定义hash算法
         */
        public interface HashFunc {
            public Long hash(Object key);
        }
    }

}
