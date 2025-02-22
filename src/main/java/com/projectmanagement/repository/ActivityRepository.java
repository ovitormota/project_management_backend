package com.projectmanagement.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectmanagement.domain.Activity;
import com.projectmanagement.domain.enumeration.Status;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    long countByStatusAndEndDateBefore(Status status, Instant currentDate);

    long countByStatus(Status status);
}
