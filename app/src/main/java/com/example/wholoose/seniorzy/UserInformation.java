package com.example.wholoose.seniorzy;

public class UserInformation {

    private String Firstame;
    private String Email;
    private String phone_num;
    private String description;
    private String leader;
    private String userid;


    public UserInformation(){

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getName() {
        return Firstame;
    }

    public void setName(String Firstame) {
        this.Firstame = Firstame;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}