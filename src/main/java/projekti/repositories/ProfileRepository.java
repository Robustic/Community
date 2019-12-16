package projekti.repositories;

import java.util.List;
import projekti.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByAccount(Account account);
    Profile findByAlias(String alias);
    List<Profile> findByNameContaining(String str);
    List<Profile> findByNameContainingIgnoreCase(String str);
    Page<Profile> findByNameContaining(String string, Pageable pageable);
    Page<Profile> findByNameContainingAndAliasNot(String string, String alias, Pageable pageable);
    Page<Profile> findByNameContainingIgnoreCaseAndAliasNot(String string, String alias, Pageable pageable);
}

