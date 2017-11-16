package ru.vyukov.stomp;

import lombok.Data;
import org.springframework.web.socket.WebSocketHttpHeaders;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static java.util.Base64.getEncoder;


@Data
public class WebSocketStompClientProperties {


    private long reconnectDelay = 5_000;

    /**
     * ws://127.0.0.1:8080/endpoint
     */
    @NotNull
    private String url;


    @Valid
    private BasicAuth basicAuth = new BasicAuth();

    @Data
    class BasicAuth {

        @Nullable
        private String username;

        @Nullable
        private String password;

        public boolean isNotEmpty() {
            return null != username && null != password;
        }
    }


    public WebSocketHttpHeaders getHeaders() {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        if (basicAuth.isNotEmpty()) {
            String plainCreds = basicAuth.getUsername() + ":" + basicAuth.getPassword();
            String base64Creds = new String(getEncoder().encodeToString(plainCreds.getBytes()));
            headers.add("Authorization", "Basic " + base64Creds);
        }
        return headers;
    }


}
