package controller;

import DBConnection.DBconnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import tableModel.memberTM;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageMember implements Initializable {
    public AnchorPane root;
    public ImageView btn_home;
    public JFXTextField txt_id;
    public JFXTextField txt_name;
    public JFXTextField txt_address;
    public JFXTextField txt_contact;
    public JFXButton addMember;
    public TableView<memberTM>  tableMember;
    public JFXButton btn_delete;

    private Connection connection;
    private PreparedStatement insertdata;
    private PreparedStatement getdata;
    private PreparedStatement deletedata;
    private PreparedStatement update;
    private PreparedStatement getId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_delete.setDisable(true);
        txt_id.setEditable(false);
        txt_name.setDisable(true);
        txt_address.setDisable(true);
        txt_contact.setDisable(true);


        tableMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tableMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));

        try {
            connection= DBconnection.getInstance().getConnection();
            insertdata = connection.prepareStatement("INSERT INTO member VALUES (?,?,?,?)");
            getdata = connection.prepareStatement("SELECT * FROM member");
            deletedata = connection.prepareStatement("DELETE FROM member WHERE Id=?");
            update = connection.prepareStatement("UPDATE member SET name=?,Address=?,Contact=? WHERE Id=?");
            getId  = connection.prepareStatement("SELECT Id FROM member ORDER by Id DESC LIMIT 1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            tabledataenter();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableMember.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<memberTM>() {
            @Override
            public void onChanged(Change<? extends memberTM> c){
                txt_name.setDisable(false);
                txt_address.setDisable(false);
                txt_contact.setDisable(false);
                addMember.setDisable(false);
                btn_delete.setDisable(false);
                memberTM data = tableMember.getSelectionModel().getSelectedItem();
                addMember.setText("Update");
                if(data != null) {
                    txt_id.setText(data.getId());
                    txt_name.setText(data.getName());
                    txt_address.setText(data.getAddress());
                    txt_contact.setText(data.getContact());
                }

            }
        });

    }

    public void btn_homeOnAction(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getSource() instanceof ImageView){
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

    public void btn_mouseEntered(MouseEvent mouseEvent) {
        ImageView icon = (ImageView)mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D), icon);
        scaleT.setToX(1.2D);
        scaleT.setToY(1.2D);
        scaleT.play();
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);
    }

    public void btn_mouseExited(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D),icon);
        scaleT.setToX(1D);
        scaleT.setToY(1D);
        scaleT.play();
        icon.setEffect(null);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.BLUE);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);
    }

    public void btn_addNewMemberOnAction(ActionEvent actionEvent) {
        txt_id.clear();txt_name.clear();txt_address.clear();txt_contact.clear();
        txt_name.setDisable(false);txt_address.setDisable(false);txt_contact.setDisable(false);
        addMember.setDisable(false);
        addMember.setText("Add Member");
        btn_delete.setDisable(true);

        try {
            ResultSet resultSet = getId.executeQuery();
            if(resultSet.next()){
                String string = resultSet.getString(1);
                String[] split = string.split(":");
                int temp = Integer.parseInt(split[1]);
                temp++;
                if(temp<10) {
                    txt_id.setText("M:00" + temp);
                }
                else if(temp<100) {
                    txt_id.setText("M:0" + temp);
                }
                else if(temp<1000) {
                    txt_id.setText("M:" + temp);
                }
            }
            else{
                txt_id.setText("M:001");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        txt_name.requestFocus();

    }

    public void btn_addMemberOnAction(ActionEvent actionEvent) throws SQLException {
       if(addMember.getText().equals("Add Member")) {

           String id = txt_id.getText();
           String name = txt_name.getText();
           String address = txt_address.getText();
           String contact = txt_contact.getText();

           if (!name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$") || !address.matches("\\S+[a-zA-Z0-9/.,:\\sa-zA-Z0-9]+\\S+") || !contact.matches("^[+]?[0-9]{3}[-]?[0-9]{7}$")) {
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"PLESE ENTER VALID DETAILS",ButtonType.OK);
                        alert.showAndWait();
                        if(!name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")){
                            txt_name.setFocusColor(Color.RED);
                            txt_name.requestFocus();
                        }
                        else if(!address.matches("\\S+[a-zA-Z0-9/.,:\\sa-zA-Z0-9]+\\S+")){
                            txt_address.setFocusColor(Color.RED);
                            txt_address.requestFocus();
                        }
                        else if(!contact.matches("^[+]?[0-9]{3}[-]?[0-9]{7}$")){
                            txt_contact.setFocusColor(Color.RED);
                            txt_contact.requestFocus();
                        }

           } else {

               insertdata.setString(1, id);
               insertdata.setString(2, name);
               insertdata.setString(3, address);
               insertdata.setString(4, contact);
               insertdata.executeUpdate();

               tabledataenter();
               txt_contact.setFocusColor(Color.BLUE);
               txt_address.setFocusColor(Color.BLUE);
               txt_name.setFocusColor(Color.BLUE);

               txt_id.clear();
               txt_name.clear();
               txt_address.clear();
               txt_contact.clear();
               addMember.setDisable(true);
           }
       }

       else if(addMember.getText().equals("Update")){

           txt_id.setDisable(true);
           update.setString(1,txt_name.getText());
           update.setString(2,txt_address.getText());
           update.setString(3,txt_contact.getText());
           update.setString(4,txt_id.getText());

           update.executeUpdate();

           tabledataenter();

           txt_id.clear();
           txt_name.clear();
           txt_address.clear();
           txt_contact.clear();
           addMember.setText("Add Member");
           addMember.setDisable(true);
           btn_delete.setDisable(true);

       }

    }
    public void tabledataenter() throws SQLException {

        tableMember.getItems().clear();

        ObservableList<memberTM> memberdata = tableMember.getItems();
        ResultSet resultSet = getdata.executeQuery();
        while ((resultSet.next())){
           String id = resultSet.getString(1);
           String name = resultSet.getString(2);
           String address = resultSet.getString(3);
           String contact = resultSet.getString(4);
           memberdata.add(new memberTM(id,name,address,contact));
        }
    }

    public void btn_deleteOnAction(ActionEvent actionEvent) throws SQLException {

        Alert alert =new Alert(Alert.AlertType.WARNING,"Do You Wish To Delete This Member",
                ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.YES)) {
            memberTM selected = tableMember.getSelectionModel().getSelectedItem();
            deletedata.setString(1, selected.getId());
            deletedata.executeUpdate();
            tabledataenter();
            btn_delete.setDisable(true);
            addMember.setDisable(true);
            addMember.setText("Add Member");
            txt_id.clear();
            txt_name.clear();
            txt_address.clear();
            txt_contact.clear();
        }

    }


    public void txt_nameOnAction(ActionEvent actionEvent) {
        txt_address.requestFocus();
    }

    public void txt_addressOnAction(ActionEvent actionEvent) {
        txt_contact.requestFocus();
    }

    public void txt_ContactOnAction(ActionEvent actionEvent) {
        addMember.requestFocus();
    }

    public void btnMouseEnter(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D), btn);
        scaleT.setToX(1.1D);
        scaleT.setToY(1.1D);
        scaleT.play();
    }

    public void btnMuseExit(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D), btn);
        scaleT.setToX(1D);
        scaleT.setToY(1D);
        scaleT.play();
    }
}
