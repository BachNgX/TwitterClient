package usth.edu.vn.twitterclient;

public class ListItem {
    private String head;
    private String desc;
    private int num;

    public ListItem(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }
}
