package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    public JFXTextField reservedAmount;
    public JFXTextField balance;
    public Button btn_Ok;
    public AnchorPane root;

    private int  temp;
    int receve;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reservedAmount.setText("0");
        reservedAmount.textProperty().addListener((observable, oldValue, newValue) -> {

            receve = Integer.parseInt(reservedAmount.getText());
            balance.setText(String.valueOf(receve-temp));
        });
    }

    public void setTotal(double value) {
        temp = (int) value;
        System.out.println(temp);
    }

    public void btn_OkOnAction(ActionEvent actionEvent) throws SQLException, IOException {

    }
}
