package dk.gov.oio.saml.filter;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class SameSiteFilter implements Filter {
    private static final String SAMESITE_COOKIE_HEADER = "Set-Cookie";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        if (response instanceof HttpServletResponse httpServletResponse) {
            patchSameSiteCookies(httpServletResponse);
        }
    }

    private void patchSameSiteCookies(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders(SAMESITE_COOKIE_HEADER);

        if (headers == null || headers.isEmpty()) {
            return;
        }

        boolean first = true;

        for (String header : headers) {
            if (header == null || header.isEmpty()) {
                continue;
            }

            String updated = header;

            if (!header.toLowerCase().contains("samesite")) {
                updated = header + "; SameSite=None";
            }

            if (first) {
                response.setHeader(SAMESITE_COOKIE_HEADER, updated);
                first = false;
            } else {
                response.addHeader(SAMESITE_COOKIE_HEADER, updated);
            }
        }
    }

}