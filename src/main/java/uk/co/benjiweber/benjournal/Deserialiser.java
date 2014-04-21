package uk.co.benjiweber.benjournal;

public interface Deserialiser<T> {
    T deserialize(String str);
}
