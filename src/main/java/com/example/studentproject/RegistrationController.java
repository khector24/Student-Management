package com.example.studentproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationController implements Initializable {
    @FXML
    public Button add_btn;
    @FXML
    public Button update_btn;
    @FXML
    public Button delete_btn;
    @FXML
    public TextField name_txtfld;
    @FXML
    public TextField phone_txtfld;
    @FXML
    public TextField course_txtfld;
    @FXML
    public TableView<Student> table;
    @FXML
    public TableColumn<Student, String> ID_column;
    @FXML
    public TableColumn<Student, String> name_column;
    @FXML
    public TableColumn<Student, String> phone_column;
    @FXML
    public TableColumn<Student, String> course_column;

    Connection con;
    PreparedStatement pst;
    int myIndex;
    int id;


    @FXML
    public void Add() {
        String st_name;
        String phone;
        String course;

        st_name = name_txtfld.getText();
        phone = phone_txtfld.getText().replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        course = course_txtfld.getText();
        course = course.substring(0, 1).toUpperCase() + course.substring(1);

        try {
            pst = con.prepareStatement("INSERT INTO registration(name,phone,course)values(?,?,?)");
            pst.setString(1, st_name);
            pst.setString(2, phone);
            pst.setString(3, course);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registration");

            alert.setHeaderText("Student Registration");
            alert.setContentText("Record Added!");

            alert.showAndWait();

            table();

            name_txtfld.setText("");
            phone_txtfld.setText("");
            course_txtfld.setText("");
            name_txtfld.requestFocus();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void table() {
        Connect();
        ObservableList<Student> students = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT id,name,phone,course FROM registration");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    Student st = new Student();
                    st.setId(rs.getString("id"));
                    st.setName(rs.getString("name"));
                    st.setPhone(rs.getString("phone"));
                    st.setCourse(rs.getString("course"));
                    students.add(st);
                }
            }
            table.setItems(students);
            ID_column.setCellValueFactory(f -> f.getValue().idProperty());
            name_column.setCellValueFactory(f -> f.getValue().nameProperty());
            phone_column.setCellValueFactory(f -> f.getValue().phoneProperty());
            course_column.setCellValueFactory(f -> f.getValue().courseProperty());


        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        table.setRowFactory(tv -> {
            TableRow<Student> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table.getSelectionModel().getSelectedIndex();
                    id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
                    name_txtfld.setText(table.getItems().get(myIndex).getName());
                    phone_txtfld.setText(table.getItems().get(myIndex).getPhone());
                    course_txtfld.setText(table.getItems().get(myIndex).getCourse());


                }
            });
            return myRow;
        });


    }

    @FXML
    public void Delete(ActionEvent event) {
        myIndex = table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));


        try {
            pst = con.prepareStatement("DELETE FROM registration WHERE id = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registration");

            alert.setHeaderText("Student Registration");
            alert.setContentText("Deleted!");

            alert.showAndWait();
            table();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void Update(ActionEvent event) {

        String st_name, phone, course;

        myIndex = table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        st_name = name_txtfld.getText();
        phone = phone_txtfld.getText().replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        course = course_txtfld.getText();
        course = course.substring(0, 1).toUpperCase() + course.substring(1);

        try {
            pst = con.prepareStatement("UPDATE registration SET name = ?,phone = ? ,course = ? WHERE id = ? ");
            pst.setString(1, st_name);
            pst.setString(2, phone);
            pst.setString(3, course);
            pst.setInt(4, id);
            pst.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registration");

            alert.setHeaderText("Student Registration");
            alert.setContentText("Updated!");

            alert.showAndWait();
            table();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Connect() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);

            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connect();
        table();
    }
}
