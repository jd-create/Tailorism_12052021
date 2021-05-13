package nl.novi.jdemeijervandriel.tailorism.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String generatePublicContent() {
        return "Public Content.";
    }

//    @PreAuthorize("hasRole('USER') ")
//    public String generateUserContent() {
//        return "User Content.";
//    }
    @PreAuthorize("hasRole('USER')")
    public String generateUserContent() {
    return "User Content.";
}

    @PreAuthorize("hasRole('ADMIN')")
    public String generateAdminContent() {
        return "Admin Board.";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    public String generateCustomerContent() {
        return "Customer Board.";
    }

    @PreAuthorize("hasRole('TAILOR')")
    public String generateTailorContent() {
        return "Tailor Board.";
    }

    @PreAuthorize("hasRole('OPERATOR')")
    public String generateOperatorContent() {
        return "Operator Board.";
    }
}
