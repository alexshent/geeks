package alexshent.assignment;

/**
 * Math expression record
 *
 * @param id    database id
 * @param body  expression body
 * @param value expression value
 */
public record Expression(String id, String body, double value) {
}
