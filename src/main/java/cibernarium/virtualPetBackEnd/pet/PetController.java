package cibernarium.virtualPetBackEnd.pet;


import cibernarium.virtualPetBackEnd.user.User;
import cibernarium.virtualPetBackEnd.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/pet")

@RequiredArgsConstructor

public class PetController {

    private final PetService petService;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    //Crear (Create):
    //
    //Permet als usuaris crear noves mascotes virtuals per cuidar i mimar. Poden triar entre una varietat de criatures, des de dracs fins a licorns, i fins i tot extraterrestres. Després, poden personalitzar el color, el nom i les característiques úniques de la seva mascota.


    //    @PostMapping("/addPet")
//    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
//        pet.setHunger(0); // Forzar el hambre a 0 al crear
//        System.out.println("Recibiendo datos de mascota: " + pet);
//        Pet savedPet = petService.addPet(pet);
//        return new ResponseEntity<>(savedPet, HttpStatus.CREATED);
//    }
    @PostMapping("/addPet")
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        // Recuperar el usuario propietario a partir del ownerId proporcionado
        Long ownerId = pet.getOwner().getId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado"));

        // Asociar el usuario como propietario de la mascota
        pet.setOwner(owner);

        // Guardar la mascota
        Pet savedPet = petService.addPet(pet);
        logger.info("Mascota guardada con éxito - ID: {}, Nombre: {}, Tipo: {}, Color: {}, OwnerID: {}",
                savedPet.getId(), savedPet.getName(), savedPet.getType(), savedPet.getColor(), ownerId);
        logger.info("Id del owner: {}", ownerId);

        // Devolver la mascota creada con el código de estado 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    //Mostra totes les mascotes virtuals existents en un entorn virtual vibrants i colorit. Els usuaris poden interactuar amb les seves mascotes, veure el seu estat d'ànim, nivell d'energia i necessitats.
//    @GetMapping("/pets")
//    public ResponseEntity<List<Pet>> getAllPets() {
//        List<Pet> pets = petService.getAllPets(); // Llama al servicio para obtener todas las mascotas
//
//        if (pets.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve un 204 si no hay mascotas
//        }
//
//        return new ResponseEntity<>(pets, HttpStatus.OK); // Devuelve las mascotas con un 200 OK
//    }

    // FLUJO ROTO IDGAF!!! retrieve directo desde la db
    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        if (pets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 si no hay mascotas
        }
        return ResponseEntity.ok(pets);  // 200 OK con la lista de mascotas
    }
    //
    //Actualitzar (Update):
    //
    //Permet als usuaris cuidar i personalitzar les seves mascotes virtuals. Poden alimentar-les, jugar amb elles, comprar accessoris divertits i canviar el seu entorn virtual per mantenir-les felices i saludables.

    @PutMapping("/updatePet/{id}")
    public ResponseEntity<Pet> updatePet(
            @PathVariable("id") Long id, // Recibe el id de la mascota a actualizar
            @RequestBody Pet updatedPet // Recibe el objeto con los nuevos datos
    ) {
        Pet pet = petService.updatePet(id, updatedPet);

        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra la mascota, devuelve un 404
        }

        return new ResponseEntity<>(pet, HttpStatus.OK); // Si la mascota se actualizó correctamente, devuelve un 200 OK
    }

    //
    //Eliminar (Delete):
    //
    //Permet als usuaris retirar mascotes virtuals que ja no desitgin cuidar. En confirmar l'eliminació, la mascota desapareix del seu entorn virtual, però poden crear-ne una nova en qualsevol moment.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id) {
        boolean isRemoved = petService.deletePet(id);

        if (!isRemoved) {
            return new ResponseEntity<>("Mascota no encontrada", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Mascota eliminada con éxito", HttpStatus.OK);
    }

}
