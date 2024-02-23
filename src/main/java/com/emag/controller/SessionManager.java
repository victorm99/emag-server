package com.emag.controller;

import com.emag.exception.AuthenticationException;
import com.emag.exception.BadRequestException;
import com.emag.model.pojo.User;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    private static final String LOGGED_USER_ID = "logged_user_id";
    private static final String LOGGED_USER_REMOTE_ADDRESS = "logged_user_remote_address";
    private static final String SUBCATEGORY_ID = "subcategory_id";

    @Autowired
    private UserRepository userRepository;

    public void isLoggedVerification(HttpSession session) {
        if (session.getAttribute(LOGGED_USER_ID) != null ){
            throw new AuthenticationException("You are already logged in!");
        }
    }

    public void loginUser(HttpServletRequest request, long id) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED_USER_REMOTE_ADDRESS, request.getRemoteAddr());
        session.setAttribute(LOGGED_USER_ID , id);
    }

    public void logoutUser(HttpSession session) {
        if (session.getAttribute(LOGGED_USER_ID) == null){
            throw new BadRequestException("You have to be logged in to logout!");
        }
        session.invalidate();
    }

    public boolean userHasPrivileges(HttpServletRequest request, long id) {
        User user = getLoggedUser(request);
        if (user.isAdmin()){
            return true;
        }
        return id == user.getId();
    }

    public boolean userHasPrivileges(HttpServletRequest request) {
        User user = getLoggedUser(request);
        return user.isAdmin();
    }

    public User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        validateSession(request);
        long userId = (long) session.getAttribute(LOGGED_USER_ID);
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("The user does not exist!"));
    }

    public void validateSession (HttpServletRequest request){
        String remoteAddress = request.getRemoteAddr();
        HttpSession session = request.getSession();
        if (session.getAttribute(LOGGED_USER_ID) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        if (!remoteAddress.equals(session.getAttribute(LOGGED_USER_REMOTE_ADDRESS))) {
            session.invalidate();
            throw new AuthenticationException("IP mismatch!");
        }
    }

    public void setSubcategoryId(HttpSession session , long id) {
        session.setAttribute(SUBCATEGORY_ID , id);
    }

    public long getSubcategoryId(HttpServletRequest request) {
        HttpSession session = request.getSession();
//        validateSession(request);
        if (session.getAttribute(SUBCATEGORY_ID) == null){
            throw new BadRequestException("No subcategory selected !");
        }
        return (long) session.getAttribute(SUBCATEGORY_ID);
    }
}
