package edu.uws.ii.springboot.commands;

public abstract class AuthorizedCommandBase {
    private String userCode;

    public String getUserCode() { return userCode; }
    public void setUserCode(String userCode) { this.userCode = userCode; }
}
