package Model;

//user class to define attributes of a user
public class Users {
    private String Email;
    private String ImageUrl;
    private String Status;
    private String UserId;
    private String UserName;

    public Users(String email, String imageUrl, String status, String userId, String userName) {
        Email = email;
        ImageUrl = imageUrl;
        Status = status;
        UserId = userId;
        UserName = userName;
    }

    public Users(){}

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
