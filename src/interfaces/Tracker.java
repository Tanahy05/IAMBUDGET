package interfaces;

/**
 * Tracker interface that serves as the foundation for tracking functionality.
 * This interface defines the common operations for tracking objects in the system.
 */
public interface Tracker<T> {

    /**
     * Adds a new item to be tracked.
     */
    void add(T item);

    /**
     * Edits an existing tracked item.
     */
    void edit(T item);

    /**
     * Deletes a tracked item.
     *
     * @param id The identifier of the item to delete
     */
    void delete(String id);

    /**
     * Validates an item before adding or editing.
     *
     * @param item The item to validate
     * @return true if the item is valid, false otherwise
     */
    boolean validate(T item);

    /**
     * Calculates the amount based on tracked items.
     *
     * @return The calculated decimal amount
     */
    java.math.BigDecimal calculateAmount();
}