package kz.attractor.java.lesson44;

public class SingleUserDataModel {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SingleUserDataModel(User user){
        this.user = user;
    }
}