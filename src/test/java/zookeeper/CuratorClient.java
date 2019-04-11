package zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CuratorClient {

    /** Zookeeper info */
    private static final String ADDRESS = "47.101.189.129:2181";
    private static final String PATH = "/crawler";

    public static void main(String[] args) throws Exception {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        String lock = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(PATH + "/lock");
        System.out.println("Create znode: " + lock);
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    Stat stat = client.checkExists().forPath(PATH);
                    if (stat == null||event.getData().getPath().equals(lock)) {
                        System.out.println("The znodes is empty");
                        return;
                    }
                    List<String> nodes = client.getChildren().forPath(PATH);
                    if (isMin(lock, nodes)) {
                        client.delete().forPath(lock);
                        System.out.println("The lock is deleted: " + lock);
                    } else {
                        System.out.println(event.getType() + " --");
                    }
                }
            }
        });
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    private static boolean isMin(String p, List<String> paths) {
        Integer pseq = getSeq(p);
        for (String path : paths) {
            Integer seq = getSeq(path);
            if (seq < pseq) {
                return false;
            }
        }
        return true;
    }

    private static Integer getSeq(String path) {
        return Integer.valueOf(path.substring(path.lastIndexOf("/") + 5));
    }
}
