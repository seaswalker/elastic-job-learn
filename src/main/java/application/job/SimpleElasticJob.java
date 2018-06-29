package application.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import java.util.concurrent.TimeUnit;

/**
 * {@link com.dangdang.ddframe.job.api.simple.SimpleJob}任务.
 *
 * @author skywalker
 */
public class SimpleElasticJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(shardingContext);

        throw new RuntimeException();
    }

}
