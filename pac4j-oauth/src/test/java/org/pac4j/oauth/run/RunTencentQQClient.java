package org.pac4j.oauth.run;

import com.esotericsoftware.kryo.Kryo;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.run.RunClient;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.TencentQQClient;
import org.pac4j.oauth.profile.tencentqq.TencentQQProfile;
import org.pac4j.oauth.profile.weibo.WeiboProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Run manually a test for the {@link TencentQQClient}.
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public final class RunTencentQQClient extends RunClient {

    public static void main(String[] args) throws Exception {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
//        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Administrator\\cacerts");
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        final RunTencentQQClient runTencentQQClient = new RunTencentQQClient();
        runTencentQQClient.run();

//        access_token=937EC8E82A722025A3B08E73ED04730B, token_type=null, expires_in=7776000, refresh_token=519939E1EB03D1E381C50E2FDFD72D82, scope=null

//        OAuth2AccessToken accessToken = new OAuth2AccessToken("937EC8E82A722025A3B08E73ED04730B", null, 7776000, "519939E1EB03D1E381C50E2FDFD72D82", null, "access_token=937EC8E82A722025A3B08E73ED04730B&expires_in=7776000&refresh_token=519939E1EB03D1E381C50E2FDFD72D82");
//        final TencentQQClient qqClient = (TencentQQClient) runTencentQQClient.getClient();
//        final MockWebContext context = MockWebContext.create();
//        qqClient.init(context);
//        final TencentQQProfile tencentQQProfile = qqClient.retrieveUserProfileFromToken(accessToken);
//        runTencentQQClient.verifyProfile(tencentQQProfile);
    }

    @Override
    protected String getLogin() {
        return "859541088";
    }

    @Override
    protected String getPassword() {
        return "";
    }

    @Override
    protected IndirectClient getClient() {
        final TencentQQClient tencentQQClient = new TencentQQClient();
        final String apiKey = "101304215";
        final String apiSecret = "9ff4b840c9957fa5da444cb1d3553645";

        tencentQQClient.setKey(apiKey);
        tencentQQClient.setSecret(apiSecret);
        tencentQQClient.setCallbackUrl("http://www.xinjiakao.com/callback");
        tencentQQClient.addScope(TencentQQClient.TencentQQScope.get_user_info);
        return tencentQQClient;
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
        final TencentQQProfile profile = (TencentQQProfile) userProfile;
        assertEquals("5641BF011A9F1F72A0184E00E52A43A9", profile.getId());
        assertEquals(TencentQQProfile.class.getName() + CommonProfile.SEPARATOR + "5641BF011A9F1F72A0184E00E52A43A9",
                profile.getTypedId());
        assertTrue(ProfileHelper.isTypedIdOf(profile.getTypedId(), TencentQQProfile.class));
        assertTrue(CommonHelper.isNotBlank(profile.getAccessToken()));
        assertCommonProfile(userProfile, null, null, null, "张振利", "张振利",
                Gender.MALE, null,
                "http://q.qlogo.cn/qqapp/101304215/5641BF011A9F1F72A0184E00E52A43A9/100",
                null, "江苏 南京");
        assertEquals(19, profile.getAttributes().size());
    }
}
