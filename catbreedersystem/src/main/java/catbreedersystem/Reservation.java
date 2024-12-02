package catbreedersystem;

public class Reservation {
    protected String reserveID;
    protected String catID;
    protected String userID;
    protected String reserveDate;

    public Reservation(String reserveID, String catID, String userID, String reserveDate) {
        this.reserveID = reserveID;
        this.catID = catID;
        this.userID = userID;
        this.reserveDate = reserveDate;
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reserveID='" + reserveID + '\'' +
                ", catID='" + catID + '\'' +
                ", userID='" + userID + '\'' +
                ", reserveDate='" + reserveDate + '\'' +
                '}';
    }

    public String getReserveID() {
        return this.reserveID;
    }

    public String getCatID() {
        return this.catID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getReserveDate() {
        return this.reserveDate;
    }
}