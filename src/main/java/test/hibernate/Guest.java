package test.hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Guest", schema = "Hotel")
public class Guest {
    private int guestId;
    private String name;
    private String lastName;
    private int age;
    private Integer reserv;


    public Guest(){}

    public Guest(String name, String lastName, int age, Integer reserv){
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.reserv = reserv;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Guest_id", nullable = false)
    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
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
    @Column(name = "age", nullable = true, length = 45)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Basic
    @Column(name = "reserv", nullable = true)
    public Integer getReserv() {
        return reserv;
    }

    public void setReserv(Integer reserv) {
        this.reserv = reserv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest that = (Guest) o;
        return guestId == that.guestId && Objects.equals(name, that.name) && Objects.equals(lastName, that.lastName) && Objects.equals(age, that.age) && Objects.equals(reserv, that.reserv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, name, lastName, age, reserv);
    }

    @Override
    public String toString(){
        return this.lastName + " " + this.name+ " " + this.age;
    }
}
