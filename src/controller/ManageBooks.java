package controller;

import DBConnection.DBconnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tableModel.ManageBookTM;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageBooks implements Initializable {
    public ImageView home;
    public JFXButton btn_AddBook;
    public JFXTextField bookId;
    public JFXTextField bookName;
    public JFXTextField bookAuther;
    public JFXTextField bookPrice;
    public JFXTextField bookQty;
    public TableView<ManageBookTM> tableBookData;
    public JFXButton btn_addBookToLibrary;
    public JFXButton btn_delete;
    public AnchorPane root;

    private Connection connection;
    private PreparedStatement insertdata;
    private PreparedStatement getdata;
    private PreparedStatement delete;
    private PreparedStatement update;
    private PreparedStatement getBookId;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookId.setEditable(false);
        bookName.setDisable(true);
        bookAuther.setDisable(true);
        bookPrice.setDisable(true);
        bookQty.setDisable(true);

        tableBookData.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableBookData.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableBookData.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("Auther"));
        tableBookData.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("price"));
        tableBookData.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tableBookData.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("exactQty"));

        try {
            connection= DBconnection.getInstance().getConnection();
            insertdata = connection.prepareStatement("INSERT INTO manage_book VALUES(?,?,?,?,?,?)");
            getdata = connection.prepareStatement("SELECT * FROM manage_book");
            delete = connection.prepareStatement("DELETE FROM manage_book WHERE bookId=?");
            update = connection.prepareStatement("UPDATE manage_book SET bookName=?,bookAuther=?,bookPrice=?,bookQty=? WHERE bookId=?");
            getBookId = connection.prepareStatement("SELECT bookId FROM manage_book ORDER BY bookId DESC LIMIT 1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            inserttable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableBookData.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ManageBookTM>() {
            @Override
            public void onChanged(Change<? extends ManageBookTM> c) {
                bookName.setDisable(false);
                bookAuther.setDisable(false);
                bookPrice.setDisable(false);
                bookQty.setDisable(false);

                btn_addBookToLibrary.setText("Update");

                ManageBookTM mylist = tableBookData.getSelectionModel().getSelectedItem();
               if (mylist!=null){
                   bookId.setText(mylist.getId());
                   bookName.setText(mylist.getName());
                   bookAuther.setText(mylist.getAuther());
                   bookPrice.setText(String.valueOf(mylist.getPrice()));
                   bookQty.setText(String.valueOf(mylist.getQty()));

               }
            }
        });
    }

    public void btn_AddBookOnAction(ActionEvent actionEvent) {
        bookId.clear();
        bookName.clear();
        bookAuther.clear();
        bookPrice.clear();
        bookQty.clear();

        bookId.setDisable(false);
        bookName.setDisable(false);
        bookAuther.setDisable(false);
        bookPrice.setDisable(false);
        bookQty.setDisable(false);

        btn_addBookToLibrary.setDisable(false);
        btn_addBookToLibrary.setText("Add Book To Library");

        try {
            ResultSet resultSet = getBookId.executeQuery();
            if(resultSet.next()) {
                String string = resultSet.getString(1);
                String[] split = string.split(":");
                int s = Integer.parseInt(split[1]);
                 s++;
                bookId.setText("BK:00"+s);
            }
            else{
                bookId.setText("BK:001");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void btn_addBookToLibraryOnAction(ActionEvent actionEvent) throws SQLException {
        if (btn_addBookToLibrary.getText().equals("Add Book To Library")) {

            String NbookId = bookId.getText();
            String NbookName = bookName.getText();
            String NbookAuther = bookAuther.getText();
            int NbookPrice = Integer.parseInt(bookPrice.getText());
            int NbookQty = Integer.parseInt(bookQty.getText());

            insertdata.setString(1, NbookId);
            insertdata.setString(2, NbookName);
            insertdata.setString(3, NbookAuther);
            insertdata.setInt(4, NbookPrice);
            insertdata.setInt(5, NbookQty);
            insertdata.setInt(6,NbookQty);

            insertdata.executeUpdate();
            inserttable();
            bookId.clear();
            bookName.clear();
            bookAuther.clear();
            bookPrice.clear();
            bookQty.clear();

            btn_addBookToLibrary.setDisable(true);

        } else if (btn_addBookToLibrary.getText().equals("Update")) {
            update.setString(1, bookName.getText());
            update.setString(2, bookAuther.getText());
            update.setString(3, bookPrice.getText());
            update.setString(4, bookQty.getText());
            update.setString(5, bookId.getText());

            update.executeUpdate();
            inserttable();
        }


    }

    public void inserttable() throws SQLException {

        tableBookData.getItems().clear();

        ObservableList<ManageBookTM> memberdata = tableBookData.getItems();
        ResultSet resultSet = getdata.executeQuery();
        while ((resultSet.next())) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String auther = resultSet.getString(3);
            int price = Integer.parseInt(resultSet.getString(4));
            int qty = Integer.parseInt(resultSet.getString(5));
            int exactQty = Integer.parseInt(resultSet.getString(6));
            memberdata.add(new ManageBookTM(id, name, auther, price, qty,exactQty));
        }
    }

    public void btn_deleteOnAction(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Do You Wish To Delete This Member",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            ManageBookTM mylist = tableBookData.getSelectionModel().getSelectedItem();
            String deletecoloumn = mylist.getId();
            delete.setString(1, deletecoloumn);
            delete.executeUpdate();
            inserttable();
        }
    }

    public void homeOnAction(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            Parent root = null;
            ImageView imageView = (ImageView) mouseEvent.getSource();
            root = FXMLLoader.load(this.getClass().getResource("../fxml/sample.fxml"));

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();
            }
        }
    }
}
