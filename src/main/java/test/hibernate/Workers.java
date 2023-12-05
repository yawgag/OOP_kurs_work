package test.hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Workers", schema = "Hotel", catalog = "")
public class Workers {
    private int workerId;
    private String name;
    private String lastName;
    private Integer workExperience;
    private Integer age;
    private String profession;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "worker_id", nullable = false)
    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
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
    @Column(name = "work_expierence", nullable = true)
    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
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
    @Column(name = "profession", nullable = true, length = 45)
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerId, name, lastName, workExperience, age, profession);
    }

    @Override
    public String toString(){
        return this.workerId + " " + this.lastName + " " + this.name+ " " + this.age + " " + this.profession + " " + this.workExperience;
    }

//"Фамилия", "Имя", "Возраст", "Должность", "стаж работы(в годах)"


}
