package ru.vyukov.stomp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketHttpHeaders;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static java.util.Base64.getEncoder;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StompClientProperties {


    private long reconnectDelay = 5_000;

    /**
     * example ws://127.0.0.1:8080/endpoint
     */
    @NotNull
    private String url;


    /**
     * specified if necessary
     */
    @Valid
    private BasicAuth basicAuth = new BasicAuth();

    @Data
    static  class BasicAuth {

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
