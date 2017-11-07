package Protocols;

import java.util.Optional;

public interface Invertable<T> {

    public Optional<T> getInverse();

}
