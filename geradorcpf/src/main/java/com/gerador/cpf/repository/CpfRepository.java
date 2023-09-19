package com.gerador.cpf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerador.cpf.model.Cpf;

@Repository
public interface CpfRepository extends JpaRepository<Cpf, Long>{

}
