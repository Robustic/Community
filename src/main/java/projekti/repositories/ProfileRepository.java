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
    Page<Profile> findByNameContaining(String lastname, Pageable pageable);
    Page<Profile> findByNameContainingAndAliasNot(String lastname, String alias, Pageable pageable);
}

