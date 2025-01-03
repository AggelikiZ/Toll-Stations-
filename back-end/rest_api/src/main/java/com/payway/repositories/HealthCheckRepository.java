package com.payway.repositories;
import com.payway.models.TollStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface HealthCheckRepository extends JpaRepository<TollStation, Long> {


    // Query to count the number of toll stations
    @Query("SELECT COUNT(s) FROM TollStation s")
    long countStations();

    // Query to count the number of tags (if you have a Tag entity)
    @Query("SELECT COUNT(t) FROM Tag t")
    long countTags();

    // Query to count the number of passes (if you have a Pass entity)
    @Query("SELECT COUNT(p) FROM Pass p")
    long countPasses();
}
