package com.backend.OngConciencia.Model;

public enum UsuarioRole {
    //Authoridades
    ADMIN("admin"),
    USER("user");

    private String role;

    //Construtor
    UsuarioRole(String role){
        this.role = role;
    }

    //Método get
    public String getRole(){
        return role;
    }

}
