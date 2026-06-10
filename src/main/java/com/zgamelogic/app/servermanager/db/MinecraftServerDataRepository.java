package com.zgamelogic.app.servermanager.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MinecraftServerDataRepository extends JpaRepository<MinecraftServerData, UUID> {
    List<MinecraftServerData> findAllByAutoStartIsTrue();
    List<MinecraftServerData> findAllByAutoRestartIsTrueAndAutoStartIsTrue();
}
