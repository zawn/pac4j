package org.pac4j.oauth.run;

import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.run.RunClient;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.WechatClient;
import org.pac4j.oauth.profile.wechat.WechatProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Run manually a test for the {@link WechatClient}.
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public final class RunWechatClient extends RunClient {

    public static void main(String[] args) throws Exception {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");
//        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Administrator\\cacerts");
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        final RunWechatClient runWechatClient = new RunWechatClient();
        runWechatClient.run();
    }

    @Override
    protected String getLogin() {
        return "zhangzawn";
    }

    @Override
    protected String getPassword() {
        return "";
    }

    @Override
    protected IndirectClient getClient() {
        final WechatClient wechatClient = new WechatClient();
        final String apiKey;
        final String apiSecret;
        final String callbackUrl;
        final WechatClient.WechatScope scope;
        if (false) {
//          Only for WeChat QRCode login.
            apiKey = "wx87e3d5b7290aa255";
            apiSecret = "3f70550b81198131429b408cc1c8b091";
            callbackUrl = "http://www.yomoer.cn/ThirdpartyLogin/afterWeixinLogin";
            scope = WechatClient.WechatScope.snsapi_login;
        } else {
//            WeChat embedded browser, call native login.
            apiKey = "wxade4323a74dcb897";
            apiSecret = "43854fe4325282609ace1da9df0cbf32";
            callbackUrl = "http://192.168.10.20/callback";
            scope = WechatClient.WechatScope.snsapi_base;
        }

        wechatClient.setKey(apiKey);
        wechatClient.setSecret(apiSecret);
        wechatClient.setCallbackUrl(callbackUrl);
        wechatClient.addScope(scope);
        return wechatClient;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void verifyProfile(CommonProfile userProfile) {
        final WechatProfile profile = (WechatProfile) userProfile;
        assertEquals("ofrPB1XEft_igz1Ms-QeOcev-SZQ", profile.getId());
        assertEquals(WechatProfile.class.getName() + CommonProfile.SEPARATOR + "ofrPB1XEft_igz1Ms-QeOcev-SZQ",
            profile.getTypedId());
        assertTrue(ProfileHelper.isTypedIdOf(profile.getTypedId(), WechatProfile.class));
        assertTrue(CommonHelper.isNotBlank(profile.getAccessToken()));
        assertCommonProfile(userProfile, null, null, null, "张", "张",
            Gender.MALE, null,
            "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIeoXSyFb6PBH1zZ9rqFxRj2Y4nCrfBs3VociatoRttyDTVGkT60xh1JDnYsR84ywqJk3h5RO4YxIw/132",
            null, "Nanjing,Jiangsu,CN");
        assertEquals(9, profile.getAttributes().size());
    }
}
