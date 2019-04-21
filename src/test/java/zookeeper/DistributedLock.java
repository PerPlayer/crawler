package zookeeper;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class DistributedLock {

    private static final String HOST = "47.101.189.129:2181";

    private static final String ROOT = "/lock";

    private static final CuratorFramework CLIENT;

    static {
        CLIENT = CuratorFrameworkFactory.builder()
                .connectString(HOST)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(4000, 3))
                .build();
    }

    static int a = 0;
    public static void main(String[] args) throws Exception{
        DruidDataSourceBuilder.create().build();
        CLIENT.start();
        InterProcessMutex lock = new InterProcessMutex(CLIENT, ROOT);
//        new InterProcessSemaphoreV2(CLIENT, ROOT, 10).acquire();
//        new InterProcessSemaphoreMutex(CLIENT, ROOT).acquire();
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(3);
            new Thread(){
                @Override
                public void run(){
                    try {
                        lock.acquire();
                        System.out.println(getNum() + " " + Thread.currentThread().getName());
                        Thread.sleep(500);
                        System.out.println(getNum() + " " + Thread.currentThread().getName());
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static int getNum(){
        return ++a;
    }
}

class Singleton{
    private Singleton(){}
    public static class Sub{
        public static Singleton getInstance(){
            return new Singleton();
        }
    }
    public static Singleton getInstance(){
        return Sub.getInstance();
    }
}
