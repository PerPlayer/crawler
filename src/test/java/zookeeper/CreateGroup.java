package zookeeper;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CreateGroup extends ConnectionWatcher {

    public static CreateGroup newGroup(String host){
        CreateGroup createGroup = new CreateGroup();
        try {
            createGroup.connect(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createGroup;
    }

    public void create(String groupName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName;
        String createdPath = zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println("Created " + createdPath);
    }

    public void join(String groupName, String memberName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName + "/" + memberName;
        String createdPath = zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println("Created " + createdPath);
    }

    public String joinWithEphAndSeq(String groupName, String memberName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName + "/" + memberName;
        String createdPath = zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Created " + createdPath + " " + new Date());
        return createdPath;
    }

    public void exists(String path, Watcher watcher, AsyncCallback.StatCallback callback, Object ctx) {
        zk.exists(path, watcher, callback, ctx);
    }

    public List<String> list(String groupName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            if (children.isEmpty()) {
                System.out.printf("No members in group %s\n", groupName);
                System.exit(1);
            }
            return children;
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
        return Collections.emptyList();
    }

    public void delete(String groupName) throws KeeperException,
            InterruptedException {
        String path = "" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children) {
                zk.delete(path + "/" + child, -1);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }
}