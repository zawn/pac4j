package org.pac4j.oauth.profile.tencentqq;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.core.profile.converter.FormattedDateConverter;
import org.pac4j.core.profile.converter.GenderConverter;

import java.util.Arrays;

/**
 * This class defines the attributes of the Tencent QQ Connect profile.
 *
 * @author Zhang Zhenli
 * @since 1.9.0
 */
public class TencentQQAttributesDefinition extends AttributesDefinition {
    /**
     * 返回码
     */
    public static final String ret = "ret";
    /**
     * 如果retx小于0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
     */
    public static final String msg = "msg";
    /**
     * 用户在QQ空间的昵称。
     */
    public static final String nickname = "nickname";
    /**
     * 性别。 如果获取不到则默认返回"男"
     */
    public static final String gender = "gender";
    /**
     * 省份
     */
    public static final String province = "province";
    /**
     * 城市
     */
    public static final String city = "city";
    /**
     * 出生年份
     */
    public static final String year = "year";
    /**
     * 大小为30×30像素的QQ空间头像URL。
     */
    public static final String figureurl = "figureurl";
    /**
     * 大小为50×50像素的QQ空间头像URL。
     */
    public static final String figureurl_1 = "figureurl_1";
    /**
     * 大小为100×100像素的QQ空间头像URL。
     */
    public static final String figureurl_2 = "figureurl_2";
    /**
     * 大小为40×40像素的QQ头像URL。
     */
    public static final String figureurl_qq_1 = "figureurl_qq_1";
    /**
     * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
     */
    public static final String figureurl_qq_2 = "figureurl_qq_2";
    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）。
     */
    public static final String is_yellow_vip = "is_yellow_vip";
    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）
     */
    public static final String vip = "vip";
    /**
     * 黄钻等级
     */
    public static final String yellow_vip_level = "yellow_vip_level";
    /**
     * 黄钻等级
     */
    public static final String level = "level";
    /**
     * 标识是否为年费黄钻用户（0：不是； 1：是）
     */
    public static final String is_yellow_year_vip = "is_yellow_year_vip";
    /**
     * 标识是否为年费黄钻用户（0：不是； 1：是）
     */
    public static final String is_lost = "is_lost";

    public TencentQQAttributesDefinition() {
        Arrays.stream(new String[]{
                msg,
                nickname,
                figureurl,
                figureurl_1,
                figureurl_2,
                figureurl_qq_1,
                figureurl_qq_2,
                is_yellow_vip,
                vip,
                yellow_vip_level,
                level,
                is_yellow_year_vip,
                province,
                city,
                year
        }).forEach(a -> primary(a, Converters.STRING));
        primary(ret, Converters.INTEGER);
        primary(is_lost, Converters.INTEGER);
        primary(gender, new GenderConverter("男", "女"));
        primary(year, new FormattedDateConverter("yyyy"));
    }
}
