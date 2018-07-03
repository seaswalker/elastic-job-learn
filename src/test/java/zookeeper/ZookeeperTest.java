package zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Hello 管理员.
 *
 * @author skywalker
 */
public class ZookeeperTest {

    private CuratorFramework client;

    @Before
    public void before() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(500, 4);
        client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        System.out.println("Zookeeper连接成功.");
    }

    /**
     * 当有一个事件产生时，每一个Listener是否都会收到通知?答案：是.
     */
    @Test
    public void testListener() throws Exception {
        prepareTestData();
        System.out.println("创建数据成功");

        createListeners();
        System.out.println("创建Listener成功");

        client.delete().forPath("/skywalker/age");
        System.out.println("删除节点成功");

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void testLeaderElection() throws Exception {
        LeaderLatch latch = new LeaderLatch(client, "/skywalker/latch1");
        latch.start();
        latch.await();
        System.out.println("Latch1选举结果: " + latch.hasLeadership());

        LeaderLatch latch2 = new LeaderLatch(client, "/skywalker/latch2");
        latch2.start();
        latch2.await();
        System.out.println("Latch2选举结果: " + latch.hasLeadership());

        latch.close();
        latch2.close();
    }

    private void prepareTestData() throws Exception {
        client.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/skywalker/name", "name".getBytes());

        client.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/skywalker/age", new byte[] {20});
    }

    private void createListeners() throws Exception {
        TreeCache treeCache = new TreeCache(client, "/skywalker");

        treeCache.getListenable().addListener((client, event) -> System.out.println("Listener1-事件类型: " + event));

        treeCache.getListenable().addListener(((client, event) -> System.out.println("Listener2-事件类型: " + event)));

        treeCache.start();
    }

    @After
    public void after() {
        this.client.close();
    }

}
