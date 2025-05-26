/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author tuana
 */
public class Account {

    private int userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private int gender;
    private String email;
    private String mobile;
    private String address;
    private int roleId;
    private String avatar;
    private int status;
    private BigDecimal money;

    public Account() {
    }

    public Account(String userName, String password, String firstName, String lastName,
            int gender, String email, String mobile, String address, int status) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.status = status;
        // Đặt giá trị cho các trường khác nếu cần, chẳng hạn như userId, roleId, avatar...
    }

    public Account(int userId, String userName, String password, String firstName, String lastName, int gender, String email, String mobile, String address, int roleId, String avatar, int status) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.roleId = roleId;
        this.avatar = avatar;
        this.status = status;
    }

    public Account(int userId, String userName, String password, String firstName, String lastName, int gender, String email, String mobile, String address, int roleId, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.roleId = roleId;
        this.avatar = avatar;
    }

    public Account(String userName, String password, String firstName, String lastName, int gender, String email, String mobile, String address) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public Account(String userName, String password, String firstName, String lastName, int gender, String email, String mobile, String address, int roleId, String avatar) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.roleId = roleId;
        this.avatar = avatar;
    }

    public Account(int userId, String userName, String password, String firstName, String lastName, int gender, String email, String mobile, String address, int roleId, String avatar, int status, BigDecimal money) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.roleId = roleId;
        this.avatar = avatar;
        this.status = status;
        this.money = money;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    

}
