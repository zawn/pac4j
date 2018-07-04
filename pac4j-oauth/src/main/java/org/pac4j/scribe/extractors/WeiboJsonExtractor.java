package org.pac4j.scribe.extractors;

import java.util.regex.Pattern;

import org.pac4j.scribe.model.WeiboToken;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * This class represents a specific JSON extractor for Weibo using OAuth protocol version 2. It could be part of the Scribe library.
 *
 * @author Zhang Zhenli
 * @since 3.1.0
 */
public class WeiboJsonExtractor extends OAuth2AccessTokenJsonExtractor {

    public static final Pattern UID_REGEX = Pattern.compile("\"uid\"\\s*:\\s*\"(\\S*?)\"");

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
            extractParameter(response, UID_REGEX, true));
    }
}
