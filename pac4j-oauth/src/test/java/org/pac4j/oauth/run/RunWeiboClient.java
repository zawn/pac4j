package org.pac4j.oauth.run;

import com.esotericsoftware.kryo.Kryo;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.run.RunClient;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.oauth.client.WeiboClient;
import org.pac4j.oauth.profile.weibo.WeiboProfile;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Run manually a test for the {@link Google2Client}.
 *
 * @author Jerome Leleu
 * @since 1.9.0
 */
public final class RunWeiboClient extends RunClient {

    public static void main(String[] args) throws Exception {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
//        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Administrator\\cacerts");
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        new RunWeiboClient().run();
    }

    @Override
    protected String getLogin() {
        return "sxse@sina.com";
    }

    @Override
    protected String getPassword() {
        return "zhang#1990";
    }

    @Override
    protected IndirectClient getClient() {
        final WeiboClient weibo2Client = new WeiboClient();
        final String apiKey = "1199052435";
        final String apiSecret = "ea58fff4f260fd95bcb1223a76456d3b";

        weibo2Client.setKey(apiKey);
        weibo2Client.setSecret(apiSecret);
        weibo2Client.setCallbackUrl("https://api.weibo.com/oauth2/default.html");
        weibo2Client.setScope(WeiboClient.WeiboScope.email);
        return weibo2Client;
    }

    @Override
    protected void registerForKryo(final Kryo kryo) {
        kryo.register(WeiboProfile.class);

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
                "http://tp4.sinaimg.cn/1390951803/50/5630124907/1",
                "http://weibo.com/sxse", "安徽 淮北");
        assertEquals(Locale.SIMPLIFIED_CHINESE,profile.getLocale());
        assertEquals(34, profile.getAttributes().size());
    }
}
