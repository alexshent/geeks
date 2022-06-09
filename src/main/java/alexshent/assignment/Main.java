package alexshent.assignment;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    private final static int stageWidth = 800;
    private final static int stageHeight = 750;
    private final static int idColumnWidth = 300;
    private final static int bodyColumnWidth = 300;
    private final static int valueColumnWidth = 100;
    private final static int searchTextAreaWidth = 200;
    private final static int searchTextAreaHeight = 100;
    private final static int hBoxSpacing = 10;
    private final static int vBoxSpacing = 10;

    private final TableView<Expression> table = new TableView<>();
    private final TextArea searchTextArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Group());
        stage.setTitle("Assignment");
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        Label label = new Label("Expressions");
        label.setFont(new Font("Arial", 20));

        // id column
        TableColumn <Expression, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(idColumnWidth);
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().id()));
        // body column
        TableColumn <Expression, String> bodyColumn = new TableColumn<>("body");
        bodyColumn.setMinWidth(bodyColumnWidth);
        bodyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().body()));
        bodyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyColumn.setOnEditCommit(event -> {
            // edit expression body
            int index = event.getTablePosition().getRow();
            Expression expression = event.getTableView().getItems().get(index);
            String newBody = event.getNewValue();
            if (Tokenizer.isValid(newBody)) {
                updateRecord(new Expression(expression.id(), newBody, 0));
                double value = Parser.evaluate(newBody);
                event.getTableView().getItems().set(index, new Expression(expression.id(), newBody, value));
            } else {
                errorAlert("Error", "invalid expression");
                table.refresh();
            }
        });
        // value column
        TableColumn <Expression, String> valueColumn = new TableColumn<>("value");
        valueColumn.setMinWidth(valueColumnWidth);
        valueColumn.setCellValueFactory(data -> new SimpleStringProperty(Double.toString(data.getValue().value())));

        table.getColumns().addAll(idColumn, bodyColumn, valueColumn);
        table.setEditable(true);
        loadTable();

        // delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Expression expression = table.getSelectionModel().getSelectedItem();
            if (expression != null) {
                deleteRecord(expression.id());
                table.getItems().remove(expression);
            }
        });

        HBox tableHBox = new HBox();
        tableHBox.setAlignment(Pos.CENTER);
        tableHBox.setSpacing(hBoxSpacing);
        tableHBox.getChildren().addAll(table, deleteButton);

        // add body field
        TextField addBody = new TextField();
        addBody.setMaxWidth(bodyColumn.getMinWidth());
        addBody.setPromptText("expression");
        // add button
        final Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            addRecord(addBody.getText());
            addBody.clear();
        });
        // add hBox
        HBox addHBox = new HBox();
        addHBox.getChildren().addAll(addBody, addButton);
        addHBox.setSpacing(hBoxSpacing);

        // search field
        TextField searchValue = new TextField();
        searchValue.setPromptText("value");
        // comparator choicebox
        ChoiceBox<String> comparatorChoiceBox = new ChoiceBox<>();
        comparatorChoiceBox.getItems().addAll("=", ">", "<", ">=", "<=");
        comparatorChoiceBox.setValue("=");
        // search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction( event -> {
            searchRecords(searchValue.getText(), comparatorChoiceBox.getValue());
        });
        // search hBox
        HBox searchHBox = new HBox();
        searchHBox.getChildren().addAll(searchValue, comparatorChoiceBox, searchButton);
        searchHBox.setSpacing(hBoxSpacing);
        // search text area
        searchTextArea.setMinWidth(searchTextAreaWidth);
        searchTextArea.setMinHeight(searchTextAreaHeight);
        // search vbox
        VBox searchVBox = new VBox();
        searchVBox.getChildren().addAll(searchHBox, searchTextArea);
        searchVBox.setSpacing(vBoxSpacing);
        // main vertical box
        VBox vbox = new VBox();
        vbox.setSpacing(vBoxSpacing);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, tableHBox, addHBox, searchVBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        scene.getStylesheets().add("styles.css");

        stage.setScene(scene);
        stage.show();
    }

    private void loadTable() {
        ObservableList<Expression> data = FXCollections.observableArrayList(ExpressionDAO.readAll());
        table.setItems(data);
    }

    private void addRecord(String body) {
        if (Tokenizer.isValid(body)) {
            double value = Parser.evaluate(body);
            ExpressionDAO.create(new Expression(null, body, value));
            loadTable();
        } else {
            errorAlert("Error", "invalid expression");
        }
    }

    private void updateRecord(Expression expression) {
        if (Tokenizer.isValid(expression.body())) {
            double value = Parser.evaluate(expression.body());
            ExpressionDAO.update(new Expression(expression.id(), expression.body(), value));
        } else {
            errorAlert("Error", "invalid expression");
        }
    }

    private void deleteRecord(String id) {
        if (id != null) {
            ExpressionDAO.delete(id);
        }
    }

    private void searchRecords(String value, String comparator) {
        Pattern pattern = Pattern.compile("[\\d\\-+.]+");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            List<Expression> items = ExpressionDAO.findByValue(value, comparator);
            searchTextArea.clear();
            if (items.size() > 0) {
                for (Expression expression : items) {
                    searchTextArea.appendText(expression.id() + " " + expression.body() + " " + expression.value() + "\n");
                }
            }
        } else {
            errorAlert("Error", "invalid value");
        }
    }

    private void errorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
