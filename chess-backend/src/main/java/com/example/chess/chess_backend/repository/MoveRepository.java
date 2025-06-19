package com.example.chess.chess_backend.repository;
import com.example.chess.chess_backend.entity.Move;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
    List<Move> findByGameIdOrderByTimestampAsc(Long gameId);
}
