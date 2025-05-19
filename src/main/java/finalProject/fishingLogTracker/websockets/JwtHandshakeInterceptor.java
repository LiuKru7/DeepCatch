package finalProject.fishingLogTracker.websockets;

import finalProject.fishingLogTracker.auth.service.JwtService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;


/**
 * Intercepts the WebSocket handshake to extract and validate a JWT token.
 * If the token is valid, the extracted username is stored in the session attributes.
 */
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    /**
     * Constructs a JwtHandshakeInterceptor with the required JwtService.
     *
     * @param jwtService the service used to validate and extract information from JWT tokens
     */
    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Intercepts the handshake before the WebSocket connection is established.
     * Validates the JWT token passed as a URL parameter. If valid, extracts
     * the username and stores it in the session attributes.
     *
     * @param request    the HTTP request
     * @param response   the HTTP response
     * @param wsHandler  the WebSocket handler
     * @param attributes the attributes to pass to the WebSocket session
     * @return true to proceed with the handshake
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("token");

            if (token != null && jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                log.info("WebSocket handshake authorized for user: {}", username);
                attributes.put("username", username);
            } else {
                log.warn("WebSocket handshake failed: invalid or missing token");
            }
        }

        return true;
    }

    /**
     * Called after the handshake is done. No additional logic implemented.
     *
     * @param request   the HTTP request
     * @param response  the HTTP response
     * @param wsHandler the WebSocket handler
     * @param exception an exception raised during handshake, if any
     */
    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}
