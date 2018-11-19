package usth.edu.vn.twitterclient.findpeople;

public class FindFrends {


    public String profileImage;
    public String fullname;
    public String username;
    public  FindFrends () {

    }


    public FindFrends(String profileImage, String fullname, String username) {
        this.profileImage = profileImage;
        this.fullname = fullname;
        this.username = username;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
