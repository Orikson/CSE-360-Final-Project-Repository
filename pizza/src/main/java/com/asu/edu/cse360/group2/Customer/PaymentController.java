/**
 * Provides functionality for all Payment screen UI elements
 * @author Eron Ristich
 * @date 10/22/22
 * @version 1.0
 */

package com.asu.edu.cse360.group2.Customer;

// application
import com.asu.edu.cse360.group2.App;
import com.asu.edu.cse360.group2.AppState;
import com.asu.edu.cse360.group2.Pizza;
import com.asu.edu.cse360.group2.Order;

// general imports
import java.io.IOException;
import javafx.fxml.FXML;
import java.util.ArrayList;

// javafx objects
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PaymentController {
    @FXML
    private TableView<Pizza> pizzas;

    @FXML
    private TableColumn<Pizza, String> pizzaColumn;

    @FXML
    private TextField asuField;

    @FXML
    public void initialize() {
        // this function is needed to load elements into the table when the view loads
        pizzaColumn.setCellValueFactory(new PropertyValueFactory<Pizza, String>("name"));
        pizzas.setItems(AppState.CustomerState.pizzaList);

        pizzas.getSelectionModel().selectedItemProperty().addListener((observableList, oldSelection, newSelection) -> {
            // run on table element selection
        });
    }

    @FXML
    public void cancel() throws IOException {
        // return to order page
        App.setRoot("customerorders");
    }

    @FXML
    public void pay() throws IOException {
        boolean success = true;
        if (asuField.getText().length() != 10) {
            success = false;
        }
        int ID = 0;
        try {
            ID = Integer.parseInt(asuField.getText());
        } catch (NumberFormatException nfe) {
            success = false;
        }

        if (!success) {
            Alert alert = new Alert(AlertType.ERROR, "Invalid ASU ID");
            alert.show();
            return;
        }

        AppState.CustomerState.currentUserID = ID;

        // cache order in order database, set app state pizzaList to null
        // this copies by reference, which is fine, because we won't use the old list
        // anymore anyways
        Order order = new Order(new ArrayList<>(pizzas.getItems()), AppState.CustomerState.currentUserID);
        if (AppState.orders.get(ID) == null) {
            ArrayList<Order> orders = new ArrayList<Order>();
            orders.add(order);
            AppState.orders.put(ID, orders);
        } else {
            ArrayList<Order> orders = AppState.orders.get(ID);
            orders.add(order);
        }
        if (AppState.newOrders.get(ID) == null) {
            ArrayList<Order> orders = new ArrayList<Order>();
            orders.add(order);
            AppState.newOrders.put(ID, orders);
        } else {
            ArrayList<Order> orders = AppState.newOrders.get(ID);
            orders.add(order);
        }

        AppState.CustomerState.pizzaList = null;
        App.setRoot("customeroverview");
    }
}