package application.configuration;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import application.job.SimpleElasticJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 添加/注册一个{@link com.dangdang.ddframe.job.api.simple.SimpleJob}.
 *
 * @author skywalker
 */
@Configuration
public class SimpleElasticJobConfiguration {

    @Resource
    private ZookeeperRegistryCenter registryCenter;

    @Bean
    public SimpleElasticJob simpleJob() {
        return new SimpleElasticJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler jobScheduler(@Value("${simpleJob.cron}") final String cron,
                                     @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount) {
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(
                "simpleElasticJob", cron, shardingTotalCount
        ).build();

        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(
                jobCoreConfiguration, SimpleElasticJob.class.getCanonicalName()
        );

        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).build();

        return new JobScheduler(registryCenter, liteJobConfiguration);
    }

}
