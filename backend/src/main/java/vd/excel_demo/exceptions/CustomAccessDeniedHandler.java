package vd.excel_demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import vd.excel_demo.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        String message = Constants.ACCESS_DENIED + Constants.ERROR_CODE + HttpServletResponse.SC_FORBIDDEN;
        LOGGER.error(message + Constants.SPACE + ex.getMessage());
        response.setContentType(Constants.APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(message);
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
}