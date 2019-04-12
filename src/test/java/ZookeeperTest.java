import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import zookeeper.CreateGroup;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZookeeperTest {

    /** Zookeeper info */
    private static final String ADDRESS = "47.101.189.129:2181";
    private static final String GROUP = "crawler";

    public static void main(String[] args) throws Exception {
        CreateGroup group = CreateGroup.newGroup(ADDRESS);
        group.delete("/crawler/lock0000000085");
        /*group.joinWithEphAndSeq(GROUP, "csdn-");

        group.list(GROUP).forEach((String path) -> {
            System.out.println(path);
        });
        Thread.sleep(Long.MAX_VALUE);*/
    }

    private static void lock(CreateGroup group, String path){
        group.exists(path, group, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int i, String s, Object ctx, Stat stat) {
                try {
                    List<String> paths = group.list(GROUP);
                    boolean isMin = isMin(s, paths);
                    System.out.println(s + " --");
                    if (isMin) {
                        group.delete(s);
                        System.out.println(s + " is gone!" + " " + new Date());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "hehe");
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
