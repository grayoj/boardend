package com.boardend.boardend.security.services;

import com.boardend.boardend.models.DeliveryStatus;
import com.boardend.boardend.models.MobileDelivery;
import com.boardend.boardend.models.Rider;
import com.boardend.boardend.repository.MobileDeliveryRepository;
import com.boardend.boardend.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    MobileDeliveryRepository mobileDeliveryRepository;

    @Autowired
    RiderRepository riderRepository;

    @Async
    @Transactional
    public void checkAndAssignDeliveries() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<MobileDelivery> pendingDeliveries = mobileDeliveryRepository.findByStatusAndDeliveryTimeBefore(DeliveryStatus.PENDING, currentTime);

        for (MobileDelivery delivery : pendingDeliveries) {
            Optional<Rider> availableRider = riderRepository.findByAvailableAndStatusIn(true, Arrays.asList(DeliveryStatus.IN_PROGRESS, DeliveryStatus.PICKED_UP));
            if (availableRider.isPresent()) {
                Rider rider = availableRider.get();
                delivery.setRider(rider);
                delivery.setStatus(DeliveryStatus.IN_PROGRESS);
                mobileDeliveryRepository.save(delivery);
            }
        }
    }

}
