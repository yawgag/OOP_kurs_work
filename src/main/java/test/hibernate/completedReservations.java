package test.hibernate;

import javax.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "completedReservations", schema = "Hotel", catalog = "")
public class completedReservations {
    private int id;
    private String name;
    private String lastName;
    private Integer age;
    private Date checkIn;
    private Date chechOut;
    private Integer room;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 45)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
    @Column(name = "chech_out", nullable = true)
    public Date getChechOut() {
        return chechOut;
    }

    public void setChechOut(Date chechOut) {
        this.chechOut = chechOut;
    }

    @Basic
    @Column(name = "room", nullable = true)
    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        completedReservations that = (completedReservations) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lastName, that.lastName) && Objects.equals(age, that.age) && Objects.equals(checkIn, that.checkIn) && Objects.equals(chechOut, that.chechOut) && Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, age, checkIn, chechOut, room);
    }
}
