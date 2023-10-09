package com.jagsii.springxx.common.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    public static CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (CurrentUser) authentication.getDetails();
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
}
