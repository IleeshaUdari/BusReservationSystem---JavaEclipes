package bussystem;

public class User {
    private String name;
    private String userId;
    private String password;
    private String email;
    private String city;
    private String phone;
    private int age;

    public User(String name, String userId, String password, String email, String city, String phone, int age) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.city = city;
        this.phone = phone;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }
}
