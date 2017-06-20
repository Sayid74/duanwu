package com.ucap.duanwu.Persistence;

import com.ucap.duanwu.htmlpage.FrameDigest;

/**
 * Created by emmet on 2017/6/20.
 */
public interface FramePersistence {
    /**
     * Save frame page value.
     *
     * @param value       the value
     * @param websiteHost the website host
     * @param loadingDate the loading date
     */
    void saveFramePageValue(FrameDigest.PersistenceObj value, String websiteHost
            , long loadingDate);
}
