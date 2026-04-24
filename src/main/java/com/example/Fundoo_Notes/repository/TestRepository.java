package com.example.Fundoo_Notes.repository;

import com.example.Fundoo_Notes.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}