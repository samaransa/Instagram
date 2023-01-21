package com.example.instagram.Models;

public class ProfileComplete {
    int image;
    String txtAddBio , about, btnAdd;

    public ProfileComplete(int image, String txtAddBio, String about, String btnAdd) {
        this.image = image;
        this.txtAddBio = txtAddBio;
        this.about = about;
        this.btnAdd = btnAdd;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTxtAddBio() {
        return txtAddBio;
    }

    public void setTxtAddBio(String txtAddBio) {
        this.txtAddBio = txtAddBio;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(String btnAdd) {
        this.btnAdd = btnAdd;
    }
}
