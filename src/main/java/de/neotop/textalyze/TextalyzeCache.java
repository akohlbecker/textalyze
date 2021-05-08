package de.neotop.textalyze;

import java.io.IOException;
import java.time.Duration;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;


@Component
public class TextalyzeCache implements ITextalyzeCache, DisposableBean {

    private static final int CACHE_EXPIRATION_HOURS_DEFAULT = 1;

    private static final String CACHE_NAME = "default";

    CacheManager cacheManager;

    private void initCache() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(
                        CACHE_NAME,
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, TextalyzeRecord.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(100, EntryUnit.ENTRIES)
                                .offheap(1, MemoryUnit.MB)
                            )
                            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(CACHE_EXPIRATION_HOURS_DEFAULT)))
                        )
                .build(true);
    }

    @Override
    public Cache<String, TextalyzeRecord> getCache() throws IOException {
        if(cacheManager == null) {
            initCache();
        }
        return cacheManager.getCache(CACHE_NAME, String.class, TextalyzeRecord.class);
    }

    @Override
    public TextalyzeRecord lookup(String id) throws CacheLoadingException, IOException {
       return getCache().get(id);
    }

    @Override
    public void put(TextalyzeRecord record) throws CacheWritingException, IOException {
        getCache().put(record.getId(), record);
    }

    @Override
    public void destroy() throws Exception {
        cacheManager.close();
    }

}
