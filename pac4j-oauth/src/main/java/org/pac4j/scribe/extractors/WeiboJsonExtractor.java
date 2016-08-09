package org.pac4j.scribe.extractors;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import org.pac4j.scribe.model.WeiboToken;

/**
 * This class represents a specific JSON extractor for ORCiD using OAuth protocol version 2. It could be part of the Scribe library.
 * {
 * "uid": 1073880650,
 * "appkey": 1352222456,
 * "scope": null,
 * "create_at": 1352267591,
 * "expire_in": 157679471
 * }
 *
 * @author Jens Tinglev
 * @since 1.6.0
 */
public class WeiboJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    public static final String UID_REGEX = "\"uid\"\\s*:\\s*\"(\\S*?)\"";
    public static final String APPKEY_REGEX = "\"appkey\"\\s*:\\s*\"(\\S*?)\"";
    public static final String CREATE_AT_REGEX = "\"create_at\"\\s*:\\s*\"(\\S*?)\"";

    protected WeiboJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final WeiboJsonExtractor INSTANCE = new WeiboJsonExtractor();
    }

    public static WeiboJsonExtractor instance() {
        return WeiboJsonExtractor.InstanceHolder.INSTANCE;
    }

    public WeiboToken createFormExistedToken(OAuth2AccessToken accessToken, String response) {
        return new WeiboToken(accessToken,
                extractParameter(response, UID_REGEX, true),
                extractParameter(response, APPKEY_REGEX, true),
                extractParameter(response, CREATE_AT_REGEX, true),
                response);
    }
}
