package com.campusrental.security;

import com.campusrental.entity.User;

public class RequestContext {
    private RequestContext() {
    }

    private static ThreadLocal<User> loggedUser = ThreadLocal.withInitial(() -> null);

    public static User getLoggedUser() {
        return RequestContext.loggedUser.get();
    }

    public static synchronized void setLoggedUser(User user) {
        RequestContext.loggedUser.set(user);
    }

}
