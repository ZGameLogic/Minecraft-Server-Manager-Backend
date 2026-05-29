package com.zgamelogic.app.msm.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MinecraftServerDataRepository extends JpaRepository<MinecraftServerData, UUID> {
}
