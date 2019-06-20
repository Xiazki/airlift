package com.xiazki.airlift.common;

import java.net.InetAddress;

public class IPUtil {

    public static String getIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

}
