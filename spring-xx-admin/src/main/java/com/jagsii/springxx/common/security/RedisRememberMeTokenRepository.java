package com.jagsii.springxx.common.security;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

public class RedisRememberMeTokenRepository implements PersistentTokenRepository {
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        // TODO
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
// TODO
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        // TODO
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
// TODO
    }
}
