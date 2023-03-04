package kz.attractor.java.lesson44;

import java.util.ArrayList;
import java.util.List;

public class UserDataModel {
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UserDataModel(){
        this.users = FileService.readUserFile();
    }
}
