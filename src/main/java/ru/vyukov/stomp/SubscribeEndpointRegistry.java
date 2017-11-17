package ru.vyukov.stomp;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class SubscribeEndpointRegistry {

    private ConcurrentMap<String, SubscribeMethodInstance> subscribeMethodsInstances = new ConcurrentHashMap<>();


    public void add(SubscribeMethodInstance subscribeMethodInstance) {
        String destination = subscribeMethodInstance.getDestination();
        if (subscribeMethodsInstances.containsKey(destination)) {
            SubscribeMethodInstance existedMethod = subscribeMethodsInstances.get(destination);
            throw new IllegalStateException("Destination " + destination + " already binded to " + existedMethod);
        }
        subscribeMethodsInstances.put(destination, subscribeMethodInstance);
    }

    /**
     * @param destination
     * @return
     * @throws NullPointerException if no @Subscribe method on destination
     */
    @NotNull
    public SubscribeMethodInstance getMethod(String destination) {
        SubscribeMethodInstance instance = subscribeMethodsInstances.get(destination);
        if (null == instance) {
            throw new NullPointerException("No method for " + destination);
        }
        return instance;
    }

    public void addAll(List<SubscribeMethodInstance> methodInstances) {
        methodInstances.forEach(this::add);
    }

    public Set<String> getAllDestination() {
        return subscribeMethodsInstances.keySet();
    }
}
