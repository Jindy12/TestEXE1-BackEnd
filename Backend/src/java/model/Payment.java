/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class Payment {

    private String id, vnp_TxnRef, vnp_Amount, vnp_OrderInfo, vnp_ResponseCode, vnp_TransactionNo, vnp_BankCode, vnp_PayDate, vnp_TransactionStatus;
    private int aid;

    public Payment() {
    }

    public Payment(String id, String vnp_TxnRef, String vnp_Amount, String vnp_OrderInfo, String vnp_ResponseCode, String vnp_TransactionNo, String vnp_BankCode, String vnp_PayDate, String vnp_TransactionStatus, int aid) {
        this.id = id;
        this.vnp_TxnRef = vnp_TxnRef;
        this.vnp_Amount = vnp_Amount;
        this.vnp_OrderInfo = vnp_OrderInfo;
        this.vnp_ResponseCode = vnp_ResponseCode;
        this.vnp_TransactionNo = vnp_TransactionNo;
        this.vnp_BankCode = vnp_BankCode;
        this.vnp_PayDate = vnp_PayDate;
        this.vnp_TransactionStatus = vnp_TransactionStatus;
        this.aid = aid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public String getVnp_Amount() {
        return vnp_Amount;
    }

    public void setVnp_Amount(String vnp_Amount) {
        this.vnp_Amount = vnp_Amount;
    }

    public String getVnp_OrderInfo() {
        return vnp_OrderInfo;
    }

    public void setVnp_OrderInfo(String vnp_OrderInfo) {
        this.vnp_OrderInfo = vnp_OrderInfo;
    }

    public String getVnp_ResponseCode() {
        return vnp_ResponseCode;
    }

    public void setVnp_ResponseCode(String vnp_ResponseCode) {
        this.vnp_ResponseCode = vnp_ResponseCode;
    }

    public String getVnp_TransactionNo() {
        return vnp_TransactionNo;
    }

    public void setVnp_TransactionNo(String vnp_TransactionNo) {
        this.vnp_TransactionNo = vnp_TransactionNo;
    }

    public String getVnp_BankCode() {
        return vnp_BankCode;
    }

    public void setVnp_BankCode(String vnp_BankCode) {
        this.vnp_BankCode = vnp_BankCode;
    }

    public String getVnp_PayDate() {
        return vnp_PayDate;
    }

    public void setVnp_PayDate(String vnp_PayDate) {
        this.vnp_PayDate = vnp_PayDate;
    }

    public String getVnp_TransactionStatus() {
        return vnp_TransactionStatus;
    }

    public void setVnp_TransactionStatus(String vnp_TransactionStatus) {
        this.vnp_TransactionStatus = vnp_TransactionStatus;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

}
