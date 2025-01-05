package com.payway.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.payway.models.Pass;
import org.springframework.stereotype.Repository;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {
    // Κάποιες custom μέθοδοι, αν απαιτούνται
}