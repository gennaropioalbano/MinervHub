package it.minervhub.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String urlPrecedente = (String) session.getAttribute("URL_PRECEDENTE");

        if (urlPrecedente != null) {
            session.removeAttribute("URL_PRECEDENTE");
            getRedirectStrategy().sendRedirect(request, response, urlPrecedente);
        } else {
            // Se non c'è un URL salvato, usa il comportamento di default (di solito /home o /)
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}