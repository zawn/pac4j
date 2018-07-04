package org.pac4j.oauth.run;

import java.util.Locale;

import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.run.RunClient;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.WeiboClient;
import org.pac4j.oauth.profile.weibo.WeiboProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Run manually a test for the {@link WeiboClient}.
 *
 * @author ZhangZhenli
 * @since 1.9.0
 */
public final class RunWeiboClient extends RunClient {

    public static void main(String[] args) throws Exception {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");
//        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Administrator\\cacerts");
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        new RunWeiboClient().run();
    }

    @Override
    protected String getLogin() {
        return "sxse@sina.com";
    }

    @Override
    protected String getPassword() {
        return "";
    }

    @Override
    protected IndirectClient getClient() {
        final String apiKey = "3722350620";
        final String apiSecret = "3edbe998b0d53130db83928c330c879b";

        final WeiboClient weiboClient = new WeiboClient();
        weiboClient.setKey(apiKey);
        weiboClient.setSecret(apiSecret);
        weiboClient.setCallbackUrl("https://git.xjiakao.com/cas/login?client_name=WeiboClient");
        weiboClient.setScope(WeiboClient.WeiboScope.email);
        return weiboClient;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void verifyProfile(CommonProfile userProfile) {
        final WeiboProfile profile = (WeiboProfile) userProfile;
        assertEquals("1390951803", profile.getId());
        assertEquals(WeiboProfile.class.getName() + CommonProfile.SEPARATOR + "1390951803",
            profile.getTypedId());
        assertTrue(ProfileHelper.isTypedIdOf(profile.getTypedId(), WeiboProfile.class));
        assertTrue(CommonHelper.isNotBlank(profile.getAccessToken()));
        assertCommonProfile(userProfile, null, "sxse", null, "sxse", "sxse",
            Gender.MALE, Locale.SIMPLIFIED_CHINESE,
            "http://tva3.sinaimg.cn/crop.0.0.180.180.1024/52e83d7bjw1e8qgp5bmzyj2050050aa8.jpg",
            "http://weibo.com/sxse", "安徽 淮北");
        assertEquals(Locale.SIMPLIFIED_CHINESE, profile.getLocale());
        assertEquals(35, profile.getAttributes().size());
    }
}
