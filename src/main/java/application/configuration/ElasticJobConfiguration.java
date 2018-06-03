package application.configuration;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Elastic job属性配置.
 *
 * @author skywalker
 */
@Configuration
@ConditionalOnExpression("'${spring.elasticjob.serverList}'.length() > 0")
public class ElasticJobConfiguration {

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(@Value("${spring.elasticjob.serverList}") final String serverList,
                                                  @Value("${spring.elasticjob.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }

}
