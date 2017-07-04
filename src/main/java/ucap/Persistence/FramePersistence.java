package ucap.Persistence;

import ucap.htmlpage.FrameDigest;

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
    void saveValue(FrameDigest.PersistenceObj value, String websiteHost
            , long loadingDate);
}
