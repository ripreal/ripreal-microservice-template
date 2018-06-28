package ru.sportmaster.esm.user.dto;

import ru.sportmaster.esm.loyalty.Customer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UpdateUserInfoRequest {
    @NotEmpty(message = "error.firstName_empty")
    @Size(min = 2, max = 64)
    private String firstName;

    @NotEmpty(message = "error.secondName_empty")
    @Size(min = 2, max = 64)
    private String secondName;

    @NotEmpty(message = "error.lastName_empty")
    @Size(min = 2, max = 64)
    private String lastName;

    @NotNull(message = "error.birthday_empty")
    private LocalDate birthday;

    @NotNull(message = "error.gender_empty")
    private Customer.Gender gender;

    @NotEmpty(message = "error.city_name_empty")
    private String cityName;

    private List<Customer.Child> children;

    public UpdateUserInfoRequest() {
        children = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Customer.Gender getGender() {
        return gender;
    }

    public void setGender(Customer.Gender gender) {
        this.gender = gender;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Customer.Child> getChildren() {
        return children;
    }

    public void setChildren(List<Customer.Child> children) {
        this.children = children;
    }
}
