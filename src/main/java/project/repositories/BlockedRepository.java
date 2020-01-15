package project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Blocked;
import project.entities.Profile;


public interface BlockedRepository extends JpaRepository<Blocked, Long> {
    Blocked findByBlockerAndBlocked(Profile blocker, Profile blocked);
    List<Blocked> findByBlocker(Profile blocker);
    List<Blocked> findByBlocked(Profile blocked);
}
