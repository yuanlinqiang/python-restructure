package com.gsafety.pythonrestructure.config.oauth2;

/**
 * Created by YLQ on 2020/05/10.
 */
public class AccessToken {
    /**
     * 访问令牌字符串
     */
    public String access_token;

    /**
     * 令牌类型字符串
     */
    public String token_type;

    /**
     * 刷新令牌字符串
     */
    public String refresh_token;


    /**
     * 权限范围字符串
     */
    public String scope;

    /**
     * 过期时间，秒
     */
    public int expires_in;

    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     */
    public String getAccessToken() {
        return this.access_token;
    }

    /**
     * 设置访问令牌
     *
     * @param accessToken 访问令牌
     */
    public void setAccessToken(final String accessToken) {
        this.access_token = accessToken;
    }

    /**
     * 获取令牌类型
     *
     * @return 令牌类型
     */
    public String getTokenType() {
        return this.token_type;
    }

    /**
     * 设置令牌类型
     *
     * @param tokenType 令牌类型
     */
    public void setTokenType(final String tokenType) {
        this.token_type = tokenType;
    }

    /**
     * 获取刷新令
     *
     * @return 刷新令牌
     */
    public String getRefreshToken() {
        return this.refresh_token;
    }

    /**
     * 设置刷新令牌
     *
     * @param refreshToken 刷新令牌
     */
    public void setRefreshToken(final String refreshToken) {
        this.refresh_token = refreshToken;
    }

    /**
     * 获取业务系统权限范围
     *
     * @return scope 业务系统权限范围
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * 设置业务系统权限范围
     *
     * @param scope 业务系统权限范围
     */
    public void setScope(final String scope) {
        this.scope = scope;
    }

    /**
     * 获取过期时间(单位秒）
     *
     * @return 过期时间
     */
    public int getExpiresIn() {
        return this.expires_in;
    }

    /**
     * 设置过期时间(单位秒）
     *
     * @param expiresIn 过期时间
     */
    public void setExpiresIn(final int expiresIn) {
        this.expires_in = expiresIn;
    }
}
