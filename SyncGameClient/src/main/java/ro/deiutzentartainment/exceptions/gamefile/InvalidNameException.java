package ro.deiutzentartainment.exceptions.gamefile;

public class InvalidNameException extends InvalidDataException {
    public InvalidNameException(int i) {
        super("Invalid name on the line" + i);
    }
    public InvalidNameException(String name){
        super("Invalid name " + name);
    }
}
