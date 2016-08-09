package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.tencentqq.TencentQQProfile;
import org.pac4j.scribe.builder.api.WechatApi;
import org.pac4j.scribe.extractors.TencentQQJsonExtractor;
import org.pac4j.scribe.model.TencentQQToken;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is the OAuth client to authenticate users in Tencent Wechat.</p>
 * <p>It returns a {@link TencentQQProfile}.</p>
 * <p>More information at http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token</p>
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class WechatClient extends BaseOAuth20Client<TencentQQProfile> {

    public enum WechatScope {
        /**
         * 获取登录用户的昵称、头像、性别
         */
        snsapi_login,
        /**
         * 获取QQ会员的基本信息	需要申请
         */
        snsapi_base,
        /**
         * 获取QQ会员的高级信息
         */
        snsapi_userinfo
    }


    protected List<WechatScope> scopes;


    public WechatClient() {
    }

    public WechatClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotBlank("key", this.getKey());
        CommonHelper.assertNotBlank("secret", this.getSecret());
        CommonHelper.assertNotBlank("callbackUrl", this.callbackUrl);

        this.service = getApi().createService(buildOAuthConfig(context));
    }

    @Override
    protected BaseApi<OAuth20Service> getApi() {
        return new WechatApi();
    }

    @Override
    protected boolean hasOAuthGrantType() {
        return true;
    }

    @Override
    protected String getOAuthScope() {
        StringBuilder builder = null;
        if (scopes != null) {
            for (WechatScope value : scopes) {
                if (builder == null) {
                    builder = new StringBuilder();
                } else {
                    builder.append(",");
                }
                builder.append(value.toString());
            }
        }
        return builder == null ? null : builder.toString();
    }

    /**
     * 获取Token完整信息的url.
     *
     * @param accessToken the access token
     * @return the url of the token info given by the provider
     */
    protected String getTokenInfoUrl(final OAuth2AccessToken accessToken) {
        return "https://graph.qq.com/oauth2.0/me";
    }

    @Override
    protected String getProfileUrl(final OAuth2AccessToken accessToken) {
        return "https://graph.qq.com/user/get_user_info";
    }

    @Override
    public TencentQQProfile retrieveUserProfileFromToken(OAuth2AccessToken accessToken) throws HttpAction {
        TencentQQToken accessTokenExtra = getAccessTokenExtra(accessToken);
        String profileUrl = getProfileUrl(accessToken);
        profileUrl = profileUrl + "?openid=" + accessTokenExtra.getOpenid() + "&oauth_consumer_key=" + accessTokenExtra.getClientId();
        final String body = sendRequestForData(accessToken, profileUrl);
        if (body == null) {
            throw new HttpCommunicationException("Not data found for accessToken: " + accessToken);
        }
        final TencentQQProfile profile = extractUserProfile(body);
        addAccessTokenToProfile(profile, accessTokenExtra);
        profile.setId(accessTokenExtra.getOpenid());
        return profile;
    }

    protected TencentQQToken getAccessTokenExtra(OAuth2AccessToken accessToken) {
        final String tokenInfoBody = sendRequestForData(accessToken, getTokenInfoUrl(accessToken));
        return extractAccessTokenExtra(accessToken, tokenInfoBody);
    }

    private TencentQQToken extractAccessTokenExtra(OAuth2AccessToken accessToken, final String body) {
        final TencentQQToken extract = (TencentQQToken) TencentQQJsonExtractor.instance().createFormExistedToken(accessToken, body);
        System.out.println(extract);
        return extract;
    }

    @Override
    protected TencentQQProfile extractUserProfile(String body) throws HttpAction {
        final TencentQQProfile profile = new TencentQQProfile();
        final JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null) {
            for (final String attribute : profile.getAttributesDefinition().getPrimaryAttributes()) {
                profile.addAttribute(attribute, JsonHelper.getElement(json, attribute));
            }
        }
        return profile;
    }

    public List<WechatScope> getScopes() {
        return scopes;
    }

    public void setScopes(List<WechatScope> scopes) {
        this.scopes = scopes;
    }

    public void addScope(WechatScope scopes) {
        if (this.scopes == null)
            this.scopes = new ArrayList<>();
        this.scopes.add(scopes);
    }
}
