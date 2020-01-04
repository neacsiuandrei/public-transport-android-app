package ro.ase.neacsiuandrei.database;

import android.database.Cursor;

import ro.ase.neacsiuandrei.models.User;

public class UserService implements UserDao {
    private final UserDao tableInstance;

    public UserService(UserDao tableInstance) {
        this.tableInstance = tableInstance;
    }

    @Override
    public Long addUser(User user) {
        return tableInstance.addUser(user);
    }

    @Override
    public Cursor getUsers() {
        return tableInstance.getUsers();
    }

    @Override
    public void deleteUser(User user) {
        tableInstance.deleteUser(user);
    }

    @Override
    public User getUserByID(int id) {
        return tableInstance.getUserByID(id);
    }

    @Override
    public void deleteUserFromUsername(String username) {
        tableInstance.deleteUserFromUsername(username);
    }
}
