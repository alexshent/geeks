package alexshent.assignment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Math expression database access object
 */
public class ExpressionDAO {
    private final static int idIndex = 1;
    private final static int bodyIndex = 2;
    private final static int valueIndex = 3;

    /**
     * Read all records from DB
     *
     * @return list of all expression records
     */
    public static List<Expression> readAll() {
        List<Expression> result = new ArrayList<>();
        String query = "SELECT * FROM expressions ORDER BY created_at DESC";
        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                result.add(new Expression(
                                resultSet.getString(idIndex),
                                resultSet.getString(bodyIndex),
                                resultSet.getDouble(valueIndex)
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Save new expression record to DB
     *
     * @param expression new record to save
     */
    public static void create(Expression expression) {
        String uuid = UUID.randomUUID().toString();
        String format = "INSERT INTO expressions (id, body, value) VALUES ('%s', '%s', %f)";
        String query = String.format(format, uuid, expression.body(), expression.value());
        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update existing expression record in DB
     *
     * @param expression record to update
     */
    public static void update(Expression expression) {
        String format = "UPDATE expressions SET body = '%s', value = %f WHERE id = '%s'";
        String query = String.format(format, expression.body(), expression.value(), expression.id());
        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete expression record from DB
     *
     * @param id record with this id will be deleted
     */
    public static void delete(String id) {
        String format = "DELETE FROM expressions WHERE id = '%s'";
        String query = String.format(format, id);
        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find expression record by its value in DB
     *
     * @param value      target value
     * @param comparator search comparator
     * @return list of found records
     */
    public static List<Expression> findByValue(String value, String comparator) {
        List<Expression> result = new ArrayList<>();
        String format = "SELECT * FROM expressions WHERE value %s %s ORDER BY created_at DESC";
        String query = String.format(format, comparator, value);
        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                result.add(new Expression(
                                resultSet.getString(idIndex),
                                resultSet.getString(bodyIndex),
                                resultSet.getDouble(valueIndex)
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
