package test.hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Room", schema = "Hotel", catalog = "")
public class Room {
    private int roomId;
    private Integer roomNumber;
    private Integer copacity;
    private Integer price;
    private Integer reservedNow;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "room_id", nullable = false)
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Basic
    @Column(name = "Room_number", nullable = true)
    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Basic
    @Column(name = "copacity", nullable = true)
    public Integer getCopacity() {
        return copacity;
    }

    public void setCopacity(Integer copacity) {
        this.copacity = copacity;
    }

    @Basic
    @Column(name = "price", nullable = true)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "reserved_now", nullable = true)
    public Integer getReservedNow() {
        return reservedNow;
    }

    public void setReservedNow(Integer reservedNow) {
        this.reservedNow = reservedNow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, roomNumber, copacity, price);
    }

    @Override
    public String toString(){
        return this.roomId + " " + this.roomNumber + " " + this.copacity+ " " + this.price + " " + this.reservedNow;
    }
}
