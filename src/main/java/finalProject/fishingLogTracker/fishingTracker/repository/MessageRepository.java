package finalProject.fishingLogTracker.fishingTracker.repository;

import finalProject.fishingLogTracker.fishingTracker.entity.Message;
import finalProject.fishingLogTracker.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(User sender, User receiver, User receiver2, User sender2);
}
