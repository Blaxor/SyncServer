package ro.deiutzentartainment.exceptions.gamefile;

public class InvalidSavePathException extends InvalidDataException {
    public InvalidSavePathException(int i) {
        super("Invalid name on the line" + i);
    }
}
