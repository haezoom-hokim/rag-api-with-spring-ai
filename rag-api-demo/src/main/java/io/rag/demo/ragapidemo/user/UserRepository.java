package io.rag.demo.ragapidemo.user;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  List<User> findByNameContaining(String name);

  List<User> findByAgeBetween(Integer minAge, Integer maxAge);

  List<User> findByAddressContaining(String address);

  @Query("SELECT u FROM users u WHERE u.age >= :minAge AND u.address LIKE %:city%")
  List<User> findByAgeAndCity(@Param("minAge") Integer minAge, @Param("city") String city);
}
