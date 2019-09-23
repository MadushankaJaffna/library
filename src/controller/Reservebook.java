package controller;

import DBConnection.DBconnection;
import com.jfoenix.controls.*;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.animation.TranslateTransition;
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
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;
import tableModel.reservedBookTM;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;

import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Reservebook implements Initializable {
    public ImageView btn_home;
    public JFXComboBox orderId;
    public JFXTextField bookId;
    public JFXTextField bookName;
    public JFXTextField memberName;
    public JFXDatePicker ReserveDate;
    public JFXTextArea note;
    public TableView<reservedBookTM> tbl_reserveBook;
    public JFXTextField price;
    public JFXTextField additionalPrice;
    public JFXTextField totalPrice;
    public JFXButton btn_getReturnBook;
    public AnchorPane root;
    public JFXTextField issuedDate;
    private Connection connection;


    private PreparedStatement databaseAdd;
    private PreparedStatement addData;
    private PreparedStatement setOrderId;
    private PreparedStatement getissuedDate;
    private PreparedStatement datediff;
    private PreparedStatement chooseMember;
    private PreparedStatement chooseBookName;
    private PreparedStatement deleteFromIssueBook;
    private PreparedStatement deleteFromReserveBook;
    private PreparedStatement setQty;

    String getIdFromCombo;
    LocalDate localDate1=LocalDate.now();
    int mynewdifference;
    public int total;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        bookId.setDisable(true);bookName.setDisable(true);memberName.setDisable(true);issuedDate.setDisable(true);
        ReserveDate.setDisable(true);note.setDisable(true);price.setDisable(true);additionalPrice.setDisable(true);
        totalPrice.setDisable(true);

        tbl_reserveBook.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tbl_reserveBook.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("bookName"));
        tbl_reserveBook.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tbl_reserveBook.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("memberName"));
        tbl_reserveBook.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        tbl_reserveBook.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("recervedDate"));
        tbl_reserveBook.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("note"));
        tbl_reserveBook.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("additionalPrice"));
        tbl_reserveBook.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("totalPrice"));


        price.setText("20");

        try {
            connection= DBconnection.getInstance().getConnection();
            addData = connection.prepareStatement("SELECT * FROM reserve_book");
            databaseAdd = connection.prepareStatement("INSERT INTO reserve_book VALUES(?,?,?,?,?,?,?,?,?)");
            setOrderId = connection.prepareStatement("SELECT orderId FROM issue_book");
            getissuedDate = connection.prepareStatement("SELECT dateissue,bookId from issue_book WHERE orderId = ? ");
            datediff = connection.prepareStatement("SELECT DATEDIFF(CURDATE(),dateissue) FROM issue_book WHERE orderId=?;");
            chooseMember=connection.prepareStatement("SELECT member.name FROM member join issue_book b on member.Id = b.memberId where orderId=?");
            chooseBookName = connection.prepareStatement("SELECT m.bookName from manage_book m join issue_book b on m.bookId = b.bookId where orderId=?");
            deleteFromIssueBook = connection.prepareStatement("DELETE FROM issue_book WHERE orderId = ?");
            deleteFromReserveBook = connection.prepareStatement("DELETE FROM reserve_book WHERE orderId=? AND bookId=?");
            setQty = connection.prepareStatement("UPDATE manage_book SET bookQty=(bookQty+1) WHERE bookId=?");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            add_tableValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            ObservableList<String> orderIdCmbo = orderId.getItems();
            ResultSet resultSet=setOrderId.executeQuery();
            while(resultSet.next()){
               orderIdCmbo.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ReserveDate.setValue(localDate1);

    }

    public void selectOrderID(ActionEvent actionEvent) throws SQLException, ParseException {

        bookId.setDisable(false);bookName.setDisable(false);memberName.setDisable(false);issuedDate.setDisable(false);
        price.setDisable(false);additionalPrice.setDisable(false);
        totalPrice.setDisable(false);

        bookId.setEditable(false);bookName.setEditable(false);memberName.setEditable(false);issuedDate.setEditable(false);
        price.setEditable(false);additionalPrice.setEditable(false);
        totalPrice.setEditable(false);

        LocalDate localDate= LocalDate.now();
        int dada=0;
        note.setDisable(false);
        additionalPrice.clear();
        totalPrice.clear();

        getIdFromCombo = (String) orderId.getSelectionModel().getSelectedItem();

        getissuedDate.setString(1, getIdFromCombo);
        datediff.setString(1,getIdFromCombo);
        chooseMember.setString(1,getIdFromCombo);
        chooseBookName.setString(1,getIdFromCombo);
        ResultSet resultSet = getissuedDate.executeQuery();
        ResultSet resultSet2 = datediff.executeQuery();
        ResultSet resultSet3 = chooseMember.executeQuery();
        ResultSet resultSet4 = chooseBookName.executeQuery();

        while (resultSet.next()) {
            Date ISSUED = resultSet.getDate(1);
            String bkid = resultSet.getString(2);
            bookId.setText(bkid);
            issuedDate.setText(String.valueOf(ISSUED));

        }
        while (resultSet2.next()){
            int difference=resultSet2.getInt(1);
                total=20;
           if(difference>7){
                mynewdifference = (difference-7)*5;
                total = mynewdifference+20;
                additionalPrice.setText(String.valueOf(mynewdifference));
                totalPrice.setText(String.valueOf(total));
           }
           else {
               totalPrice.setText("20");
           }
        }
        while (resultSet3.next()){
            memberName.setText(resultSet3.getString(1));
        }
        while (resultSet4.next()){
            bookName.setText(resultSet4.getString(1));
        }
    }

    public void btn_getReturnBookOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Accept The Book Clean and Without Damage ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {

            FXMLLoader loader= new FXMLLoader(this.getClass().getResource("/fxml/login.fxml"));
            Parent p=loader.load();
            loginController controller = loader.getController();
            controller.setTotal(total);
            Stage s= new Stage();
            s.setScene(new Scene(p));
            s.show();


            databaseAdd.setString(3, getIdFromCombo);
            databaseAdd.setString(1, bookId.getText());
            databaseAdd.setString(2, bookName.getText());
            databaseAdd.setString(4, memberName.getText());
            databaseAdd.setString(5, issuedDate.getText());
            databaseAdd.setString(6, localDate1.toString());
            databaseAdd.setString(7, note.getText());
            databaseAdd.setInt(8, mynewdifference);
            databaseAdd.setInt(9, Integer.parseInt(totalPrice.getText()));

            databaseAdd.executeUpdate();

            deleteFromIssueBook.setString(1, getIdFromCombo);
            deleteFromIssueBook.executeUpdate();

            add_tableValue();

            setQty.setString(1,bookId.getText());
            setQty.executeUpdate();

            orderId.getItems().remove(getIdFromCombo);
            bookId.clear();
            bookName.clear();
            memberName.clear();
            issuedDate.clear();
            note.clear();
            additionalPrice.clear();
            totalPrice.clear();

        }
    }
    public void btn_deleteOnAction(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.WARNING, "DO YOU WISH TO DELETE THIS RESERVE BOOK ITEM ?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {

            reservedBookTM reservedbook = tbl_reserveBook.getSelectionModel().getSelectedItem();
            System.out.println(reservedbook.getBookId());
            System.out.println(reservedbook.getOrderId());
            deleteFromReserveBook.setString(1, reservedbook.getOrderId());
            deleteFromReserveBook.setString(2, reservedbook.getBookId());
            deleteFromReserveBook.executeUpdate();
            add_tableValue();
        }

    }

    public void add_tableValue() throws SQLException {
        ObservableList<reservedBookTM> list = tbl_reserveBook.getItems();
        tbl_reserveBook.getItems().clear();
        ResultSet resultSet = addData.executeQuery();
        while (resultSet.next()){
            list.add(new reservedBookTM(resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getString(3),
                                        resultSet.getString(4),
                                        resultSet.getDate(5),
                                        resultSet.getDate(6),
                                        resultSet.getString(7),
                                        resultSet.getInt(8),
                                        resultSet.getInt(9)));
        }

    }


    public void btn_homeOnAction(MouseEvent mouseEvent) throws IOException {
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
