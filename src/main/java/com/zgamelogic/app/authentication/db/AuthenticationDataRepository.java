package com.zgamelogic.app.authentication.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationDataRepository extends JpaRepository<AuthenticationData, AuthenticationData.AuthenticationDataId> {
}
