package usth.edu.vn.twitterclient;

public class Posts {
    public String uid;
    public String time;
    public String date;
    public String tweetImage;
    public String description;
    public String profileImage;
    public String fullname;

    public Posts() {

    }

    public Posts(String uid, String time, String date, String tweetImage, String description, String profileImage, String fullname) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.tweetImage = tweetImage;
        this.description = description;
        this.profileImage = profileImage;
        this.fullname = fullname;
    }




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTweetImage() {
        return tweetImage;
    }
    public void setTweetImage(String tweetImage)
     {
        this.tweetImage = tweetImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
