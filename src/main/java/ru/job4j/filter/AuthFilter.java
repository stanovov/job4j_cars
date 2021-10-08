package ru.job4j.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest sReq, ServletResponse sResp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sReq;
        HttpServletResponse resp = (HttpServletResponse) sResp;
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }
        chain.doFilter(sReq, sResp);
    }

    @Override
    public void destroy() {
    }
}
