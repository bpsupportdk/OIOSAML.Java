package dk.gov.oio.saml.filter;

import dk.gov.oio.saml.util.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class SameSiteFilter implements Filter {
    private static final String SAMESITE_COOKIE_HEADER = "Set-Cookie";
    private static final String SAMESITE_ATTRIBITE_NAME = "SameSite";
    private static final String SAMESITE_NONE_VALUE = "None";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse httpServletResponse) {
            patchSameSiteCookies(httpServletResponse);
            chain.doFilter(request, response);
        }

        chain.doFilter(request, response);
    }

    private void patchSameSiteCookies(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders(SAMESITE_COOKIE_HEADER);
        if (headers == null || headers.isEmpty()) {
            return;
        }

        boolean firstCookie = true;
        for (String header : headers) {
            if (StringUtil.isEmpty(header)) {
                continue;
            }

            if (!header.contains(SAMESITE_ATTRIBITE_NAME)) {
                header = header + ";" + SAMESITE_ATTRIBITE_NAME + "=" + SAMESITE_NONE_VALUE;
            }

            // overwrite existing cookies on first run, then append the new ones
            if (firstCookie) {
                response.setHeader(SAMESITE_COOKIE_HEADER, header);
            }
            else {
                response.addHeader(SAMESITE_COOKIE_HEADER, header);
            }

            firstCookie = false;
        }
    }
}