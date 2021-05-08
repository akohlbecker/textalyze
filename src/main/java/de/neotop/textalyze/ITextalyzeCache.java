package de.neotop.textalyze;

import java.io.IOException;

import org.ehcache.Cache;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;

public interface ITextalyzeCache {

    Cache<String, TextalyzeRecord> getCache() throws IOException;

    /**
     * Store the <code>TextalyzeRecord</code> in the cache with
     * {@link TextalyzeRecord#getId()} as key.
     *
     * @param record
     * @throws CacheWritingException
     * @throws IOException
     */
    void put(TextalyzeRecord record) throws CacheWritingException, IOException;

    /**
     * @param id
     *            The {@link TextalyzeRecord#getId()}
     */
    TextalyzeRecord lookup(String id) throws CacheLoadingException, IOException;

}