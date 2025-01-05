package com.payway.repositories;
import com.payway.models.TollStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TollStationRepository extends JpaRepository<TollStation, String> {
}
