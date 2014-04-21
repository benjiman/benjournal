package uk.co.benjiweber.benjournal;

import java.util.HashMap;
import java.util.Map;

class SerializerMap<T> {
    private Map<Class<T>, Serializer<T>> serializers = new HashMap<>();

    public void add(Class<T> cls, Serializer<T> serializer) {
    }
}
