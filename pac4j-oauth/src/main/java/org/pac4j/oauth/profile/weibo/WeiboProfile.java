package org.pac4j.oauth.profile.weibo;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.oauth.profile.OAuth20Profile;

import java.util.Locale;

/**
 * <p>This class is the user profile for Google (using OAuth protocol version 2) with appropriate getters.</p>
 * <p>It is returned by the {@link org.pac4j.oauth.client.Google2Client}.</p>
 *
 * @author Jerome Leleu
 * @since 1.2.0
 */
public class WeiboProfile extends OAuth20Profile {

    private static final long serialVersionUID = -7486869356444327783L;

    private transient final static AttributesDefinition ATTRIBUTES_DEFINITION = new WeiboAttributesDefinition();

    @Override
    public AttributesDefinition getAttributesDefinition() {
        return ATTRIBUTES_DEFINITION;
    }

    @Override
    public String getId() {
        setId(getAttribute(WeiboAttributesDefinition.id));
        return super.getId();
    }

    @Override
    public String getFirstName() {
        return (String) getAttribute(WeiboAttributesDefinition.name);
    }

    @Override
    public String getDisplayName() {
        return (String) getAttribute(WeiboAttributesDefinition.screen_name);
    }

    @Override
    public String getUsername() {
        return (String) getAttribute(WeiboAttributesDefinition.screen_name);
    }

    @Override
    public Locale getLocale() {
        return (Locale) getAttribute(WeiboAttributesDefinition.lang);
    }

    @Override
    public String getPictureUrl() {
        return (String) getAttribute(WeiboAttributesDefinition.profile_image_url);
    }

    @Override
    public String getProfileUrl() {
        final String attribute = (String) getAttribute(WeiboAttributesDefinition.profile_url);
        if (attribute.startsWith("http"))
            return attribute;
        else
            return "http://weibo.com/" + attribute;
    }
}
