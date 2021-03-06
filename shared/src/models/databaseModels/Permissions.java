package models.databaseModels;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by acaccia on 11/05/16.
 */
@XmlRootElement
public class Permissions {
    private String username;
    private String projectUID;
    private int userID;
    private boolean writable, readable;

    public Permissions() {
    }

    public Permissions(String projectUID, int userID, boolean write, boolean read, String username) {
        this.projectUID = projectUID;
        this.userID = userID;
        this.writable = write;
        this.readable = read;
        this.username = username;
    }

    @XmlAttribute
    public String getProjectUID() {
        return projectUID;
    }

    public void setProjectUID(String projectUID) {
        this.projectUID = projectUID;
    }

    @XmlAttribute
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @XmlAttribute
    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    @XmlAttribute
    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
