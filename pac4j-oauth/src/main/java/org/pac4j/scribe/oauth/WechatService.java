package org.pac4j.scribe.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * This service is dedicated for Wechat service using OAuth protocol version 2.0.
 *
 * @author Alexey Ogarkov
 * @since 1.5.0
 */
public final class WechatService extends OAuth20Service {

    public WechatService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    protected <T extends AbstractRequest> T createAccessTokenRequest(String code, T request) {
        final OAuthConfig config = getConfig();
        request.addParameter("appid", config.getApiKey());
        request.addParameter("secret", config.getApiSecret());
        request.addParameter(OAuthConstants.CODE, code);
        if (config.hasScope()) {
            request.addParameter(OAuthConstants.SCOPE, config.getScope());
        }
        if (config.hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, config.getGrantType());
        }
        return request;
    }
}
