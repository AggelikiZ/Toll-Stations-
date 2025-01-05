package com.payway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payway.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.opId = :opId")
    List<Tag> findByOpId(@Param("opId") String opId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM TollStation t WHERE t.opId = :opId")
    boolean existsByOpId(@Param("opId") String opId);

    @Query("SELECT t.ref FROM Tag t")
    List<String> findAllTagRefs();
}


