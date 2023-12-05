package test.hibernate;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Reservation", schema = "Hotel", catalog = "")
public class Reservation {
    private int reservationId;
    private Integer room;
    private Date checkIn;
    private Date checkOut;


    public Reservation(int room, Date checkIn, Date checkOut){
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Reservation(){ }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "reservation_id", nullable = false)
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    @Basic
    @Column(name = "room", nullable = true)
    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    @Basic
    @Column(name = "check_in", nullable = true)
    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    @Basic
    @Column(name = "check_out", nullable = true)
    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return reservationId == that.reservationId && Objects.equals(room, that.room) && Objects.equals(checkIn, that.checkIn) && Objects.equals(checkOut, that.checkOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, room, checkIn, checkOut);
    }


    @Override
    public String toString() {
        return this.room + " " + this.checkIn + " " + this.checkOut;
    }
}
