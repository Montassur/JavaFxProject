package org.example.controllers;

import org.example.entities.User;

public abstract class HomeControllerBase {
    protected User currentUser;
    public void setCurrentUser(User u) {
        this.currentUser = u;
        onUserSet();
    }
    protected abstract void onUserSet();
}