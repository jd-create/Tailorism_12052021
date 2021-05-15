package nl.novi.jdemeijervandriel.tailorism.controller;

import nl.novi.jdemeijervandriel.tailorism.domain.Address;
import nl.novi.jdemeijervandriel.tailorism.domain.Customer;
import nl.novi.jdemeijervandriel.tailorism.domain.File;
import nl.novi.jdemeijervandriel.tailorism.payload.RegisterUserRequest;
import nl.novi.jdemeijervandriel.tailorism.payload.response.ResponseMessage;
import nl.novi.jdemeijervandriel.tailorism.repository.AddressRepository;
import nl.novi.jdemeijervandriel.tailorism.service.CustomerService;
import nl.novi.jdemeijervandriel.tailorism.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable("id") long id){
        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping(value = "/lastname/{lastname}")
    public ResponseEntity<Object> getCustomerByLastName(@PathVariable("lastname") String lastname){
        Customer customer = customerService.getCustomerByLastName(lastname);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> saveCustomer(@RequestBody RegisterUserRequest registerUserRequest){
        long newId = customerService.saveCustomer(registerUserRequest);
        return new ResponseEntity<>(newId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") long id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Client with ID " + id + "deleted.", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer)
    {
        customerService.updateCustomer(id, customer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/upload/customerid/{id}")
    public ResponseEntity<ResponseMessage> uploadFile(@PathVariable long id, @RequestParam("file") MultipartFile file) {

        try{
            fileStorageService.store(file, id);

            String message = "Upload file succesfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception e){
            String message = "Couldn't upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/download/customerid/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable String id){
        File file1 = fileStorageService.getFileById(id);

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                 "attachment; filename=\"" + file1.getName() + "\"")
                .body(file1.getData());
    }

    @GetMapping("/address/customer_lastname/{lastname}")
    public Optional<Address> findAddressByCustomer_lastname(@PathVariable ("lastname")String lastname){
        Optional<Address> address1 = addressRepository.findAddressByCustomer_LastName(lastname);
        return address1;
    }
}
