package com.boardend.boardend.controller;
import com.boardend.boardend.models.*;
import com.boardend.boardend.repository.FareManagementRepository;
import com.boardend.boardend.repository.MobileUserRepository;
import com.boardend.boardend.repository.UserRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Pageable;
        import com.boardend.boardend.models.Rider;
        import com.boardend.boardend.repository.RiderRepository;
        import com.boardend.boardend.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.*;

import java.util.List;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private MobileUserRepository mobileUserRepository;

    @Autowired
    private FareManagementRepository fareManagementRepository;

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");

        if (newStatus != null) {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                try {
                    Status status = Status.valueOf(newStatus);
                    Status previousStatus = user.getStatus();
                    user.setStatus(status);
                    userRepository.save(user);

                    String email = user.getEmail();
                    String companyName = user.getCompanyName();

                    if (status == Status.APPROVED && previousStatus != Status.APPROVED) {
                        sendCongratulationsEmail(email, companyName);
                    } else if (status == Status.DISABLED && previousStatus != Status.DISABLED) {
                        sendAccountDisabledEmail(email);
                    } else if (status == Status.ENABLED && previousStatus != Status.ENABLED) {
                        sendAccountEnabledEmail(email, companyName);
                    }

                    return ResponseEntity.ok("User status updated successfully");
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid status value");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("Status field is required");
        }
    }

    private void sendCongratulationsEmail(String email, String companyName) {
        String subject = "Congratulations! Your account has been approved";
        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f2f2f2; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #ffffff; text-align: center; padding: 20px; }" +
                ".logo { max-width: 200px; }" +
                ".content { background-color: #ffffff; padding: 20px; }" +
                "h1 { color: #333333; font-size: 24px; margin-bottom: 20px; }" +
                "p { color: #555555; font-size: 16px; margin-bottom: 10px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<img class='logo' src='' alt='Logo'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Congratulations, " + companyName + "!</h1>" +
                "<p>Your account has been successfully approved by our administrators.</p>" +
                "<p>You can now sign in to your account using the following link:</p>" +
                "<p><a href=''>Login to Platform</a></p>" +
                "<p>If you still experience any problems, please contact our support at:" +
                "<p>Thank you for choosing us!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(email, subject, body);
    }

    private void sendAccountEnabledEmail(String email, String companyName) {
        String subject = "Congratulations! Your account has been approved";
        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f2f2f2; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #ffffff; text-align: center; padding: 20px; }" +
                ".logo { max-width: 200px; }" +
                ".content { background-color: #ffffff; padding: 20px; }" +
                "h1 { color: #333333; font-size: 24px; margin-bottom: 20px; }" +
                "p { color: #555555; font-size: 16px; margin-bottom: 10px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<img class='logo' src='' alt='Logo'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Dear, " + companyName + "!</h1>" +
                "<p>After careful considerations, Your account has been successfully reinstated by our administrators.</p>" +
                "<p>You can now sign in to your account using the following link:</p>" +
                "<p><a href=''>Login to Platform</a></p>" +
                "<p>If you still experience any problems, please contact our support at:" +
                "<p>Thank you for choosing Logistics!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(email, subject, body);
    }


    private void sendAccountDisabledEmail(String email) {
        String subject = "Account Temporarily Disabled";
        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f2f2f2; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #ffffff; text-align: center; padding: 20px; }" +
                ".logo { max-width: 200px; }" +
                ".content { background-color: #ffffff; padding: 20px; }" +
                "h1 { color: #333333; font-size: 24px; margin-bottom: 20px; }" +
                "p { color: #555555; font-size: 16px; margin-bottom: 10px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Account Temporarily Disabled</h1>" +
                "<p>Your account has been temporarily disabled. This could happen due to maintenance, a data breach, unauthorized access to our systems, or for a violation of our policies.</p>" +
                "<p>Please contact our support for more information:</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        sendEmail(email, subject, body);
    }

    private void sendEmail(String email, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", "");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("", ""));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(body, "text/html");


            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/mobile/users/{id}")
    public ResponseEntity<String> updateMobileUserStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");

        if (newStatus != null) {
            Optional<MobileUser> optionalUser = mobileUserRepository.findById(id);

            if (optionalUser.isPresent()) {
                MobileUser user = optionalUser.get();
                try {
                    Status status = Status.valueOf(newStatus);
                    Status previousStatus = user.getStatus();
                    user.setStatus(status);
                    mobileUserRepository.save(user);

                    // Sending email
                    String email = user.getEmail();
                    if (status == Status.NOT_APPROVED && previousStatus != Status.NOT_APPROVED) {
                        sendDisabledEmail(email);
                    }

                    return ResponseEntity.ok("User status updated successfully");
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid status value");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("Status field is required");
        }
    }

    private void sendDisabledEmail(String email) {
        String subject = "Account Temporarily Disabled";
        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f2f2f2; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #ffffff; text-align: center; padding: 20px; }" +
                ".logo { max-width: 200px; }" +
                ".content { background-color: #ffffff; padding: 20px; }" +
                "h1 { color: #333333; font-size: 24px; margin-bottom: 20px; }" +
                "p { color: #555555; font-size: 16px; margin-bottom: 10px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Account Temporarily Disabled</h1>" +
                "<p>Your account has been temporarily disabled. This could happen due to maintenance, a data breach, unauthorized access to our systems, or for a violation of our policies.</p>" +
                "<p>Please contact our support for more information:</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        sendEmail(email, subject, body);
    }


    @GetMapping("/users/search")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users")
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @GetMapping("/riders")
    public ResponseEntity<Page<Rider>> getRiderAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Fetch the user object based on the user ID
        Optional<User> userOptional = userRepository.findById(userDetails.getId());

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Pageable pageable = PageRequest.of(page, size);

            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            return ResponseEntity.ok(riderAccounts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/riders/{id}")
    public ResponseEntity<Rider> getRiderAccountById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findById(userDetails.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Rider> riderOptional = riderRepository.findByIdAndUser(id, user);

            if (riderOptional.isPresent()) {
                Rider rider = riderOptional.get();
                return ResponseEntity.ok(rider);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }


    @GetMapping("/users/total")
    public Long getTotalUsers() {
        return userRepository.count();
    }



    @GetMapping("/users/search-engine")
    public Page<User> searchUsers(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllByCacNumberContainingIgnoreCase(query, pageable);
    }

    // Mobile Users
    @GetMapping("/mobile/users/search")
    public List<MobileUser> getMobileUsers() {
        return mobileUserRepository.findAll();
    }

    @GetMapping("/mobile/users")
    public Page<MobileUser> getMobileUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mobileUserRepository.findAll(pageable);
    }

    @GetMapping("/mobile/users/{id}")
    public MobileUser getMobileUser(@PathVariable Long id) {
        return mobileUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/mobile/users/{id}")
    public void deleteMobileUser(@PathVariable Long id) {
        mobileUserRepository.deleteById(id);
    }

    @GetMapping("/mobile/users/total")
    public Long getTotalMobileUsers() {
        return mobileUserRepository.count();
    }

    @GetMapping("/mobile/users/search-engine")
    public Page<MobileUser> searchMobileUsers(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mobileUserRepository.findAllByNameContainingIgnoreCase(query, pageable);
    }

    @GetMapping("/riders/count")
    public ResponseEntity<RiderResponse> getRiderAccountsCount(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Pageable pageable = PageRequest.of(page, size);

            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            long totalRiders = riderRepository.countByUser(user);

            RiderResponse response = new RiderResponse(riderAccounts.getContent(), totalRiders);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/riders/all")
    public Page<Rider> getAllRiders(Pageable pageable) {
        return riderRepository.findAll(pageable);
    }

    @GetMapping("/riders/all/{id}")
    public ResponseEntity<Rider> getRiderById(@PathVariable Long id) {
        Optional<Rider> optionalRider = riderRepository.findById(id);
        if (optionalRider.isPresent()) {
            return ResponseEntity.ok(optionalRider.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/fare")
    public ResponseEntity<FareManagement> getFareManagement() {
        FareManagement fareManagement = fareManagementRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No Fare Management record found"));

        return new ResponseEntity<>(fareManagement, HttpStatus.OK);
    }


    @PostMapping("/users/fare/save")
    public ResponseEntity<FareManagement> createFareManagement(@RequestBody FareManagement fareManagement) {
        FareManagement createdFareManagement = fareManagementRepository.save(fareManagement);
        return new ResponseEntity<>(createdFareManagement, HttpStatus.CREATED);
    }

    @PutMapping("/users/fare/{id}")
    public ResponseEntity<FareManagement> updateFareManagement(@PathVariable Long id, @RequestBody FareManagement fareManagement) {
        FareManagement existingFareManagement = fareManagementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fare Management with ID " + id + " not found"));

        existingFareManagement.setAmount(fareManagement.getAmount());
        existingFareManagement.setRate(fareManagement.getRate());
        existingFareManagement.setServiceFee(fareManagement.getServiceFee());
        existingFareManagement.setCommissionPercent(fareManagement.getCommissionPercent());
        FareManagement updatedFareManagement = fareManagementRepository.save(existingFareManagement);

        return new ResponseEntity<>(updatedFareManagement, HttpStatus.OK);
    }

}

