package models;

/**
 *
 * @author ataulislam.raihan
 */
public enum OrderStatus {
    ORDERED('o'), DELIVERED('d');
    
    public char toChar() {
        return asChar;
    }

    private final char asChar;

    private OrderStatus(char asChar) {
        this.asChar = asChar;
    }
}
