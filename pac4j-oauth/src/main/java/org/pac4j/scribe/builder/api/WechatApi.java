/*
  Copyright 2012 - 2014 Zhang Zhenli

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.scribe.builder.api;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import org.pac4j.scribe.oauth.WechatService;


/**
 * This class represents the OAuth API implementation for Tencent Wechat using OAuth protocol version 2. It could be part of the Scribe library.
 * <pre>
 * appid	        是	应用唯一标识
 * redirect_uri	    是	重定向地址，需要进行UrlEncode
 * response_type	是	填code
 * scope	        是	应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即可
 * state	        否	用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
 * </pre>
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class WechatApi extends DefaultApi20 {

    // Endpont Url.
    public static final String AUTHORIZE_ENDPOINT_URL = "https://open.weixin.qq.com/connect/qrconnect";
    public static final String TOKEN_ENDPOINT_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    // Url Templet
    public static final String AUTHORIZATION_URL_TEMPLET = AUTHORIZE_ENDPOINT_URL +
            "?appid=%s" +
            "&redirect_uri=%s" +
            "&response_type=code";

    private static class InstanceHolder {
        private static final WechatApi INSTANCE = new WechatApi();
    }

    public static WechatApi instance() {
        return WechatApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        String authorizeUrl = String.format(AUTHORIZATION_URL_TEMPLET,
                config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()));
        if (config.getScope() != null) {
            authorizeUrl = authorizeUrl + "&scope=" + OAuthEncoder.encode(config.getScope());
        } else {
            authorizeUrl = authorizeUrl + "&scope=snsapi_login";
        }
        return authorizeUrl;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_ENDPOINT_URL + "?grant_type=authorization_code";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new WechatService(this, config);
    }
}

