package com.ys.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Spring's equivalent of the old AuthFilter — blocks any /api/** request
 * that doesn't have a logged-in session. /api/auth/** is excluded via the
 * path pattern registered in WebConfig, so login/register stay open.
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("firmId") == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Not logged in\"}");
            return false; // stop here — don't let the request reach the controller
        }
        return true;
    }
}
