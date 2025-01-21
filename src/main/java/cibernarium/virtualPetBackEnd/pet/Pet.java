package cibernarium.virtualPetBackEnd.pet;


import cibernarium.virtualPetBackEnd.accessory.Accessory;
import cibernarium.virtualPetBackEnd.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@ToString

@Entity
public class Pet {

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", owner=" + owner +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    //    private int hunger;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Color color;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;



    //    @OneToMany(mappedBy = "pet")
//    private List<Accessory> accessories;

}
