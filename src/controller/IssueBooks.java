package controller;

import DBConnection.DBconnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import tableModel.issuBookTM;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class IssueBooks implements Initializable {
    public AnchorPane root;
    public ImageView btn_home;
    public JFXDatePicker datepicker;
    public JFXTextField orderId;
    public JFXTextField memberName;
    public JFXTextField bookName;
    public JFXButton btn_addBook;
    public TableView<issuBookTM> tableIssueBook;
    public JFXComboBox memberId;
    public JFXComboBox bookId;

    private Connection connection;
    private PreparedStatement enterdata;
    private PreparedStatement getbookId;
    private PreparedStatement getmemberId;
    private PreparedStatement getmembername;
    private PreparedStatement getbookname;
    private PreparedStatement tableAdd;
    private PreparedStatement deleteorder;
    private PreparedStatement updatetable;
    private PreparedStatement setQty;
    private PreparedStatement getQty;
    private PreparedStatement getIssueId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderId.setEditable(false);


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (connection != null){
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
       }));

        tableIssueBook.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        tableIssueBook.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableIssueBook.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tableIssueBook.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerId"));

        datepicker.setDisable(true);
        orderId.setDisable(true);
        memberName.setDisable(true);
        bookName.setDisable(true);
        bookId.setDisable(true);
        memberId.setDisable(true);

        LocalDate localdate = LocalDate.now();
        datepicker.setValue(localdate);

        try {
            Connection connection = DBconnection.getInstance().getConnection();
            enterdata = connection.prepareStatement("INSERT INTO issue_book VALUES (?,?,?,?)");
            getbookId = connection.prepareStatement("SELECT bookId FROM manage_book");
            getmemberId = connection.prepareStatement("SELECT Id FROM member");
            getmembername = connection.prepareStatement("SELECT name FROM member WHERE Id=?");
            getbookname =  connection.prepareStatement("SELECT bookName FROM manage_book WHERE bookId=?");
            tableAdd=connection.prepareStatement("SELECT * FROM issue_book");
            deleteorder=connection.prepareStatement("DELETE FROM issue_book WHERE orderId=?");
            updatetable = connection.prepareStatement("UPDATE issue_book set bookId=?,memberId=? WHERE orderId=?");
            setQty = connection.prepareStatement("UPDATE manage_book SET bookQty=(bookQty-1) WHERE bookId=?");
            getQty = connection.prepareStatement("SELECT bookQty FROM manage_book WHERE bookId=?");
            getIssueId=connection.prepareStatement("SELECT orderId FROM issue_book  order by orderId DESC LIMIT 1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet resultSet =getbookId.executeQuery();
            ResultSet result =getmemberId.executeQuery();
            ObservableList<String> combo1 = bookId.getItems();
            ObservableList<String> combo2 = memberId.getItems();
            while(resultSet.next()){
                String id = resultSet.getString(1);
                combo1.add(id);
            }
            while(result.next()){
                String mid = result.getString(1);
                combo2.add(mid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            tableadddata();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableIssueBook.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<issuBookTM>() {
            @Override
            public void onChanged(Change<? extends issuBookTM> c) {
                issuBookTM myvalue = tableIssueBook.getSelectionModel().getSelectedItem();
                btn_addBook.setText("Update");

                if(myvalue!=null) {
                    //datepicker.setValue(myvalue.getDate());
                    orderId.setText(myvalue.getOrderId());
                    memberId.setValue(myvalue.getCustomerId());
                    bookId.setValue(myvalue.getBookId());
                    datepicker.setDisable(false);
                    // orderId.setDisable(false);
                    memberName.setDisable(true);
                    bookName.setDisable(true);
                    bookId.setDisable(false);
                    memberId.setDisable(false);
                }
            }
        });


    }

    public void btn_issueBookOnAction(ActionEvent actionEvent) {
        datepicker.setDisable(false);
        orderId.setDisable(false);

        memberName.setDisable(false);
        bookName.setDisable(false);
        memberName.setEditable(false);
        bookName.setEditable(false);

        bookId.setDisable(false);
        memberId.setDisable(false);
        orderId.clear();
        memberName.clear();
        bookName.clear();
        memberName.clear();
        btn_addBook.setText("Save");

        try {
            ResultSet resultSet = getIssueId.executeQuery();
            if(resultSet.next()){
                String s = resultSet.getString(1);
                String[] split = s.split(":");
                int i = Integer.parseInt(split[1]);
                i++;
                orderId.setText("OD:"+i);
            }
            else{
                orderId.setText("OD:001");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btn_addBookOnAction(ActionEvent actionEvent) throws SQLException {
            if(!btn_addBook.getText().equals("Update")) {
                getQty.setObject(1, String.valueOf(bookId.getSelectionModel().getSelectedItem()));
                 ResultSet risult = getQty.executeQuery();
                 risult.next();
                if(risult.getInt(1) == 0 ){
                    new Alert(Alert.AlertType.WARNING,"Our Library has Not This book Found For Issue",ButtonType.OK).show();
                    }
                   else{

                    enterdata.setDate(1, java.sql.Date.valueOf(datepicker.getValue()));
                    enterdata.setString(2, orderId.getText());
                    enterdata.setString(3, (String) bookId.getSelectionModel().getSelectedItem());
                    enterdata.setString(4, (String) memberId.getSelectionModel().getSelectedItem());
                    enterdata.executeUpdate();
                    tableadddata();
                    setQty.setString(1, String.valueOf(bookId.getSelectionModel().getSelectedItem()));
                    setQty.executeUpdate();
                }
            }
            else{

                updatetable.setString(1, (String) bookId.getSelectionModel().getSelectedItem());
                updatetable.setString(2, (String) memberId.getSelectionModel().getSelectedItem());
                updatetable.setString(3,orderId.getText());
                updatetable.executeUpdate();
                tableadddata();

            }
    }

    public void btn_deleteBookOnAction(ActionEvent actionEvent) throws SQLException {
        Alert alert =new Alert(Alert.AlertType.WARNING,"Do You Wish To Delete This Member",
                ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.YES)) {
            issuBookTM issues = tableIssueBook.getSelectionModel().getSelectedItem();
            deleteorder.setString(1, issues.getOrderId());
            deleteorder.executeUpdate();
            tableadddata();
        }

    }

    public void tableadddata() throws SQLException {
        tableIssueBook.getItems().clear();
        ObservableList<issuBookTM> getdata = tableIssueBook.getItems();
        ResultSet resultSet = tableAdd.executeQuery();
        while (resultSet.next()){
            Date date = resultSet.getDate(1);
            String orid = resultSet.getString(2);
            String bkid = resultSet.getString(3);
            String  memid = resultSet.getString(4);
            getdata.add(new issuBookTM(date,orid,bkid,memid));
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

    public void selectedMemberId(ActionEvent actionEvent) throws SQLException {
        String selectedmember = (String) memberId.getSelectionModel().getSelectedItem();
        getmembername.setString(1,selectedmember);
         ResultSet getname = getmembername.executeQuery();
         while(getname.next()){
            String valu1 =getname.getString(1);
            memberName.setText(valu1);
         }

    }

    public void selectedBookId(ActionEvent actionEvent) throws SQLException {
        String selectedmember = (String) bookId.getSelectionModel().getSelectedItem();
        getbookname.setString(1,selectedmember);
        ResultSet getId = getbookname.executeQuery();
        while(getId.next()){
            String valu1 =getId.getString(1);
            bookName.setText(valu1);
        }
    }
}
