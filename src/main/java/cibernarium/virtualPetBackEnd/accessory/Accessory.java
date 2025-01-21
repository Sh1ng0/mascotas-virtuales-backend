package cibernarium.virtualPetBackEnd.accessory;


import cibernarium.virtualPetBackEnd.pet.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;  // Puede ser un collar, sombrero, etc.

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;  // Relación con la mascota

    // Getters y setters
}