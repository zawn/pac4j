package org.pac4j.scribe.model;

import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * Tencent QQ token extra.
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class TencentQQToken extends OAuth2AccessToken {

    private String openid;
    private String client_id;
    private String extraResponse;

    public TencentQQToken(OAuth2AccessToken accessToken, String openid, String client_id, String extraResponse) {
        super(accessToken.getAccessToken(), accessToken.getTokenType(), accessToken.getExpiresIn(), accessToken.getRefreshToken(), accessToken.getScope(),
                accessToken.getRawResponse());
        this.openid = openid;
        this.client_id = client_id;
        this.extraResponse = extraResponse;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getClientId() {
        return client_id;
    }

    public void setClientId(String client_id) {
        this.client_id = client_id;
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

        TencentQQToken that = (TencentQQToken) o;

        if (openid != null ? !openid.equals(that.openid) : that.openid != null) return false;
        return client_id != null ? client_id.equals(that.client_id) : that.client_id == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (client_id != null ? client_id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TencentQQToken{" + super.toString() + " " +
                "openid='" + openid + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }
}
