package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.apis.SinaWeiboApi20;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.exception.OAuthCredentialsException;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.weibo.WeiboProfile;
import org.pac4j.scribe.extractors.WeiboJsonExtractor;
import org.pac4j.scribe.model.WeiboToken;

/**
 * <p>This class is the OAuth client to authenticate users in Weibo using OAuth protocol version 2.0.</p>
 * <p>The <i>scope</i> is by default : {@link WeiboScope#email}, but it can also but set to : {@link WeiboScope#all}
 * or {@link WeiboScope#email}.</p>
 * <p>It returns a {@link WeiboProfile}.</p>
 * <p>More information at http://open.weibo.com/wiki/Oauth2/</p>
 *
 * @author Zhang Zhenli
 * @since 1.2.0
 */
public class WeiboClient extends BaseOAuth20Client<WeiboProfile> {

    public enum WeiboScope {
        all,                        // 	请求下列所有scope权限
        email,                      //	用户的联系邮箱，接口文档
        direct_messages_write,  // 	私信发送接口，接口文档
        direct_messages_read,   // 	私信读取接口，接口文档
        invitation_write,       // 	邀请发送接口，接口文档
        friendships_groups_read,//	好友分组读取接口组，接口文档
        friendships_groups_write,//	好友分组写入接口组，接口文档
        statuses_to_me_read,      // 	定向微博读取接口组，接口文档
        follow_app_official_microblog // 	关注应用官方微博，该参数不对应具体接口，只需在应用控制台填写官方帐号即可。填写的路径：我的应用-选择自己的应用-应用信息-基本信息-官方运营账号（默认值是应用开发者帐号）
    }

    protected WeiboScope scope = WeiboScope.email;

    protected String scopeValue;

    public WeiboClient() {
    }

    public WeiboClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("scope", this.scope);
        if (this.scope == null)
            this.scope = WeiboScope.email;
        this.scopeValue = this.scope.toString();
        super.internalInit(context);
    }

    @Override
    protected BaseApi<OAuth20Service> getApi() {
        return SinaWeiboApi20.instance();
    }

    @Override
    protected boolean hasOAuthGrantType() {
        return true;
    }

    @Override
    protected String getOAuthScope() {
        return this.scopeValue;
    }

    @Override
    protected String getProfileUrl(final OAuth2AccessToken accessToken) {
        return "https://api.weibo.com/2/users/show.json";
    }

    /**
     * 获取Token完整信息的url.
     *
     * @param accessToken the access token
     * @return the url of the token info given by the provider
     */
    protected String getTokenInfoUrl(final OAuth2AccessToken accessToken) {
        return "https://api.weibo.com/oauth2/get_token_info";
    }

    @Override
    protected WeiboProfile retrieveUserProfileFromToken(OAuth2AccessToken accessToken) throws HttpAction {
        String uid = getUidFormToken(accessToken);
        String profileUrl = getProfileUrl(accessToken);
        profileUrl = profileUrl + "?uid=" + uid;
        final String body = sendRequestForData(accessToken, profileUrl);
        if (body == null) {
            throw new HttpCommunicationException("Not data found for accessToken: " + accessToken);
        }
        final WeiboProfile profile = extractUserProfile(body);
        addAccessTokenToProfile(profile, accessToken);
        return profile;
    }

    private String getUidFormToken(OAuth2AccessToken accessToken) {
        String uid = "";
        final String rawResponse = accessToken.getRawResponse();
        logger.info("AccessToken rawResponse:" + rawResponse);
        if (rawResponse != null) {
            try {
                JsonNode json = JsonHelper.getFirstNode(rawResponse);
                if (json != null) {
                    uid = (String) JsonHelper.getElement(json, "uid");
                }
            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        }
        if (uid == null || uid.equals("")) {
            final String tokenInfoBody = sendRequestForData(accessToken, getTokenInfoUrl(accessToken));
            uid = extractTokenInfo(accessToken, tokenInfoBody).getUid();
        }
        return uid;
    }

    private WeiboToken extractTokenInfo(OAuth2AccessToken accessToken, final String body) {
        final WeiboToken extract = (WeiboToken) WeiboJsonExtractor.instance().createFormExistedToken(accessToken, body);
        return extract;
    }

    @Override
    protected WeiboProfile extractUserProfile(final String body) throws HttpAction {
        System.out.println(body);
        final WeiboProfile profile = new WeiboProfile();
        final JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null) {
            profile.setId(JsonHelper.getElement(json, "id"));
            for (final String attribute : profile.getAttributesDefinition().getPrimaryAttributes()) {
                profile.addAttribute(attribute, JsonHelper.getElement(json, attribute));
            }
        }
        return profile;
    }

    @Override
    protected boolean hasBeenCancelled(final WebContext context) {
        final String error = context.getRequestParameter(OAuthCredentialsException.ERROR);
        // user has denied permissions
        if ("access_denied".equals(error)) {
            return true;
        }
        return false;
    }

    public WeiboScope getScope() {
        return this.scope;
    }

    public void setScope(final WeiboScope scope) {
        this.scope = scope;
    }
}
