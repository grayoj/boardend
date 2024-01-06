package com.boardend.boardend.controller;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.net.URI;
import com.boardend.boardend.exception.ResourceNotFoundException;
import com.boardend.boardend.models.*;
import com.boardend.boardend.repository.*;
import com.boardend.boardend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    RiderRepository riderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MobileUserRepository mobileUserRepository;

    @Autowired
    PaymentResponseRepository paymentResponseRepository;
    @Autowired
    MobileDeliveryRepository mobileDeliveryRepository;

    @Autowired
    UserMobileDeliveryRepository userMobileDeliveryRepository;

    private static final String paystackApiKey = "";
    private static final String callbackUrl = "";



    @GetMapping("/admin/delivery")
    public ResponseEntity<Page<MobileDelivery>> getAllDeliveryAdmin(
            @RequestParam(required = false) String packageName,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Long userId) {

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<MobileDelivery> page;

            Optional<MobileUser> mobileUserOptional = mobileUserRepository.findById(userId);
            if (mobileUserOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            MobileUser mobileUser = mobileUserOptional.get();

            if (packageName == null) {
                page = mobileDeliveryRepository.findByRiderUser(mobileUser, paging);
            } else {
                page = mobileDeliveryRepository.findByRiderUserAndPackageName(mobileUser, packageName, paging);
            }

            if (page.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(page, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/dashboard/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable("id") long id) {
        Optional<Delivery> deliveryData = deliveryRepository.findById(id);

        if (deliveryData.isPresent()) {
            return new ResponseEntity<>(deliveryData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable("id") long id, @RequestBody Delivery delivery) {
        Optional<Delivery> deliveryData = deliveryRepository.findById(id);

        if (deliveryData.isPresent()) {
            Delivery _delivery = deliveryData.get();
            _delivery.setPackageName(delivery.getPackageName());
            _delivery.setPackageType(delivery.getPackageType());
            _delivery.setDropAddress(delivery.getDropAddress());
            _delivery.setPickupAddress(delivery.getPickupAddress());
            return new ResponseEntity<>(deliveryRepository.save(_delivery), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delivery/{id}")
    public ResponseEntity<HttpStatus> deleteDelivery(@PathVariable("id") long id) {
        try {
            deliveryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delivery")
    public ResponseEntity<HttpStatus> deleteAllDelivery() {
        try {
            deliveryRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/delivery/delivered")
    public ResponseEntity<List<Delivery>> findByDelivered() {
        try {
            List<Delivery> delivery = deliveryRepository.findByDelivered(true);

            if (delivery.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(delivery, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/delivery/create")
//    public ResponseEntity<MobileDelivery> createDelivery(@RequestParam Long userId, @RequestBody MobileDelivery delivery) {
//        try {
//            Optional<MobileUser> userData = mobileUserRepository.findById(userId);
//            if (userData.isPresent()) {
//                MobileUser user = userData.get();
//                delivery.setUser(user);
//                delivery.setDeliveryTime(LocalDateTime.now()); // Set delivery time with current timestamp
//                MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
//                return new ResponseEntity<>(savedDelivery, HttpStatus.CREATED);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


//    @PostMapping("/delivery/create")
//    public ResponseEntity<MobileDelivery> createDelivery(@RequestParam Long userId, @RequestBody MobileDelivery delivery) {
//        try {
//            Optional<MobileUser> userData = mobileUserRepository.findById(userId);
//            if (userData.isPresent()) {
//                MobileUser user = userData.get();
//                delivery.setUser(user);
//                delivery.setDeliveryTime(LocalDateTime.now()); // Set delivery time with current timestamp
//
//                // Initiate payment through Paystack API
//                ResponseEntity<InitializeTransactionResponse> paymentResponse = initiatePayment(delivery);
//
//                if (paymentResponse.getStatusCode() == HttpStatus.OK) {
//                    InitializeTransactionResponse initializeTransactionResponse = paymentResponse.getBody();
//                    PaymentResponse paymentResponseEntity = new PaymentResponse();
//                    paymentResponseEntity.setStatus(true);
//                    paymentResponseEntity.setMessage("Authorization URL created");
//                    paymentResponseEntity.setAuthorizationUrl(initializeTransactionResponse.getAuthorizationUrl());
//                    paymentResponseEntity.setAccessCode(initializeTransactionResponse.getAccessCode());
//                    paymentResponseEntity.setReference(initializeTransactionResponse.getReference());
//                    paymentResponseEntity.setAmount(initializeTransactionResponse.getAmount());
//                    paymentResponseEntity.setCurrency(initializeTransactionResponse.getCurrency());
//
//                    // Store the payment response in the delivery
//                    delivery.setPaymentResponse(paymentResponseEntity);
//
//                    // Store the delivery based on payment status
//                    if (initializeTransactionResponse.getStatus()) {
//                        // Payment was successful, store the delivery
//                        delivery.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
//                        MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
//                        return new ResponseEntity<>(savedDelivery, HttpStatus.CREATED);
//                    } else {
//                        // Payment failed, store the delivery with payment status as PAYMENT_FAILED
//                        delivery.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
//                        MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
//                        return new ResponseEntity<>(savedDelivery, HttpStatus.OK);
//                    }
//                } else {
//                    // Failed to initiate payment, return an error response
//                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private ResponseEntity<InitializeTransactionResponse> initiatePayment(MobileDelivery delivery) {
//        // Create the request body
//        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//        requestBody.add("amount", String.valueOf(delivery.getAmount()));
//        requestBody.add("email", delivery.getUser().getEmail());
//        requestBody.add("callback_url", callbackUrl);
//
//        // Set the request headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setBearerAuth(paystackApiKey);
//
//        // Create the request entity with the URL, method, headers, and body
//        RequestEntity<MultiValueMap<String, String>> requestEntity = null;
//        try {
//            requestEntity = new RequestEntity<>(requestBody, headers, HttpMethod.POST, new URI("https://api.paystack.co/transaction/initialize"));
//            // Rest of your code
//        } catch (URISyntaxException e) {
//            // Handle the URISyntaxException
//            e.printStackTrace(); // Or use logging or custom error handling
//        }
//
//
//        // Send the request and get the response
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<InitializeTransactionResponse> responseEntity = restTemplate.exchange(requestEntity, InitializeTransactionResponse.class);
//
//        return responseEntity;
//    }
@PostMapping("/delivery/create")
public ResponseEntity<PaymentResponse> createDelivery(@RequestParam Long userId, @RequestBody MobileDelivery delivery) {
    try {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);
        if (userData.isPresent()) {
            MobileUser user = userData.get();
            delivery.setUser(user);
            delivery.setDeliveryTime(LocalDateTime.now());

            PaymentType paymentType = delivery.getPaymentType();

            if (PaymentType.CASH.equals(paymentType)) {
                // Payment type is cash, create a cash payment response
                PaymentResponse paymentResponseEntity = new PaymentResponse();
                paymentResponseEntity.setStatus(true);
                paymentResponseEntity.setPaymentType(PaymentType.CASH);
                paymentResponseEntity.setAmount(delivery.getAmount());
                paymentResponseEntity.setCreatedAt(LocalDate.now());

                paymentResponseEntity.setDelivery(delivery);
                delivery.setPaymentResponse(paymentResponseEntity);

                delivery.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
                MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
                return new ResponseEntity<>(paymentResponseEntity, HttpStatus.CREATED);
            } else if (PaymentType.PAYSTACK.equals(paymentType)) {
                // Payment type is Paystack, initiate payment
                ResponseEntity<PaymentResponse> paymentResponse = initiatePayment(delivery);

                if (paymentResponse.getStatusCode() == HttpStatus.OK) {
                    PaymentResponse initializeTransactionResponse = paymentResponse.getBody();
                    PaymentResponse paymentResponseEntity = new PaymentResponse();
                    paymentResponseEntity.setStatus(true);
                    paymentResponseEntity.setMessage("Authorization URL created");
                    paymentResponseEntity.setAuthorizationUrl(initializeTransactionResponse.getAuthorizationUrl());
                    paymentResponseEntity.setAccessCode(initializeTransactionResponse.getAccessCode());
                    paymentResponseEntity.setReference(initializeTransactionResponse.getReference());
                    paymentResponseEntity.setAmount(initializeTransactionResponse.getAmount());
                    paymentResponseEntity.setCurrency(initializeTransactionResponse.getCurrency());
                    paymentResponseEntity.setPaymentType(PaymentType.PAYSTACK);

                    paymentResponseEntity.setDelivery(delivery);
                    delivery.setPaymentResponse(paymentResponseEntity);

                    if (initializeTransactionResponse.getStatus()) {
                        delivery.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
                        MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
                        return new ResponseEntity<>(paymentResponseEntity, HttpStatus.CREATED);
                    } else {
                        // Payment failed, store the delivery with payment status as PAYMENT_FAILED
                        delivery.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
                        MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
                        return new ResponseEntity<>(paymentResponseEntity, HttpStatus.OK);
                    }
                } else {
                    // Failed to initiate payment, return an error response
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                // Invalid payment type, return an error response
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    private ResponseEntity<PaymentResponse> initiatePayment(MobileDelivery delivery) {
        try {
            InitializeTransactionRequest request = new InitializeTransactionRequest();
            int amountInKobo = (int) (delivery.getAmount() * 100);
            request.setAmount(amountInKobo);
            request.setEmail(delivery.getUser().getEmail());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(paystackApiKey);

            HttpEntity<InitializeTransactionRequest> requestEntity = new HttpEntity<>(request, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    "https://api.paystack.co/transaction/initialize",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseMap = responseEntity.getBody();
                boolean status = (boolean) responseMap.get("status");
                String message = (String) responseMap.get("message");

                Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
                String authorizationUrl = (String) dataMap.get("authorization_url");
                String accessCode = (String) dataMap.get("access_code");
                String reference = (String) dataMap.get("reference");

                PaymentResponse initializeTransactionResponse = new PaymentResponse();
                initializeTransactionResponse.setStatus(status);
                initializeTransactionResponse.setMessage(message);
                initializeTransactionResponse.setAuthorizationUrl(authorizationUrl);
                initializeTransactionResponse.setAccessCode(accessCode);
                initializeTransactionResponse.setReference(reference);
                initializeTransactionResponse.setAmount(delivery.getAmount());

                return new ResponseEntity<>(initializeTransactionResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean verifyPayment(String reference) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(paystackApiKey);

            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    "https://api.paystack.co/transaction/verify/" + reference,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseMap = responseEntity.getBody();
                boolean status = (boolean) responseMap.get("status");
                return status;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updatePaymentResponse(MobileDelivery delivery, PaymentResponse initializeTransactionResponse, boolean paymentStatus) {
        initializeTransactionResponse.setStatus(paymentStatus);
        delivery.setPaymentResponse(initializeTransactionResponse);

        if (paymentStatus) {
            delivery.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
        } else {
            delivery.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
        }

        MobileDelivery savedDelivery = mobileDeliveryRepository.save(delivery);
    }

    @GetMapping("/finance/all")
    public ResponseEntity<Page<PaymentResponse>> getAllPaymentResponses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResponse> paymentResponses = paymentResponseRepository.findAll(pageable);
        return new ResponseEntity<>(paymentResponses, HttpStatus.OK);
    }


    @GetMapping("/finance/search")
    public ResponseEntity<Map<String, Object>> getAllPaymentResponses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String companyName
    ) {
        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Create a specification for filtering by createdAt date range and companyName
        Specification<PaymentResponse> specification = Specification.where(null);

        // Add filter for createdAt date range
        if (fromDate != null && toDate != null) {
            LocalDate from = LocalDate.parse(fromDate);
            LocalDate to = LocalDate.parse(toDate);

            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), from, to));
        }

        // Add filter for companyName
        if (companyName != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("delivery").get("rider").get("user").get("companyName"), companyName));
        }

        // Retrieve the payment responses based on the filters and pageable
        Page<PaymentResponse> paymentResponses = paymentResponseRepository.findAll(specification, pageable);

        // Calculate the total amount
        double totalAmount = paymentResponses.stream()
                .mapToDouble(PaymentResponse::getAmount)
                .sum();

        // Create a custom response object with payment responses and total amount
        Map<String, Object> response = new HashMap<>();
        response.put("content", paymentResponses.getContent());
        response.put("totalPages", paymentResponses.getTotalPages());
        response.put("totalElements", paymentResponses.getTotalElements());
        response.put("last", paymentResponses.isLast());
        response.put("size", paymentResponses.getSize());
        response.put("number", paymentResponses.getNumber());
        response.put("sort", paymentResponses.getSort());
        response.put("numberOfElements", paymentResponses.getNumberOfElements());
        response.put("first", paymentResponses.isFirst());
        response.put("empty", paymentResponses.isEmpty());
        response.put("totalAmount", totalAmount);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dashboard/all")
    public ResponseEntity<List<Delivery>> getAllDelivery(@RequestParam(value = "packageName", required = false) String packageName,
                                                         @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Retrieve the authenticated user from the authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Fetch the user object based on the user ID
        Optional<User> userOptional = userRepository.findById(userDetails.getId());

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Retrieve the deliveries created by the logged-in user
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Delivery> deliveries = deliveryRepository.findByUser(user, packageName, pageable);

            // Check if any deliveries are found
            if (deliveries.hasContent()) {
                return ResponseEntity.ok(deliveries.getContent());
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{deliveryId}/assign-rider")
    public ResponseEntity<?> assignRiderToDelivery(
            @PathVariable("deliveryId") long deliveryId,
            @RequestBody Rider rider
    ) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();

            // Fetch the rider object based on the rider ID
            Optional<Rider> optionalRider = riderRepository.findById(rider.getId());

            if (optionalRider.isPresent()) {
                Rider assignedRider = optionalRider.get();
                delivery.setRider(assignedRider);

                // Save the updated delivery with the assigned rider
                deliveryRepository.save(delivery);

                return ResponseEntity.ok("Rider assigned to delivery successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/dashboard/{id}/status")
    public ResponseEntity<String> updateDeliveryStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");

        if (newStatus != null) {
            Optional<Delivery> optionalDelivery = deliveryRepository.findById(id);

            if (optionalDelivery.isPresent()) {
                Delivery delivery = optionalDelivery.get();
                try {
                    DeliveryStatus status = DeliveryStatus.valueOf(newStatus);
                    DeliveryStatus previousStatus = delivery.getDeliveryStatus();
                    delivery.setDeliveryStatus(status);
                    deliveryRepository.save(delivery);

                    // Sending email
                    String customerName = delivery.getCustomerName();
                    String pickupAddress = delivery.getPickupAddress();
                    String dropoffAddress = delivery.getDropAddress();
                    Rider rider = delivery.getRider();
                    double amount = delivery.getAmount();
                    LocalDateTime timestamp = delivery.getTimestamp();

                    if (status == DeliveryStatus.COMPLETED && previousStatus != DeliveryStatus.COMPLETED) {
                        sendCongratulationsEmail(customerName, pickupAddress, dropoffAddress, rider, amount, timestamp, delivery);
                    } else if (status == DeliveryStatus.CANCELLED && previousStatus != DeliveryStatus.CANCELLED) {
                        sendCancellationEmail(customerName, pickupAddress, dropoffAddress, rider, amount, timestamp, delivery);
                    }

                    return ResponseEntity.ok("Delivery status updated successfully");
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

    private void sendCongratulationsEmail(String customerName, String pickupAddress, String dropoffAddress, Rider rider, double amount, LocalDateTime timestamp, Delivery delivery) {
        String subject = "Congratulations! Your order was successfully shipped";
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
                "<img class='logo' src='https://i.ibb.co/0GX70CY/Photo-from-Gray.jpg' alt='Logo'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Congratulations, " + customerName + "!</h1>" +
                "<p>Your order was successfully shipped.</p>" +
                "<p>Your details are as follows:</p>" +
                "<p>From: " + pickupAddress + "</p>" +
                "<p>To: " + dropoffAddress + "</p>" +
                "<p>Your dispatch rider's name: " + rider.getName() + "</p>" +
                "<p>Your dispatch rider's phone: " + rider.getPhone() + "</p>" +
                "<p>Total amount paid: " + amount + "</p>" +
                "<p>Timestamp of the delivery: " + timestamp + "</p>" +
                "<p>Regards, the  Logistics team</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(delivery.getCustomerEmail(), subject, body);
    }

    private void sendCancellationEmail(String customerName, String pickupAddress, String dropoffAddress, Rider rider, double amount, LocalDateTime timestamp, Delivery delivery) {
        String subject = "Delivery Cancelled";
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
                "<img class='logo' src='https://i.ibb.co/0GX70CY/Photo-from-Gray.jpg' alt='Logo'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Delivery Cancelled</h1>" +
                "<p>Dear " + customerName + ",</p>" +
                "<p>Your delivery was cancelled by your rider.</p>" +
                "<p>Your details are as follows:</p>" +
                "<p>From: " + pickupAddress + "</p>" +
                "<p>To: " + dropoffAddress + "</p>" +
                "<p>Your dispatch rider's name: " + rider.getName() + "</p>" +
                "<p>Your dispatch rider's phone: " + rider.getPhone() + "</p>" +
                "<p>Total amount paid: " + amount + "</p>" +
                "<p>Timestamp of the delivery: " + timestamp + "</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        sendEmail(delivery.getCustomerEmail(), subject, body);
    }
    private void sendEmail(String email, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("jmnusltd@gmail.com", "udnafhivdjqzelez");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(body, "text/html");


            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // Mobile
    @GetMapping("/mobile/all")
    public ResponseEntity<Page<MobileDelivery>> getAllMobileDelivery(
            @RequestParam(required = false) String packageName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<MobileDelivery> deliveries;

            if (packageName == null) {
                deliveries = mobileDeliveryRepository.findAll(paging);
            } else {
                deliveries = mobileDeliveryRepository.findByPackageName(packageName, paging);
            }

            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deliveries/total")
    public Long getTotalMobileDeliveries() {
        return deliveryRepository.count();
    }

    // @GetMapping("/delivery")
    // public ResponseEntity<List<Delivery>> getAllDelivery(@RequestParam(required =
    // false) String packageName) {
    // try {
    // List<Delivery> deliveries = new ArrayList<Delivery>();

    // if (packageName == null)
    // deliveryRepository.findAll().forEach(deliveries::add);
    // else
    // deliveryRepository.findByPackageName(packageName).forEach(deliveries::add);

    // if (deliveries.isEmpty()) {
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

    // return new ResponseEntity<>(deliveries, HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    @GetMapping("/deliveries")
    public ResponseEntity<List<Delivery>> getAllMobileDeliveries() {
        try {
            List<Delivery> deliveries = deliveryRepository.findAll();
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(deliveries, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/delivery/{id}")
    public ResponseEntity<MobileDelivery> getMobileDeliveryById(@PathVariable("id") long id) {
        Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findById(id);

        if (deliveryData.isPresent()) {
            return new ResponseEntity<>(deliveryData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/delivery")
//    public ResponseEntity<MobileDelivery> createMobileDelivery(@RequestParam Long userId, @RequestBody MobileDelivery delivery) {
//        try {
//            Optional<MobileUser> userData = mobileUserRepository.findById(userId);
//            if (userData.isPresent()) {
//                MobileUser user = userData.get();
//                delivery.setUser(user);
//                delivery.setDeliveryTime(LocalDateTime.now()); // Set delivery time with current timestamp
//                MobileDelivery _delivery = mobileDeliveryRepository.save(delivery);
//                return new ResponseEntity<>(_delivery, HttpStatus.CREATED);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/mobile/delivery")
//    public ResponseEntity<MobileDelivery> createMobileDelivery(@RequestParam Long userId, @RequestParam(required = false) LocalDateTime deliveryTime, @RequestBody MobileDelivery delivery) {
//        try {
//            Optional<MobileUser> userData = mobileUserRepository.findById(userId);
//            if (userData.isPresent()) {
//                MobileUser user = userData.get();
//                delivery.setUser(user);
//                delivery.setDeliveryTime(deliveryTime != null ? deliveryTime : LocalDateTime.now()); // Set delivery time based on the input or current timestamp
//
//                // Get a list of available riders without any active delivery
//                List<Rider> availableRiders = riderRepository.findByAvailable(true);
//
//                List<Rider> eligibleRiders = availableRiders.stream()
//                        .filter(rider -> rider.getMobileDelivery() == null ||
//                                (rider.getMobileDelivery().getStatus() != DeliveryStatus.PICKED_UP &&
//                                        rider.getMobileDelivery().getStatus() != DeliveryStatus.IN_PROGRESS))
//                        .collect(Collectors.toList());
//
//                if (!eligibleRiders.isEmpty()) {
//                    // Randomly select an eligible rider
//                    Random random = new Random();
//                    int index = random.nextInt(eligibleRiders.size());
//                    Rider rider = eligibleRiders.get(index);
//
//                    delivery.setRider(rider);
//                    MobileDelivery _delivery = mobileDeliveryRepository.save(delivery);
//                    return new ResponseEntity<>(_delivery, HttpStatus.CREATED);
//                } else {
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PutMapping("/mobile/delivery/{id}")
    public ResponseEntity<MobileDelivery> updateMobileDelivery(@PathVariable("id") long id, @RequestBody MobileDelivery delivery) {
        Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findById(id);

        if (deliveryData.isPresent()) {
            MobileDelivery _delivery = deliveryData.get();
            _delivery.setDeliveryRider(delivery.getDeliveryRider());
            _delivery.setPackageName(delivery.getPackageName());
            _delivery.setPackageType(delivery.getPackageType());
            _delivery.setDropAddress(delivery.getDropAddress());
            _delivery.setPickupAddress(delivery.getPickupAddress());
            _delivery.setDelivered(delivery.isDelivered());
            return new ResponseEntity<>(mobileDeliveryRepository.save(_delivery), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/mobile/delivery/{id}")
    public ResponseEntity<HttpStatus> deleteMobileDelivery(@PathVariable("id") long id) {
        try {
            deliveryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/delivery/{id}/status")
    public ResponseEntity<MobileDelivery> getDeliveryById(@PathVariable("id") Long id) {
        try {
            Optional<MobileDelivery> delivery = mobileDeliveryRepository.findById(id);
            if (delivery.isPresent()) {
                return new ResponseEntity<>(delivery.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/delivery/{id}/status/rider")
    public ResponseEntity<MobileDelivery> getTheDeliveryByRiderAndId(
            @PathVariable("id") Long id,
            @RequestParam("riderId") Long riderId
    ) {
        try {
            Optional<Rider> rider = riderRepository.findById(riderId);
            if (rider.isPresent()) {
                Optional<MobileDelivery> delivery = mobileDeliveryRepository.findByIdAndRider(id, rider.get());
                if (delivery.isPresent()) {
                    return new ResponseEntity<>(delivery.get(), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/mobile/delivery/{id}/status")
    public ResponseEntity<MobileDelivery> updateDeliveryStatus(@PathVariable("id") long id,
                                                               @RequestParam(value = "riderId", required = false) Long riderId,
                                                               @RequestBody MobileDelivery delivery) {
        Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findById(id);

        if (deliveryData.isPresent()) {
            MobileDelivery existingDelivery = deliveryData.get();
            existingDelivery.setStatus(delivery.getStatus());
            existingDelivery.setDeliveryRider(delivery.getDeliveryRider());
            existingDelivery.setDeliveryRiderNumber(delivery.getDeliveryRiderNumber());
            existingDelivery.setAdditionalStatus(delivery.getAdditionalStatus());
            existingDelivery.setDelivered(false);

            // Associate the mobile delivery with the rider
            if (riderId != null) {
                Rider rider = riderRepository.findById(riderId)
                        .orElseThrow(() -> new RuntimeException("Rider not found"));

                existingDelivery.setRider(rider); // Set the rider for the mobile delivery
                existingDelivery.setDeliveryRider(rider.getName()); // Set the name of the rider
            }

            MobileDelivery savedDelivery = mobileDeliveryRepository.save(existingDelivery);

            if (savedDelivery.getStatus() == DeliveryStatus.CANCELLED) {
                sendCancelledEmail(savedDelivery);
            } else if (savedDelivery.getStatus() == DeliveryStatus.COMPLETED) {
                sendCompleteEmail(savedDelivery);
            }

            return ResponseEntity.ok(savedDelivery);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void sendCancelledEmail(MobileDelivery delivery) {
        MobileUser user = delivery.getUser();
        String subject = "Delivery Cancelled";
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
                "<img class='logo' src='https://i.ibb.co/0GX70CY/Photo-from-Gray.jpg' alt='Logo'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Delivery Cancelled</h1>" +
                "<p>Dear " + user.getName() + ",</p>" +
                "<p>We regret to inform you that your delivery with ID " + delivery.getId() +
                " has been cancelled.</p>" +
                "<p>Delivery Details:</p>" +
                "<p>From: " + delivery.getPickupAddress() + "</p>" +
                "<p>To: " + delivery.getDropAddress() + "</p>" +
                "<p>Delivery Rider: " + delivery.getDeliveryRider() + "</p>" +
                "<p>Delivery Rider's Phone: " + delivery.getDeliveryRiderNumber() + "</p>" +
                "<p>Total Amount Paid: " + delivery.getAmount() + "</p>" +
                "<p>Timestamp: " + delivery.getDeliveryTime() + "</p>" +
                "<p>If you have any questions or concerns, please contact our support team.</p>" +
                "<p>Regards,</p>" +
                "<p>The  Logistics Team</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendMobileEmail(user.getEmail(), subject, body);
    }

    private void sendCompleteEmail(MobileDelivery delivery) {
        MobileUser user = delivery.getUser();
        String subject = "Delivery Complete";
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
                "<img class='logo' src='https://i.ibb.co/0GX70CY/Photo-from-Gray.jpg' alt='Logo'>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Delivery Complete</h1>" +
                "<p>Dear " + user.getName() + ",</p>" +
                "<p>Your delivery with ID " + delivery.getId() + " has been successfully completed.</p>" +
                "<p>Delivery Details:</p>" +
                "<p>From: " + delivery.getPickupAddress() + "</p>" +
                "<p>To: " + delivery.getDropAddress() + "</p>" +
                "<p>Delivery Rider: " + delivery.getDeliveryRider() + "</p>" +
                "<p>Delivery Rider's Phone: " + delivery.getDeliveryRiderNumber() + "</p>" +
                "<p>Total Amount Paid: " + delivery.getAmount() + "</p>" +
                "<p>Timestamp: " + delivery.getDeliveryTime() + "</p>" +
                "<p>Thank you for choosing  Logistics!</p>" +
                "<p>Regards,</p>" +
                "<p>The  Logistics Team</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendMobileEmail(user.getEmail(), subject, body);
    }

    private void sendMobileEmail(String email, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("jmnusltd@gmail.com", "udnafhivdjqzelez");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email, " Logistics"));
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

    @DeleteMapping("/mobile/delivery")
    public ResponseEntity<HttpStatus> deleteAllMobileDelivery() {
        try {
            mobileDeliveryRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/mobile/delivery/delivered")
    public ResponseEntity<List<MobileDelivery>> findByMobileDelivered() {
        try {
            List<MobileDelivery> delivery = mobileDeliveryRepository.findByDelivered(true);

            if (delivery.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(delivery, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/deliveries/user/{userId}")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByUser(@PathVariable Long userId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByUser(user);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/user/{userId}/delivery/{deliveryId}")
    public ResponseEntity<MobileDelivery> getDeliveryByUserAndId(@PathVariable Long userId, @PathVariable Long deliveryId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findByIdAndUser(deliveryId, user);

            if (deliveryData.isPresent()) {
                MobileDelivery delivery = deliveryData.get();
                return new ResponseEntity<>(delivery, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/delivery/in-progress")
    public ResponseEntity<List<MobileDelivery>> getInProgressDeliveries() {
        try {
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByStatus(DeliveryStatus.IN_PROGRESS);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/delivery/picked-up")
    public ResponseEntity<List<MobileDelivery>> getPickedUpDeliveries() {
        try {
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByStatus(DeliveryStatus.IN_PROGRESS);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/mobile/delivery/rider/in-progress")
    public ResponseEntity<List<MobileDelivery>> getInProgressDeliveries(@RequestParam Long riderId) {
        try {
            List<DeliveryStatus> statuses = Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.PICKED_UP);
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRider_IdAndStatusIn(riderId, statuses);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/mobile/delivery/rider/completed")
    public ResponseEntity<List<MobileDelivery>> getCompleteDeliveries(@RequestParam Long riderId) {
        try {
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRider_IdAndStatus(riderId, DeliveryStatus.COMPLETED);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/delivery/rider/cancelled")
    public ResponseEntity<List<MobileDelivery>> getCancelledDeliveries(@RequestParam Long riderId) {
        try {
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRider_IdAndStatus(riderId, DeliveryStatus.CANCELLED);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/mobile/delivery/not-delivered")
    public ResponseEntity<List<MobileDelivery>> getNotDeliveredDeliveries() {
        try {
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByStatus(DeliveryStatus.PENDING);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/delivery/delivery-complete")
    public ResponseEntity<List<MobileDelivery>> getDeliveredDeliveries() {
        try {
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByStatus(DeliveryStatus.COMPLETED);
            if (deliveries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/deliveries/user/{userId}/delivery/{deliveryId}/in-progress")
    public ResponseEntity<MobileDelivery> getDeliveryByUserAndIdStatus(@PathVariable Long userId,
                                                                       @PathVariable Long deliveryId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            List<DeliveryStatus> statuses = Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.PICKED_UP);
            Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findByIdAndUserAndStatusIn(deliveryId, user, statuses);

            if (deliveryData.isPresent()) {
                MobileDelivery delivery = deliveryData.get();
                return new ResponseEntity<>(delivery, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/user/{userId}/in-progress")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByUserStatus(@PathVariable Long userId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            List<DeliveryStatus> statuses = Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.PICKED_UP);
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByUserAndStatusIn(user, statuses);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/mobile/deliveries/user/{userId}/completed")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByUserStatusCompleted(@PathVariable Long userId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByUserAndStatus(user, DeliveryStatus.COMPLETED);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/user/{userId}/not-delivered")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByUserStatusNotDelivered(@PathVariable Long userId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByUserAndStatus(user, DeliveryStatus.PENDING);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/user/{userId}/cancelled")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByUserStatusCancelled(@PathVariable Long userId) {
        Optional<MobileUser> userData = mobileUserRepository.findById(userId);

        if (userData.isPresent()) {
            MobileUser user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByUserAndStatus(user, DeliveryStatus.CANCELLED);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // For Riders

    @GetMapping("/deliveries/rider")
    public ResponseEntity<Page<MobileDelivery>> getDeliveriesByRiders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch the user object based on the provided user ID
        Optional<User> userOptional = userRepository.findById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a pageable object for pagination
            Pageable pageable = PageRequest.of(page, size);

            // Fetch the associated rider accounts for the specified user
            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            // Collect the rider IDs
            List<Rider> riders = riderAccounts.getContent();

            // Fetch the deliveries associated with the rider accounts and pagination
            Page<MobileDelivery> deliveries = mobileDeliveryRepository.findAllByRiders(riders, pageable);

            return ResponseEntity.ok(deliveries);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deliveries/rider/payments")
    public ResponseEntity<Page<PaymentResponse>> getDeliveriesPaymentsByRiders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch the user object based on the provided user ID
        Optional<User> userOptional = userRepository.findById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a pageable object for pagination
            Pageable pageable = PageRequest.of(page, size);

            // Fetch the associated rider accounts for the specified user
            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            // Collect the rider IDs
            List<Rider> riders = riderAccounts.getContent();

            // Fetch the deliveries associated with the rider accounts and pagination
            Page<MobileDelivery> deliveries = mobileDeliveryRepository.findAllByRiders(riders, pageable);

            // Fetch the payment responses for the fetched deliveries
            List<PaymentResponse> paymentResponses = paymentResponseRepository.findByDeliveryIn(deliveries.getContent());

            return ResponseEntity.ok(new PageImpl<>(paymentResponses, pageable, deliveries.getTotalElements()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deliveries/rider/payments/search")
    public ResponseEntity<Map<String, Object>> getDeliveriesPaymentsByRiders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String deliveryRider
    ) {
        // Fetch the user object based on the provided user ID
        Optional<User> userOptional = userRepository.findById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a pageable object for pagination
            Pageable pageable = PageRequest.of(page, size);

            // Fetch the associated rider accounts for the specified user
            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            // Collect the rider IDs
            List<Rider> riders = riderAccounts.getContent();

            // Fetch all deliveries associated with the rider accounts
            Page<MobileDelivery> deliveries = mobileDeliveryRepository.findAllByRiders(riders, pageable);

            // Perform filtering on the fetched deliveries based on the provided parameters
            List<MobileDelivery> filteredDeliveries = deliveries.getContent().stream()
                    .filter(delivery -> {
                        // Check if the delivery falls within the specified date range
                        if (fromDate != null && toDate != null) {
                            LocalDate deliveryDate = delivery.getDeliveryTime().toLocalDate();
                            LocalDate from = LocalDate.parse(fromDate);
                            LocalDate to = LocalDate.parse(toDate);
                            return deliveryDate.isEqual(from) || deliveryDate.isEqual(to) || (deliveryDate.isAfter(from) && deliveryDate.isBefore(to));
                        }
                        return true;
                    })
                    .filter(delivery -> {
                        // Check if the delivery belongs to the specified delivery rider
                        if (deliveryRider != null) {
                            return delivery.getRider().getName().equalsIgnoreCase(deliveryRider);
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            // Fetch the payment responses for the filtered deliveries
            List<PaymentResponse> paymentResponses = paymentResponseRepository.findByDeliveryIn(filteredDeliveries);

            // Calculate the total amount
            double totalAmount = paymentResponses.stream()
                    .mapToDouble(PaymentResponse::getAmount)
                    .sum();

            // Create a custom response object with payment responses, total amount, and pageable information
            Map<String, Object> response = new HashMap<>();
            response.put("content", paymentResponses);
            response.put("totalPages", deliveries.getTotalPages());
            response.put("totalElements", deliveries.getTotalElements());
            response.put("last", deliveries.isLast());
            response.put("size", deliveries.getSize());
            response.put("number", deliveries.getNumber());
            response.put("sort", deliveries.getSort());
            response.put("numberOfElements", deliveries.getNumberOfElements());
            response.put("first", deliveries.isFirst());
            response.put("empty", deliveries.isEmpty());
            response.put("totalAmount", totalAmount);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deliveries/rider/payments/excel-report")
    public ResponseEntity<String> generateExcelReport(
            @RequestParam Long userId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String deliveryRider,
            HttpServletResponse response
    ) {
        // Fetch the user object based on the provided user ID
        Optional<User> userOptional = userRepository.findById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a pageable object to fetch all records
            Pageable pageable = Pageable.unpaged();

            // Fetch the associated rider accounts for the specified user
            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            // Collect the rider IDs
            List<Rider> riders = riderAccounts.getContent();

            // Fetch all deliveries associated with the rider accounts
            Page<MobileDelivery> deliveries = mobileDeliveryRepository.findAllByRiders(riders, pageable);

            // Perform filtering on the fetched deliveries based on the provided parameters
            List<MobileDelivery> filteredDeliveries = deliveries.getContent().stream()
                    .filter(delivery -> {
                        // Check if the delivery falls within the specified date range
                        if (fromDate != null && toDate != null) {
                            LocalDate deliveryDate = delivery.getDeliveryTime().toLocalDate();
                            LocalDate from = LocalDate.parse(fromDate);
                            LocalDate to = LocalDate.parse(toDate);
                            return deliveryDate.isEqual(from) || deliveryDate.isEqual(to) || (deliveryDate.isAfter(from) && deliveryDate.isBefore(to));
                        }
                        return true;
                    })
                    .filter(delivery -> {
                        // Check if the delivery belongs to the specified delivery rider
                        if (deliveryRider != null) {
                            return delivery.getRider().getName().equalsIgnoreCase(deliveryRider);
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            // Fetch the payment responses for the filtered deliveries
            List<PaymentResponse> paymentResponses = paymentResponseRepository.findByDeliveryIn(filteredDeliveries);

            // Generate the Excel report
            byte[] excelData = generateExcel(paymentResponses);

            // Send the Excel report via email
            sendEmailWithAttachment(excelData, user.getCompanyName());

            return ResponseEntity.ok("Excel report generated and sent via email.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private byte[] generateExcel(List<PaymentResponse> paymentResponses) {
        // TODO: Implement the logic to generate the Excel report based on the paymentResponses
        return new byte[0];
    }

    private void sendEmailWithAttachment(byte[] attachmentData, String companyName) {
        // TODO: Implement the logic to send the email with the Excel attachment
    }




//    @GetMapping("/deliveries/rider/count")
//    public ResponseEntity<Long> getDeliveryTotalCountByRiders(
//            @RequestParam Long userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        // Fetch the user object based on the provided user ID
//        Optional<User> userOptional = userRepository.findById(userId);
//
//        // Check if the user exists
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Create a pageable object for pagination
//            Pageable pageable = PageRequest.of(page, size);
//
//            // Fetch the associated rider accounts for the specified user
//            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);
//
//            // Collect the rider IDs
//            List<Rider> riders = riderAccounts.getContent();
//
//            // Manually count the deliveries associated with the rider accounts
//            Long deliveryCount = mobileDeliveryRepository.countDeliveriesByRiders(riders);
//
//            return ResponseEntity.ok(deliveryCount);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


//    @GetMapping("/deliveries/rider/count")
//    public ResponseEntity<Long> getDeliveryCountByRiders(@RequestParam Long userId) {
//        // Fetch the user object based on the provided user ID
//        Optional<User> userOptional = userRepository.findById(userId);
//
//        // Check if the user exists
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Fetch the associated rider accounts for the specified user
//            List<Rider> riderAccounts = riderRepository.findByUser(user);
//
//            // Fetch the total count of deliveries associated with the rider accounts
//            Long deliveryCount = mobileDeliveryRepository.countByRiders(riderAccounts);
//
//            return ResponseEntity.ok(deliveryCount);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


    @GetMapping("/deliveries/rider/{deliveryId}")
    public ResponseEntity<MobileDelivery> getDeliveryByDeliveryId(@PathVariable Long deliveryId) {
        // Fetch the delivery by its ID
        Optional<MobileDelivery> deliveryOptional = mobileDeliveryRepository.findById(deliveryId);

        // Check if the delivery exists
        if (deliveryOptional.isPresent()) {
            MobileDelivery delivery = deliveryOptional.get();

            // Return the delivery as a response
            return ResponseEntity.ok(delivery);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deliveries/rider/count")
    public ResponseEntity<Long> getDeliveryCountByRiders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch the user object based on the provided user ID
        Optional<User> userOptional = userRepository.findById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a pageable object for fetching deliveries
            Pageable pageable = PageRequest.of(page, size);

            // Fetch the associated rider accounts for the specified user with pagination
            Page<Rider> riderAccounts = riderRepository.findByUser(user, pageable);

            // Collect the rider IDs
            List<Rider> riders = riderAccounts.getContent();

            // Fetch the deliveries associated with the rider accounts and pagination
            Page<MobileDelivery> deliveries = mobileDeliveryRepository.findAllByRiders(riders, pageable);

            // Retrieve the total count of deliveries
            long deliveryCount = deliveries.getTotalElements();

            return ResponseEntity.ok(deliveryCount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @GetMapping("/mobile/deliveries/rider/{userId}")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByRider(@PathVariable Long userId) {
        Optional<Rider> riderData = riderRepository.findById(userId);

        if (riderData.isPresent()) {
            Rider rider = riderData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRider(rider);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/rider/{userId}/delivery/{deliveryId}")
    public ResponseEntity<MobileDelivery> getDeliveryByRiderAndId(@PathVariable Long userId, @PathVariable Long deliveryId) {
        Optional<Rider> riderData = riderRepository.findById(userId);

        if (riderData.isPresent()) {
            Rider rider = riderData.get();
            Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findByIdAndRider(deliveryId, rider);

            if (deliveryData.isPresent()) {
                MobileDelivery delivery = deliveryData.get();
                return new ResponseEntity<>(delivery, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/rider/{userId}/delivery/{deliveryId}/in-progress")
    public ResponseEntity<MobileDelivery> getDeliveryByRiderAndIdStatus(@PathVariable Long userId,
                                                                        @PathVariable Long deliveryId) {
        Optional<Rider> userData = riderRepository.findById(userId);

        if (userData.isPresent()) {
            Rider user = userData.get();
            List<DeliveryStatus> statuses = Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.PICKED_UP);
            Optional<MobileDelivery> deliveryData = mobileDeliveryRepository.findByIdAndRiderAndStatusIn(deliveryId, user, statuses);

            if (deliveryData.isPresent()) {
                MobileDelivery delivery = deliveryData.get();
                return new ResponseEntity<>(delivery, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/rider/{userId}/in-progress")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByRiderStatus(@PathVariable Long userId) {
        Optional<Rider> userData = riderRepository.findById(userId);

        if (userData.isPresent()) {
            Rider user = userData.get();
            List<DeliveryStatus> statuses = Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.PICKED_UP);
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRiderAndStatusIn(user, statuses);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/mobile/deliveries/rider/{userId}/completed")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByRiderStatusCompleted(@PathVariable Long userId) {
        Optional<Rider> userData = riderRepository.findById(userId);

        if (userData.isPresent()) {
            Rider user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRiderAndStatus(user, DeliveryStatus.COMPLETED);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/rider/{userId}/not-delivered")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByRiderStatusNotDelivered(@PathVariable Long userId) {
        Optional<Rider> userData = riderRepository.findById(userId);

        if (userData.isPresent()) {
            Rider user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRiderAndStatus(user, DeliveryStatus.PENDING);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mobile/deliveries/rider/{userId}/cancelled")
    public ResponseEntity<List<MobileDelivery>> getDeliveriesByRiderStatusCancelled(@PathVariable Long userId) {
        Optional<Rider> userData = riderRepository.findById(userId);

        if (userData.isPresent()) {
            Rider user = userData.get();
            List<MobileDelivery> deliveries = mobileDeliveryRepository.findByRiderAndStatus(user, DeliveryStatus.CANCELLED);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/business-riders")
//    public List<MobileDelivery> getBusinessRidersDeliveries(Authentication authentication) {
//        // Retrieve the UserDetails from the authentication object
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        // Get the logged-in user's username
//        String username = userDetails.getUsername();
//
//        // Retrieve the user from the repository
//        User user = userRepository.findByUsername(username).orElse(null);
//
//        if (user == null) {
//            // Handle the case when the user is not found
//            // Return an appropriate response or throw an exception
//            // For example, you can throw a NotFoundException:
//            throw new ResourceNotFoundException("Not Found");
//        }
//
//        // Retrieve the business riders' mobile deliveries associated with the logged-in user
//        return userMobileDeliveryRepository.findByUser(user);
//    }


}