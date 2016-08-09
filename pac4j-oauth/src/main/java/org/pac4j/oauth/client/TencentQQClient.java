package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.tencentqq.TencentQQProfile;
import org.pac4j.scribe.builder.api.TencentQQApi;
import org.pac4j.scribe.extractors.TencentQQJsonExtractor;
import org.pac4j.scribe.model.TencentQQToken;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is the OAuth client to authenticate users in Tencent QQ Connect.</p>
 * <p>It returns a {@link org.pac4j.oauth.profile.tencentqq.TencentQQProfile}.</p>
 * <p>More information at http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token</p>
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class TencentQQClient extends BaseOAuth20Client<TencentQQProfile> {

    public enum TencentQQScope {
        /**
         * 获取登录用户的昵称、头像、性别
         */
        get_user_info,
        /**
         * 获取QQ会员的基本信息	需要申请
         */
        get_vip_info,
        /**
         * 获取QQ会员的高级信息
         */
        get_vip_rich_info,
        /**
         * 获取用户QQ空间相册列表	需要申请
         */
        list_album,
        /**
         * 上传一张照片到QQ空间相册
         */
        upload_pic,
        /**
         * 在用户的空间相册里，创建一个新的个人相册
         */
        add_album,
        /**
         * 获取用户QQ空间相册中的照片列表
         */
        list_photo,
        /**
         * 获取登录用户在腾讯微博详细资料
         */
        get_info,
        /**
         * 发表一条微博
         */
        add_t,
        /**
         * 删除一条微博
         */
        del_t, /**
         * 发表一条带图片的微博
         */
        add_pic_t,
        /**
         * 获取单条微博的转发或点评列表
         */
        get_repost_list,
        /**
         * 获取他人微博资料
         */
        get_other_info,
        /**
         * 我的微博粉丝列表
         */
        get_fanslist,
        /**
         * 我的微博偶像列表
         */
        get_idollist,
        /**
         * 收听某个微博用户
         */
        add_idol,
        /**
         * 取消收听某个微博用户
         */
        del_idol,
        /**
         * 在这个网站上将展现您财付通登记的收货地址	需要申请
         */
        get_tenpay_addr
    }


    protected List<TencentQQScope> scopes;


    public TencentQQClient() {
    }

    public TencentQQClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected BaseApi<OAuth20Service> getApi() {
        return new TencentQQApi();
    }

    @Override
    protected boolean hasOAuthGrantType() {
        return true;
    }

    @Override
    protected String getOAuthScope() {
        StringBuilder builder = null;
        if (scopes != null) {
            for (TencentQQScope value : scopes) {
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
     * @param accessToken  the access token
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

    public List<TencentQQScope> getScopes() {
        return scopes;
    }

    public void setScopes(List<TencentQQScope> scopes) {
        this.scopes = scopes;
    }

    public void addScope(TencentQQScope scopes) {
        if (this.scopes == null)
            this.scopes = new ArrayList<>();
        this.scopes.add(scopes);
    }
}
