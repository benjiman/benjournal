package uk.co.benjiweber.benjournal;

public interface Serializer<T> {
    String serialize(T value);
}
