package cibernarium.virtualPetBackEnd.pet;


import cibernarium.virtualPetBackEnd.user.User;
import cibernarium.virtualPetBackEnd.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class PetService {


    private final PetRepository petRepository;
    private final UserRepository userRepository;

//    @Autowired
//    public PetService(PetRepository petRepository) {
//        this.petRepository = petRepository;
//        this.userRepository = userRepository;
//    }

//    public Pet addPet(Pet pet) {
//
//        return petRepository.save(pet);
//    }

    // CON ID MANUAL

    public Pet addPet(Pet pet) {
        // Cargar el usuario completo desde el ID proporcionado
        User owner = userRepository.findById(pet.getOwner().getId())
                .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado"));

        // Asociar el propietario completo a la mascota
        pet.setOwner(owner);

        // Guardar la mascota
        return petRepository.save(pet);
    }

    public List<Pet> getUserPets(Long ownerId) {
        return petRepository.findByOwnerId(ownerId); // Filtra las mascotas por ownerId
    }

    public Pet updatePet(Long id, Pet updatedPet) {
        // Verifica si la mascota existe
        Pet pet = petRepository.findById(id).orElse(null);

        if (pet == null) {
            return null; // Si no se encuentra la mascota, retorna null
        }

        // Actualiza las propiedades de la mascota con los nuevos valores
        pet.setName(updatedPet.getName());
        // pet.setHunger(updatedPet.getHunger());
        pet.setColor(updatedPet.getColor());

        // Guarda la mascota actualizada en la base de datos
        return petRepository.save(pet);
    }

    public boolean deletePet(Long id) {
        // Verificar si la mascota existe
        if (!petRepository.existsById(id)) {
            return false;  // Si la mascota no existe, no la podemos eliminar
        }

        petRepository.deleteById(id);  // Eliminar la mascota
        return true;  // La mascota se eliminó correctamente
    }

    // fetch(`/pets/delete/${petId}`, {
    //  method: 'DELETE',
    //})
    //  .then(response => response.json())
    //  .then(data => {
    //    console.log(data);  // "Mascota eliminada con éxito"
    //  })
    //  .catch(error => console.error('Error:', error));


    // HUNGER MECHANIC UNDER CONSTRUCTION //

    // @Deprecated

//    @Scheduled(fixedRate = 60000)
//    public void increaseHunger() {
//        List<Pet> pets = petRepository.findAll();
//
//        for (Pet pet : pets) {
//            // Aumenta el hambre de la mascota, asegurándote de que no supere el 10
//            if (pet.getHunger() < 10) {
//                pet.setHunger(pet.getHunger() + 1);
//                petRepository.save(pet); // Guarda el cambio en la base de datos
//            }
//        }
//    }

    // HUNGER en el FRONT

    // import { useState, useEffect } from "react";
    //
    //function PetList() {
    //    const [pets, setPets] = useState([]);
    //
    //    useEffect(() => {
    //        const fetchPets = async () => {
    //            const response = await fetch("http://localhost:8080/pets");
    //            const data = await response.json();
    //            setPets(data);
    //        };
    //
    //        // Traer las mascotas cada 1 minuto
    //        const intervalId = setInterval(fetchPets, 60000);
    //
    //        // Inicializar la carga de mascotas
    //        fetchPets();
    //
    //        return () => clearInterval(intervalId); // Limpiar el intervalo cuando el componente se desmonte
    //    }, []);
    //
    //    const getMood = (hunger) => {
    //        if (hunger >= 8) {
    //            return "sad"; // O cualquier clase o estado que represente tristeza
    //        }
    //        return "happy"; // O cualquier clase o estado que represente felicidad
    //    };
    //
    //    return (
    //        <div>
    //            <h1>Lista de Mascotas</h1>
    //            <ul>
    //                {pets.map((pet) => (
    //                    <li key={pet.id} className={`pet ${getMood(pet.hunger)}`}>
    //                        <img
    //                            src={getMood(pet.hunger) === "sad" ? "/path/to/sad-face.png" : "/path/to/happy-face.png"}
    //                            alt="Pet Face"
    //                        />
    //                        <div>{pet.name} - Hunger: {pet.hunger}</div>
    //                    </li>
    //                ))}
    //            </ul>
    //        </div>
    //    );
    //}
    //
    //export default PetList;



}

