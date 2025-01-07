package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Contact;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.repository.ContactRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;

    public void addContactByUsername(String currentUsername, String contactUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found: " + currentUsername));

        User contactUser = userRepository.findByUsername(contactUsername)
                .orElseThrow(() -> new RuntimeException("Contact user not found: " + contactUsername));

        if (contactRepository.existsByUserIdAndContactId(currentUser.getId(), contactUser.getId())) {
            throw new RuntimeException("Contact already exists");
        }

        Contact contact = new Contact();
        contact.setUser(currentUser);
        contact.setContact(contactUser);
        contactRepository.save(contact);
    }

    public void requestContact(String currentUsername, String contactUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User contactUser = userRepository.findByUsername(contactUsername)
                .orElseThrow(() -> new RuntimeException("Contact user not found"));

        if (contactRepository.existsByUserIdAndContactId(currentUser.getId(), contactUser.getId())) {
            throw new RuntimeException("Contact request already exists");
        }

        Contact contact = new Contact();
        contact.setUser(currentUser);
        contact.setContact(contactUser);
        contact.setStatus(Contact.ContactStatus.PENDING);

        contactRepository.save(contact);
    }

    public void respondToContactRequest(Long contactId, boolean accept) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact request not found"));

        if (accept) {
            contact.setStatus(Contact.ContactStatus.ACCEPTED);

            boolean reciprocalExists = contactRepository.existsByUserIdAndContactId(
                    contact.getContact().getId(), contact.getUser().getId()
            );

            if (!reciprocalExists) {
                Contact reciprocalContact = new Contact();
                reciprocalContact.setUser(contact.getContact());
                reciprocalContact.setContact(contact.getUser());
                reciprocalContact.setStatus(Contact.ContactStatus.ACCEPTED);
                reciprocalContact.setCreatedAt(LocalDateTime.now());
                reciprocalContact.setSelected(false);

                contactRepository.save(reciprocalContact);
            }
        } else {
            // Refuser la demande
            contact.setStatus(Contact.ContactStatus.DECLINED);
        }

        contactRepository.save(contact);
    }

    public List<Contact> getSentRequests(Long userId) {
        return contactRepository.findByUserIdAndStatus(userId, Contact.ContactStatus.PENDING);
    }

    public List<Contact> getReceivedRequests(Long contactId) {
        return contactRepository.findByContactIdAndStatus(contactId, Contact.ContactStatus.PENDING);
    }

    public List<Contact> getContactsForUser(Long userId) {
        return contactRepository.findByUserId(userId);
    }



    public void removeContact(Long contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found."));
        contactRepository.delete(contact);
    }

    public long countPendingRequests(Long userId) {
        return contactRepository.countByContactIdAndStatus(userId, Contact.ContactStatus.PENDING);
    }

}

