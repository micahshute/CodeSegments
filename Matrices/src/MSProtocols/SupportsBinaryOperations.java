package MSProtocols;

import java.util.Optional;

public interface SupportsBinaryOperations {

    public abstract SupportsBinaryOperations unityValueInstance();
    public abstract SupportsBinaryOperations zeroValueInstance();

    public abstract Optional<SupportsBinaryOperations> add();
    public abstract Optional<SupportsBinaryOperations> subtract();
    public abstract Optional<SupportsBinaryOperations> multiply();
    public abstract Optional<SupportsBinaryOperations> divide();

}
