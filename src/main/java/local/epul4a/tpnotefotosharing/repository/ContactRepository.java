package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserId(Long userId);
    List<Contact> findByUserIdAndStatus(Long user_id, Contact.ContactStatus status);
    List<Contact> findByContactIdAndStatus(Long contactId, Contact.ContactStatus status);
    Optional<Contact> findByUserIdAndContactId(Long userId, Long contactId);

    boolean existsByUserIdAndContactId(Long userId, Long contactId);

    long countByContactIdAndStatus(Long contactId, Contact.ContactStatus status);
}
