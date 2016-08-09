package org.pac4j.scribe.extractors;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import org.pac4j.scribe.model.TencentQQToken;

/**
 * This class represents a specific JSON extractor for Tencent QQ Connect using OAuth protocol version 2.
 * It could be part of the Scribe library.
 * <p>More information at http://wiki.connect.qq.com/%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7openid_oauth2-0</p>
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class TencentQQJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    public static final String OPENID_REGEX = "\"openid\"\\s*:\\s*\"(\\S*?)\"";
    public static final String CLIENT_ID_REGEX = "\"client_id\"\\s*:\\s*\"(\\S*?)\"";

    protected TencentQQJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final TencentQQJsonExtractor INSTANCE = new TencentQQJsonExtractor();
    }

    public static TencentQQJsonExtractor instance() {
        return TencentQQJsonExtractor.InstanceHolder.INSTANCE;
    }

    public TencentQQToken createFormExistedToken(OAuth2AccessToken accessToken, String response) {
        return new TencentQQToken(accessToken,
                extractParameter(response, OPENID_REGEX, true),
                extractParameter(response, CLIENT_ID_REGEX, true),
                response);
    }
}
