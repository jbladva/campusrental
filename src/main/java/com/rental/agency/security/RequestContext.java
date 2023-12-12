package com.rental.agency.security;

import com.rental.agency.entity.User;

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
