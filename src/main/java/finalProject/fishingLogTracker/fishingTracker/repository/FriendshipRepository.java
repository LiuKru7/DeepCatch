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

    /**
     * Finds all friendship requests sent by the given user with a specific status.
     *
     * @param userId the ID of the sender
     * @param status the friendship status (e.g., PENDING, ACCEPTED)
     * @return list of friendships where the user is the sender and status matches
     */
    @Query("""
        SELECT f FROM Friendship f
        WHERE f.sender.id = :userId AND f.status = :status
    """)
    List<Friendship> findBySenderIdAndStatus(@Param("userId") Long userId,
                                             @Param("status") FriendshipStatus status);

    /**
     * Finds all friendship requests received by the given user with a specific status.
     *
     * @param userId the ID of the receiver
     * @param status the friendship status (e.g., PENDING, ACCEPTED)
     * @return list of friendships where the user is the receiver and status matches
     */
    @Query("""
        SELECT f FROM Friendship f
        WHERE f.receiver.id = :userId AND f.status = :status
    """)
    List<Friendship> findByReceiverIdAndStatus(@Param("userId") Long userId,
                                               @Param("status") FriendshipStatus status);

    /**
     * Finds all accepted friendships for a given user, regardless of whether the user
     * was the sender or the receiver.
     *
     * @param userId the ID of the user
     * @return list of accepted friendships involving the user
     */
    @Query("""
        SELECT f FROM Friendship f
        WHERE (f.sender.id = :userId OR f.receiver.id = :userId)
        AND f.status = 'ACCEPTED'
    """)
    List<Friendship> findAcceptedFriendshipsForUser(@Param("userId") Long userId);

    /**
     * Finds a friendship between two users with a specific status.
     *
     * @param senderId the ID of the sender
     * @param receiverId the ID of the receiver
     * @param status the friendship status
     * @return optional friendship between two users with the given status
     */
    Optional<Friendship> findBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, FriendshipStatus status);

    /**
     * Checks whether a friendship exists between two users in either direction.
     *
     * @param userId1 the ID of the first user
     * @param userId2 the ID of the second user
     * @return true if any friendship exists between the two users
     */
    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM Friendship f
        WHERE (f.sender.id = :userId1 AND f.receiver.id = :userId2)
           OR (f.sender.id = :userId2 AND f.receiver.id = :userId1)
    """)
    boolean existsBySenderIdAndReceiverIdOrViceVersa(@Param("userId1") Long userId1,
                                                     @Param("userId2") Long userId2);

    /**
     * Finds a friendship between two users regardless of who sent the request.
     *
     * @param userId1 the ID of one user
     * @param userId2 the ID of the other user
     * @return optional friendship if exists between the two users
     */
    @Query("""
        SELECT f FROM Friendship f
        WHERE (f.sender.id = :userId1 AND f.receiver.id = :userId2)
           OR (f.sender.id = :userId2 AND f.receiver.id = :userId1)
    """)
    Optional<Friendship> findByUsers(@Param("userId1") Long userId1,
                                     @Param("userId2") Long userId2);
}
