package com.campusrental.security;
import com.campusrental.exception.UserUnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class GraphQlPermission {

    public boolean hasManagerRole() {
        if(ObjectUtils.isEmpty(RequestContext.getLoggedUser()))
            throw new  UserUnAuthorizedException("You are not authorized");
        log.info("RequestContext: " + RequestContext.getLoggedUser());
        if (RequestContext.getLoggedUser().getRole().getRoleName().equalsIgnoreCase("MANAGER"))
            return true;
        return false;
    }

    public boolean hasOfficeStaffRole() {
        if(ObjectUtils.isEmpty(RequestContext.getLoggedUser()))
            throw new  UserUnAuthorizedException("You are not authorized");
        log.info("RequestContext: " + RequestContext.getLoggedUser());
        if (RequestContext.getLoggedUser().getRole().getRoleName().equalsIgnoreCase("OFFICE_STAFF"))
            return true;
        return false;
    }

}