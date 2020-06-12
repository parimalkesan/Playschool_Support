package Model;
//class Message to define format of any message
public class Message {
    private String senderId;
    private String receiverId;
    private String message;
    private String isSeen;
    private String timestamp;
    private String isResolved;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Message(String isResolved,String isSeen, String message, String receiverId, String senderId, String timestamp) {
        this.message = message;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.isSeen=isSeen;
        this.timestamp=timestamp;
        this.isResolved=isResolved;
    }




    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public Message()
    {}


    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(String isResolved) {
        this.isResolved = isResolved;
    }
}


