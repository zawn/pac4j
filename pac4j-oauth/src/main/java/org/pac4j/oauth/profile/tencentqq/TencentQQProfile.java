package org.pac4j.oauth.profile.tencentqq;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.Gender;
import org.pac4j.oauth.profile.OAuth20Profile;

/**
 * <p>This class is the user profile for Tencent QQ Connect with appropriate getters.</p>
 * <p>It is returned by the {@link org.pac4j.oauth.client.TencentQQClient}.</p>
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class TencentQQProfile extends OAuth20Profile {

    private static final long serialVersionUID = 1648212768999086087L;

    private transient final static AttributesDefinition ATTRIBUTES_DEFINITION = new TencentQQAttributesDefinition();

    @Override
    public AttributesDefinition getAttributesDefinition() {
        return ATTRIBUTES_DEFINITION;
    }

    @Override
    public String getDisplayName() {
        return (String) getAttribute(TencentQQAttributesDefinition.nickname);
    }

    @Override
    public String getUsername() {
        return (String) getAttribute(TencentQQAttributesDefinition.nickname);
    }

    @Override
    public Gender getGender() {
        return (Gender) getAttribute(TencentQQAttributesDefinition.gender);
    }

    @Override
    public String getLocation() {
        final String location = (String) getAttribute(TencentQQAttributesDefinition.province) + " " + (String) getAttribute(TencentQQAttributesDefinition.city);
        return location;
    }

    @Override
    public String getPictureUrl() {
        return (String) getAttribute(TencentQQAttributesDefinition.figureurl_qq_2);
    }
}
