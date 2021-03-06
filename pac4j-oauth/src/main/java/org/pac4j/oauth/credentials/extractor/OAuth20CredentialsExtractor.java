package org.pac4j.oauth.credentials.extractor;

import java.io.IOException;

import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.credentials.OAuth20Credentials;
import org.pac4j.oauth.exception.OAuthCredentialsException;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.OAuthEncoder;

/**
 * OAuth 2.0 credentials extractor.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public class OAuth20CredentialsExtractor extends OAuthCredentialsExtractor<OAuth20Credentials, OAuth20Configuration> {

    public OAuth20CredentialsExtractor(final OAuth20Configuration configuration, final IndirectClient client) {
        super(configuration, client);
    }

    @Override
    protected OAuth20Credentials getOAuthCredentials(final WebContext context) {
        if (configuration.isWithState()) {

            final String stateParameter = context.getRequestParameter(OAuth20Configuration.STATE_REQUEST_PARAMETER);

            if (CommonHelper.isNotBlank(stateParameter)) {
                final String stateSessionAttributeName = this.configuration.getStateSessionAttributeName(client.getName());
                final String sessionState = (String) context.getSessionStore().get(context, stateSessionAttributeName);
                // clean from session after retrieving it
                context.getSessionStore().set(context, stateSessionAttributeName, null);
                logger.debug("sessionState: {} / stateParameter: {}", sessionState, stateParameter);
                if (!stateParameter.equals(sessionState)) {
                    final String message = "State parameter mismatch: session expired or possible threat of cross-site request forgery";
                    throw new OAuthCredentialsException(message);
                }
            } else {
                final String message = "Missing state parameter: session expired or possible threat of cross-site request forgery";
                throw new OAuthCredentialsException(message);
            }

        }

        final String codeParameter = context.getRequestParameter(OAuth20Configuration.OAUTH_CODE);
        final String accessTokenParameter = context.getRequestParameter(OAuth20Configuration.OAUTH_ACCESS_TOKEN);
        final String rewResponseParameter = context.getRequestParameter(OAuth20Configuration.OAUTH_TOKEN_RESPONSE);
        if (codeParameter != null) {
            final String code = OAuthEncoder.decode(codeParameter);
            logger.debug("code: {}", code);
            return new OAuth20Credentials(code);
        } else if (accessTokenParameter != null || rewResponseParameter != null) {
            OAuth2AccessToken accessToken;
            if (rewResponseParameter != null) {
                final String rewResponse = OAuthEncoder.decode(rewResponseParameter);
                Response response = new Response(200, "OK", null, rewResponse);
                try {
                    accessToken = ((DefaultApi20) this.configuration.getApi()).getAccessTokenExtractor().extract(response);
                } catch (IOException e) {
                    throw new OAuthCredentialsException("Extracted accessToken failed: please check the submitted request format is correct");
                }
            } else {
                final String accessTokenString = OAuthEncoder.decode(accessTokenParameter);
                accessToken = new OAuth2AccessToken(accessTokenString);
            }
            return new OAuth20Credentials(accessToken);
        } else {
            final String message = "No credential found";
            throw new OAuthCredentialsException(message);
        }
    }
}
