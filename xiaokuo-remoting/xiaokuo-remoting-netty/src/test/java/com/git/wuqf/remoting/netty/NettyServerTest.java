package com.git.wuqf.remoting.netty;

import com.git.wuqf.remoting.Server;

import java.net.MalformedURLException;
import com.git.wuqf.xiaokuo.common.URL;

/**
 * Created by wuqf on 17-2-25.
 */
public class NettyServerTest {
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        URL url=new URL("http://localhost:9999");
        NettyTransports nettyTransports=new NettyTransports();
        Server server=nettyTransports.bind(url,new WordHandler());
        Thread.currentThread().join();
    }
}
