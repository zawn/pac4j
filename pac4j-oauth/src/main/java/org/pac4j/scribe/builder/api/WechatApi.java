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

import java.util.Map;

import org.pac4j.oauth.client.WechatClient;
import org.pac4j.scribe.extractors.WechatJsonExtractor;

import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;


/**
 * This class represents the OAuth API implementation for Tencent Wechat using OAuth protocol version 2. It could be part of the Scribe library.
 * <p>
 * <p>More info at: <a href=
 * "https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN">OAuth2.0</a></p>
 *
 * @author Zhang Zhenli
 * @since 3.1.0
 */
public class WechatApi extends DefaultApi20 {

    //WeChat QR code login
    public static final String AUTHORIZE_ENDPOINT_URL_1 = "https://open.weixin.qq.com/connect/qrconnect";
    //WeChat embedded browser,, call native login.
    public static final String AUTHORIZE_ENDPOINT_URL_2 = "https://open.weixin.qq.com/connect/oauth2/authorize";

    public static final String TOKEN_ENDPOINT_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private static class InstanceHolder {
        private static final WechatApi INSTANCE = new WechatApi();
    }

    public static WechatApi instance() {
        return WechatApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_ENDPOINT_URL + "?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(String responseType, String apiKey, String callback,
                                      String scope, String state,
                                      Map<String, String> additionalParams) {
        String authorizationUrl = super.getAuthorizationUrl(responseType, apiKey, callback, scope, state, additionalParams);
        authorizationUrl = authorizationUrl.replace("client_id", "appid");
        if (scope != null && scope.contains(WechatClient.WechatScope.snsapi_login.name())) {
            authorizationUrl = AUTHORIZE_ENDPOINT_URL_1 + authorizationUrl;
        } else {
            authorizationUrl = AUTHORIZE_ENDPOINT_URL_2 + authorizationUrl;
        }
        return authorizationUrl;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return WechatJsonExtractor.instance();
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return new ClientAuthenticationType() {

            @Override
            public void addClientAuthentication(OAuthRequest request, String apiKey,
                                                String apiSecret) {
                request.addParameter("appid", apiKey);
                if (apiSecret != null) {
                    request.addParameter("secret", apiSecret);
                }
            }
        };
    }
}

