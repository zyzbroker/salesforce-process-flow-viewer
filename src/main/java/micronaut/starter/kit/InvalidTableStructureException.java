package micronaut.starter.kit;

public class InvalidTableStructureException extends  Exception {
    public InvalidTableStructureException(String message) {
        super(message);
    }
}
