package org.pac4j.scribe.model;

import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * Created by ZhangZhenli on 2016/4/22.
 */
public class WeiboToken extends OAuth2AccessToken {

    private String uid;
    private String appkey;
    private String createAt;
    private String extraResponse;

    public WeiboToken(OAuth2AccessToken accessToken, String uid, String appkey, String createAt, String extraResponse) {
        super(accessToken.getAccessToken(), accessToken.getTokenType(), accessToken.getExpiresIn(), accessToken.getRefreshToken(), accessToken.getScope(),
                accessToken.getRawResponse());
        this.uid = uid;
        this.appkey = appkey;
        this.createAt = createAt;
        this.extraResponse = extraResponse;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getExtraResponse() {
        return extraResponse;
    }

    public void setExtraResponse(String extraResponse) {
        this.extraResponse = extraResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        WeiboToken that = (WeiboToken) o;

        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (appkey != null ? !appkey.equals(that.appkey) : that.appkey != null) return false;
        return createAt != null ? createAt.equals(that.createAt) : that.createAt == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (appkey != null ? appkey.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        return result;
    }
}
