package com.example.studentproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty  course;

    public Student() {
        id = new SimpleStringProperty(this, "id");
        name = new SimpleStringProperty(this, "name");
        phone = new SimpleStringProperty(this, "phone");
        course = new SimpleStringProperty(this, "course");
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.getValue();
    }
    public void setId(String newId) {
        id.setValue(newId);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String newName) {
        name.setValue(newName);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getPhone() {
        return phone.getValue();
    }

    public void setPhone(String newPhone) {
        phone.setValue(newPhone);
    }

    public StringProperty courseProperty() {
        return course;
    }

    public String getCourse() {
        return course.getValue();
    }

    public void setCourse(String newCourse) {
        course.setValue(newCourse);
    }
}
