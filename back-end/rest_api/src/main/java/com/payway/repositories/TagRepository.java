package com.payway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payway.models.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
