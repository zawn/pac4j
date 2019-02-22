package org.pac4j.oauth.credentials.authenticator;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.oauth.client.OAuth20Client;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.credentials.OAuth20Credentials;
import org.pac4j.oauth.credentials.OAuthCredentials;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * OAuth 2.0 authenticator.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public class OAuth20Authenticator extends OAuthAuthenticator<OAuth20Credentials, OAuth20Configuration> {

    public OAuth20Authenticator(final OAuth20Configuration configuration, final IndirectClient client) {
        super(configuration, client);
    }

    @Override
    public void validate(OAuth20Credentials credentials, WebContext context) {
        if (credentials.getAccessToken() == null) {
            super.validate(credentials, context);
        } else {
            validateAccessToken(credentials, context);
        }
    }

    @Override
    protected void retrieveAccessToken(final WebContext context, final OAuthCredentials credentials) {
        OAuth20Credentials oAuth20Credentials = (OAuth20Credentials) credentials;
        // no request token saved in context and no token (OAuth v2.0)
        final String code = oAuth20Credentials.getCode();
        logger.debug("code: {}", code);
        final OAuth2AccessToken accessToken;
        try {
            accessToken = this.configuration.buildService(context, client, null).getAccessToken(code);
        } catch (final IOException | InterruptedException | ExecutionException e) {
            throw new HttpCommunicationException("Error getting token:" + e.getMessage());
        }
        logger.debug("accessToken: {}", accessToken);
        oAuth20Credentials.setAccessToken(accessToken);
    }

    /**
     * Directly verify the token submitted by the user.
     *
     * @param credentials
     * @param context
     */
    protected void validateAccessToken(OAuth20Credentials credentials, WebContext context) {
        if (client instanceof OAuth20Client) {
            boolean allow = ((OAuth20Client) client).isAllowExistingToken(credentials, context);
            if (allow) {
                return;
            }
        }
        throw new CredentialsException("Existing AccessToken is not allowed");
    }
}
