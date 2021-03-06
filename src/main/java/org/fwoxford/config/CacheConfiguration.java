package org.fwoxford.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.fwoxford.domain.EquipmentModel;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.fwoxford.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Project.class.getName() + ".projectRelates", jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.ProjectSite.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.ProjectSite.class.getName() + ".projectRelates", jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.ProjectRelate.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.EquipmentGroup.class.getName(), jcacheConfiguration);
            cm.createCache(EquipmentModel.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Equipment.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Area.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.SupportRackType.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.SupportRack.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.SampleType.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.FrozenBoxType.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.FrozenTubeType.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.FrozenBox.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.FrozenTube.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.FrozenTube.class.getName() + ".frozenBoxes", jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.FrozenBox.class.getName() + ".frozenTubes", jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.SampleClassification.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Delegate.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.SerialNo.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.Question.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.QuestionItem.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.QuestionItemDetails.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.service.dto.response.QuestionForDataTableEntity.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.AuthorizationRecord.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.ReplyRecord.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.SendRecord.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.ReplyDetails.class.getName(), jcacheConfiguration);
            cm.createCache(org.fwoxford.domain.QuartzTask.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
