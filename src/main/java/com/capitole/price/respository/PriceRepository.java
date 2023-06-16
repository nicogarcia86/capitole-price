package com.capitole.price.respository;

import com.capitole.price.model.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Integer> {

    @Query("SELECT p FROM PriceEntity p WHERE p.brandId = ?1 AND" +
            " p.productId = ?2 AND" +
            " p.startDate <= ?3 AND" +
            " p.endDate >= ?3" +
            " ORDER BY p.priority Desc")
    List<PriceEntity> findActivePrices(int brandId, int productId, LocalDateTime currentDate);

}
