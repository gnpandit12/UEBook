package com.r.uebook.data.remote.model;

public class contact {
    private String contact_name;
    private String bio;
    private int contactPicId;

    public contact(String contact_name, String bio, int contactPicId) {
        this.contact_name = contact_name;
        this.bio = bio;
        this.contactPicId = contactPicId;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setContactPicId(int contactPicId) {
        this.contactPicId = contactPicId;
    }

    public int getContactPicId() {
        return contactPicId;
    }

}
