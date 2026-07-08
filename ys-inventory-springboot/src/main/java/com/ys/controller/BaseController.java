package com.ys.controller;

import com.ys.model.Firm;
import com.ys.service.AuthService;
import jakarta.servlet.http.HttpSession;

public abstract class BaseController {

    protected abstract AuthService authService();

    // AuthInterceptor already guarantees "firmId" exists in the session
    // for any request that reaches this point.
    protected Integer firmId(HttpSession session) {
        return (Integer) session.getAttribute("firmId");
    }

    protected Firm firm(HttpSession session) {
        return authService().getById(firmId(session));
    }
}
