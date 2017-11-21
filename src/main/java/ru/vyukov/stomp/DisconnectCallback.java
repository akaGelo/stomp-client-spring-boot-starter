package ru.vyukov.stomp;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Disconnect after connected
 *
 * @author Oleg Vyukov
 */
class DisconnectCallback implements ListenableFutureCallback<StompSession> {
    @Override
    public void onFailure(Throwable ex) {
    }

    @Override
    public void onSuccess(StompSession result) {
        result.disconnect();
    }
};
