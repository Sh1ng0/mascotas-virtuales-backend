package cibernarium.virtualPetBackEnd.accessory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccessoryController {

    @Autowired
    private AccessoryService accessoryService;

    @PostMapping("/addAccessory/{petId}")
    public ResponseEntity<Accessory> addAccessory(@PathVariable Long petId, @RequestBody Accessory accessory) {
        Accessory newAccessory = accessoryService.addAccessory(petId, accessory);
        return new ResponseEntity<>(newAccessory, HttpStatus.CREATED);
    }
}
