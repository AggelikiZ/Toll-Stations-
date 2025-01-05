package com.payway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payway.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t.ref FROM Tag t")
    List<String> findAllTagRefs();


}
