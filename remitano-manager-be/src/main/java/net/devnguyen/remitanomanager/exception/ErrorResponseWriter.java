package net.devnguyen.remitanomanager.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ErrorResponseWriter {
    private final ObjectMapper objectMapper;

    public ErrorResponseWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeErrorResponse(
            HttpServletResponse response, String contentType, ResponseError error) throws IOException {
        response.setStatus(error.getStatus());
        response.setContentType(contentType);
        PrintWriter responseWriter = response.getWriter();
        responseWriter.write(
                objectMapper.writeValueAsString(error));
        responseWriter.flush();
    }
}
