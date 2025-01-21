package cibernarium.virtualPetBackEnd.accessory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cibernarium.virtualPetBackEnd.pet.Pet;
import cibernarium.virtualPetBackEnd.pet.PetRepository;

@Service
public class AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Autowired
    private PetRepository petRepository;

    public Accessory addAccessory(Long petId, Accessory accessory) {
        Pet pet = petRepository.findById(petId).orElse(null);

        if (pet == null) {
            return null;  // La mascota no existe
        }

        accessory.setPet(pet);  // Asociamos el accesorio con la mascota
        return accessoryRepository.save(accessory);  // Guardamos el accesorio en la base de datos
    }
}