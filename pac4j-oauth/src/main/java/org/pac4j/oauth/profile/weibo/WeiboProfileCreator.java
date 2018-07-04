package org.pac4j.oauth.profile.weibo;

import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.creator.OAuth20ProfileCreator;
import org.pac4j.oauth.profile.definition.OAuth20ProfileDefinition;
import org.pac4j.scribe.extractors.WeiboJsonExtractor;
import org.pac4j.scribe.model.WeiboToken;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * Specific profile creator for Weibo.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public class WeiboProfileCreator extends OAuth20ProfileCreator<WeiboProfile> {

    public WeiboProfileCreator(final OAuth20Configuration configuration,
                               final IndirectClient client) {
        super(configuration, client);
    }

    @Override
    protected WeiboProfile retrieveUserProfileFromToken(WebContext context,
                                                        OAuth2AccessToken accessToken) {
        String uid = getUidFormToken(context, accessToken);
        OAuth20ProfileDefinition<WeiboProfile, OAuth20Configuration> profileDefinition
            = (OAuth20ProfileDefinition<WeiboProfile, OAuth20Configuration>) configuration.getProfileDefinition();
        String profileUrl = profileDefinition.getProfileUrl(accessToken, configuration);
        profileUrl = profileUrl + "?uid=" + uid;
        final OAuth20Service service = this.configuration.buildService(context, client, null);
        final String body = sendRequestForData(service, accessToken, profileUrl, Verb.GET);
        if (body == null) {
            throw new HttpCommunicationException("Not data found for accessToken: " + accessToken);
        }
        final WeiboProfile profile = profileDefinition.extractUserProfile(body);
        addAccessTokenToProfile(profile, accessToken);
        return profile;
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


    private String getUidFormToken(WebContext context, OAuth2AccessToken accessToken) {
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
            final OAuth20Service service = this.configuration.buildService(context, client, null);
            final String tokenInfoBody = sendRequestForData(service, accessToken, getTokenInfoUrl(accessToken), Verb.GET);
            uid = extractTokenInfo(accessToken, tokenInfoBody).getUid();
        }
        return uid;
    }

    private WeiboToken extractTokenInfo(OAuth2AccessToken accessToken, final String body) {
        final WeiboToken extract = WeiboJsonExtractor.instance().createFormExistedToken(accessToken, body);
        return extract;
    }
}
