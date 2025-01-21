package cibernarium.virtualPetBackEnd.pet;


import cibernarium.virtualPetBackEnd.accessory.Accessory;
import cibernarium.virtualPetBackEnd.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
public class Pet {

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
