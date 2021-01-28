package movie.festival.repository;

import movie.festival.model.User;
import movie.festival.payload.EmailAndUsernameResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(
            value = "SELECT email FROM users")
    public List<String>  getAllEmail();

    //@Query(value = "SELECT s.email,s.user_name as userName FROM users as s",nativeQuery = true) //bisa pakai native query
    @Query(value = "SELECT s FROM users as s") //auto bind
    public List<EmailAndUsernameResponse>  getAllEmailAndUsername();

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUserNameOrEmail(String username, String email);

//    @Query(
//            value = "SELECT s FROM users as s")
//    public List<EmailAndUsernameResponse>  getAllEmailAndUsername();
}
