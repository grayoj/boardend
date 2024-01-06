package com.boardend.boardend.repository;

import com.boardend.boardend.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MobileDeliveryRepository extends JpaRepository<MobileDelivery, Long> {

    @Query("SELECT d FROM MobileDelivery d WHERE d.rider IN :riders")
    List<MobileDelivery> findAllByRiders(@Param("riders") List<Rider> riders);

    @Query("SELECT d FROM MobileDelivery d WHERE d.rider IN :riders")
    Page<MobileDelivery> findAllByRiders(@Param("riders") List<Rider> riders, Pageable pageable);

    List<MobileDelivery> findByDelivered(boolean delivered);

    Page<MobileDelivery> findByPackageName(String packageName, Pageable pageable);

    List<MobileDelivery> findByUser(MobileUser user);

    Optional<MobileDelivery> findByIdAndUser(Long id, MobileUser user);

    List<MobileDelivery> findByStatus(DeliveryStatus status);

    Optional<MobileDelivery> findByIdAndUserAndStatus(Long id, MobileUser user, DeliveryStatus status);

    List<MobileDelivery> findByUserAndStatus(MobileUser user, DeliveryStatus status);

    Optional<MobileDelivery> findByIdAndUserAndStatusIn(Long id, MobileUser user, List<DeliveryStatus> statuses);
    List<MobileDelivery> findByUserAndStatusIn(MobileUser user, List<DeliveryStatus> statuses);

    List<MobileDelivery> findByRider(Rider rider);
    Optional<MobileDelivery> findByIdAndRider(Long id, Rider rider);

    Optional<MobileDelivery> findByIdAndRiderAndStatus(Long id, Rider rider, DeliveryStatus status);

    List<MobileDelivery> findByRiderAndStatus(Rider rider, DeliveryStatus status);

    Optional<MobileDelivery> findByIdAndRiderAndStatusIn(Long id, Rider rider, List<DeliveryStatus> statuses);
    List<MobileDelivery> findByRiderAndStatusIn(Rider rider, List<DeliveryStatus> statuses);

    List<MobileDelivery> findByStatusAndDeliveryTimeBefore(DeliveryStatus status, LocalDateTime dateTime);

    Page<MobileDelivery> findByRiderUser(MobileUser user, Pageable pageable);


    Page<MobileDelivery> findByRiderUser(User user, Pageable pageable);


    Page<MobileDelivery> findByRiderUserAndPackageName(MobileUser user, String packageName, Pageable pageable);

    @Query("SELECT COUNT(d) FROM MobileDelivery d WHERE d.rider IN :riders")
    Long countByRiders(@Param("riders") List<Rider> riders);

    @Query("SELECT COUNT(md) FROM MobileDelivery md WHERE md.rider IN :riders")
    Long countDeliveriesByRiders(@Param("riders") List<Rider> riders);



    List<MobileDelivery> findByRider_IdAndStatus(Long riderId, DeliveryStatus status);

    List<MobileDelivery> findByRider_IdAndStatusIn(Long riderId, List<DeliveryStatus> statuses);
}
