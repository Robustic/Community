package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Blocked;
import projekti.entities.Profile;


public interface BlockedRepository extends JpaRepository<Blocked, Long> {
    Blocked findByBlockerAndBlocked(Profile blocker, Profile blocked);
    List<Blocked> findByBlocker(Profile blocker);
}
