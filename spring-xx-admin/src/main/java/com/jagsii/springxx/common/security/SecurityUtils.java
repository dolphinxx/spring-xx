package com.jagsii.springxx.common.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SecurityUtils {
    private static SecurityUtils INSTANCE;

    public static SecurityUtils getInstance() {
        return INSTANCE;
    }

    private final MessageSourceAccessor messages;

    private ConcurrentLinkedQueue<Long> onlineUsers = new ConcurrentLinkedQueue<>();

    public SecurityUtils(@Qualifier("springSecurityMessageSourceAccessor") MessageSourceAccessor messages) {
        INSTANCE = this;
        this.messages = messages;
    }

    public boolean hasPermission(String permission) {
        throw new UnsupportedOperationException();
    }

    public AccessDeniedException newAccessDeniedException() {
        return new AccessDeniedException(
                this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
    }

    public void addOnlineUser(Long userId) {
        onlineUsers.add(userId);
    }

    public void removeOnlineUser(Long userId) {
        // TODO: kick user
        onlineUsers.remove(userId);
    }

    public static Principal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (Principal) authentication.getPrincipal();
        }
        return null;
    }

    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("The length should not be less than 8");
        }
        int baseCount = length / 4;
        int lowerCaseCount = baseCount + length % 4;
        SecureRandom random = new SecureRandom();
        List<Integer> chars = new ArrayList<>(length);
        for (int i = 0; i < lowerCaseCount; i++) {
            // lower case letters, a-z
            chars.add(random.nextInt(26) + 97);
        }
        for (int i = 0; i < baseCount; i++) {
            // upper case letters, A-Z
            chars.add(random.nextInt(26) + 65);
            // numbers, 0-9
            chars.add(random.nextInt(10) + 48);
            // special chars, #, $, %, &
            chars.add(random.nextInt(4) + 35);
        }
        Collections.shuffle(chars);
        StringBuilder builder = new StringBuilder();
        for (int c : chars) {
            builder.append((char) c);
        }
        return builder.toString();
    }

    /**
     * Sets a cookie indicating that the current session has been authenticated. Because the remember-me cookie is httpOnly and cannot be accessed by js.
     */
    public static void sendAuthCookie(HttpServletResponse response) {
        Collection<String> setCookies = response.getHeaders("set-cookie");
        if (setCookies.isEmpty()) {
            return;
        }
        String rememberMeCookieStr = null;
        for (String c : setCookies) {
            if (c.startsWith("remember-me=")) {
                rememberMeCookieStr = c;
                break;
            }
        }
        if (rememberMeCookieStr == null) {
            return;
        }
        Matcher m = Pattern.compile("max-age=(\\d+)", Pattern.CASE_INSENSITIVE).matcher(rememberMeCookieStr);
        if (!m.find()) {
            return;
        }
        Cookie cookie = new Cookie("authenticated", "1");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(Integer.parseInt(m.group(1)) - 10);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
