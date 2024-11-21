package catbreedersystem;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String role;

    public User(String id, String name, String email, String phone, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        validateEmail();
    }

    public abstract void viewProfile();

    private void validateEmail() {
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
