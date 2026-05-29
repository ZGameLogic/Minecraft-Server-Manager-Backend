package com.zgamelogic.app.authentication.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthenticationDataRepository extends JpaRepository<AuthenticationData, UUID> {
    Optional<AuthenticationData> findByMsmToken(String token);
    @Query("""
    SELECT a
    FROM AuthenticationData a
    WHERE a.discordTokenExpiration BETWEEN CURRENT_TIMESTAMP AND :sixMinutesFromNow
    """)
    List<AuthenticationData> findAllExpiringBetweenNowAndSixMinutes(@Param("sixMinutesFromNow") Instant sixMinutesFromNow);
}
