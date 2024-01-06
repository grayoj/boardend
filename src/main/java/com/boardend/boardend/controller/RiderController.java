package com.boardend.boardend.controller;

import com.boardend.boardend.models.Rider;
import com.boardend.boardend.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "")
@RestController
@RequestMapping("/api/rider")
public class RiderController {

    @Autowired
    RiderRepository riderRepository;

    @GetMapping("/rider")
    public ResponseEntity<List<Rider>> getAllRider(@RequestParam(required = false) String name) {
        try {
            List<Rider> riders = new ArrayList<Rider>();

            if (name == null)
                riderRepository.findAll().forEach(riders::add);
            else
                riderRepository.findByName(name).forEach(riders::add);

            if (riders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(riders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rider/{id}")
    public ResponseEntity<Rider> getRiderById(@PathVariable("id") long id) {
        Optional<Rider> RiderData = riderRepository.findById(id);

        if (RiderData.isPresent()) {
            return new ResponseEntity<>(RiderData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/rider")
//    public ResponseEntity<Rider> createRider(@RequestBody Rider rider) {
//        try {
//            Rider _rider = riderRepository
//                    .save(new Rider(rider.getFirstName(), rider.getLastName(),
//                            rider.getPhoneNumber(), rider.getStreetAddress(), rider.getEmail(),
//                            false));
//            return new ResponseEntity<>(_rider, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PutMapping("/rider/{id}")
//    public ResponseEntity<Rider> updateRider(@PathVariable("id") long id, @RequestBody Rider rider) {
//        Optional<Rider> riderData = riderRepository.findById(id);
//
//        if (riderData.isPresent()) {
//            Rider _rider = riderData.get();
//            _rider.setFirstName(rider.getFirstName());
//            _rider.setLastName(rider.getLastName());
//            _rider.setPhoneNumber(rider.getPhoneNumber());
//            _rider.setStreetAddress(rider.getStreetAddress());
//            _rider.setEmail(rider.getEmail());
//            _rider.setAvailable(rider.isAvailable());
//            return new ResponseEntity<>(riderRepository.save(_rider), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/rider/{id}")
    public ResponseEntity<HttpStatus> deleteRider(@PathVariable("id") long id) {
        try {
            riderRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/rider")
    public ResponseEntity<HttpStatus> deleteAllRider() {
        try {
            riderRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/rider/available")
    public ResponseEntity<List<Rider>> findByAvailable() {
        try {
            List<Rider> rider = riderRepository.findByAvailable(true);

            if (rider.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rider, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
