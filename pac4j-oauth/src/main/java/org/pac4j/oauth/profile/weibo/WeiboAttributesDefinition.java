package org.pac4j.oauth.profile.weibo;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.core.profile.converter.GenderConverter;

import java.util.Arrays;

/**
 * This class defines the attributes of the Google profile (using OAuth 2.0 protocol).
 *
 * @author Jerome Leleu
 * @since 1.2.0
 */
public class WeiboAttributesDefinition extends AttributesDefinition {
    /**
     * int64	用户UID
     */
    public static final String id = "id";
    /**
     * string	字符串型的用户UID
     */
    public static final String idstr = "idstr";
    /**
     * string	用户昵称
     */
    public static final String screen_name = "screen_name";
    /**
     * string	友好显示名称
     */
    public static final String name = "name";
    /**
     * int	用户所在省级ID
     */
    public static final String province = "province";
    /**
     * int	用户所在城市ID
     */
    public static final String city = "city";
    /**
     * string	用户所在地
     */
    public static final String location = "location";
    /**
     * string	用户个人描述
     */
    public static final String description = "description";
    /**
     * string	用户博客地址
     */
    public static final String url = "url";
    /**
     * string	用户头像地址（中图），50×50像素
     */
    public static final String profile_image_url = "profile_image_url";
    /**
     * string	用户的微博统一URL地址
     */
    public static final String profile_url = "profile_url";
    /**
     * string	用户的个性化域名
     */
    public static final String domain = "domain";
    /**
     * string	用户的微号
     */
    public static final String weihao = "weihao";
    /**
     * string	性别，m：男、f：女、n：未知
     */
    public static final String gender = "gender";
    /**
     * int	粉丝数
     */
    public static final String followers_count = "followers_count";
    /**
     * int	关注数
     */
    public static final String friends_count = "friends_count";
    /**
     * int	微博数
     */
    public static final String statuses_count = "statuses_count";
    /**
     * int	收藏数
     */
    public static final String favourites_count = "favourites_count";
    /**
     * string	用户创建（注册）时间
     */
    public static final String created_at = "created_at";
    /**
     * boolean	暂未支持
     */
    public static final String following = "following";
    /**
     * boolean	是否允许所有人给我发私信，true：是，false：否
     */
    public static final String allow_all_act_msg = "allow_all_act_msg";
    /**
     * boolean	是否允许标识用户的地理位置，true：是，false：否
     */
    public static final String geo_enabled = "geo_enabled";
    /**
     * boolean	是否是微博认证用户，即加V用户，true：是，false：否
     */
    public static final String verified = "verified";
    /**
     * int	暂未支持
     */
    public static final String verified_type = "verified_type";
    /**
     * string	用户备注信息，只有在查询用户关系时才返回此字段
     */
    public static final String remark = "remark";
    /**
     * object	用户的最近一条微博信息字段 详细
     */
    public static final String status = "status";
    /**
     * boolean	是否允许所有人对我的微博进行评论，true：是，false：否
     */
    public static final String allow_all_comment = "allow_all_comment";
    /**
     * string	用户头像地址（大图），180×180像素
     */
    public static final String avatar_large = "avatar_large";
    /**
     * string	用户头像地址（高清），高清头像原图
     */
    public static final String avatar_hd = "avatar_hd";
    /**
     * string	认证原因
     */
    public static final String verified_reason = "verified_reason";
    /**
     * boolean	该用户是否关注当前登录用户，true：是，false：否
     */
    public static final String follow_me = "follow_me";
    /**
     * int	用户的在线状态，0：不在线、1：在线
     */
    public static final String online_status = "online_status";
    /**
     * int	用户的互粉数
     */
    public static final String bi_followers_count = "bi_followers_count";
    /**
     * string	用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
     */
    public static final String lang = "lang";


    public WeiboAttributesDefinition() {
        Arrays.stream(new String[]{idstr,
                screen_name,
                name,
                location,
                description,
                url,
                profile_image_url,
                profile_url,
                domain,
                weihao,
                created_at,
                remark,
                avatar_large,
                avatar_hd,
                verified_reason
        }).forEach(a -> primary(a, Converters.STRING));
        Arrays.stream(new String[]{following,
                allow_all_act_msg,
                geo_enabled,
                verified,
                allow_all_comment,
                follow_me
        }).forEach(a -> primary(a, Converters.BOOLEAN));
        Arrays.stream(new String[]{province,
                city,
                followers_count,
                friends_count,
                statuses_count,
                favourites_count,
                verified_type,
                online_status,
                bi_followers_count
        }).forEach(a -> primary(a, Converters.INTEGER));
        primary(id, Converters.LONG);
        primary(lang, Converters.LOCALE);
        primary(gender, new GenderConverter("m", "f"));
    }
}
