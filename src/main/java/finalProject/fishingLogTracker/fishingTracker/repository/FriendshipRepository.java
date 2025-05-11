package finalProject.fishingLogTracker.fishingTracker.repository;

import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("""
    SELECT f FROM Friendship f
    WHERE f.sender.id = :userId AND f.status = :status
    """)
    List<Friendship> findBySenderIdAndStatus(@Param("userId") Long userId,
                                             @Param("status") FriendshipStatus status);

    @Query("""
    SELECT f FROM Friendship f
    WHERE f.receiver.id = :userId AND f.status = :status
    """)
    List<Friendship> findByReceiverIdAndStatus(@Param("userId") Long userId,
                                               @Param("status") FriendshipStatus status);

    @Query("""
    SELECT f FROM Friendship f
    WHERE (f.sender.id = :userId OR f.receiver.id = :userId)
    AND f.status = 'ACCEPTED'
    """)
    List<Friendship> findAcceptedFriendshipsForUser(@Param("userId") Long userId);

    Optional<Friendship> findBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, FriendshipStatus status);

    @Query("""
    SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
    FROM Friendship f
    WHERE (f.sender.id = :userId1 AND f.receiver.id = :userId2)
       OR (f.sender.id = :userId2 AND f.receiver.id = :userId1)
""")
    boolean existsBySenderIdAndReceiverIdOrViceVersa(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("""
        SELECT f FROM Friendship f
        WHERE (f.sender.id = :userId1 AND f.receiver.id = :userId2)
           OR (f.sender.id = :userId2 AND f.receiver.id = :userId1)
    """)
    Optional<Friendship> findByUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

}
