package org.pac4j.oauth.credentials;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.pac4j.core.util.CommonHelper;

/**
 * This class represents an OAuth credentials for OAuth 2.0 an authorization code.
 *
 * @author zhangzhenli
 * @since 1.9.0
 */
public class OAuth20Credentials extends OAuthCredentials {

    private static final long serialVersionUID = -1370874913317625788L;
    private String code;

    private OAuth2AccessToken accessToken;

    /**
     * For OAuth2 Authorization Code Flow.
     *
     * @param code       the authorization code
     */
    public OAuth20Credentials(String code) {
        this.code = code;
    }

    /**
     * For OAuth2 Other Flow.
     *
     * @param accessToken the accessToken.
     */
    public OAuth20Credentials(OAuth2AccessToken accessToken) {
        this.code = null;
        this.accessToken = accessToken;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuth20Credentials that = (OAuth20Credentials) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return accessToken != null ? accessToken.equals(that.accessToken) : that.accessToken == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        return result;
    }

    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return CommonHelper.toNiceString(this.getClass(),
                "code", code,
                "accessToken", accessToken);
    }
}
