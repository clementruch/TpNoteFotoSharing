package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Contact;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.service.ContactService;
import local.epul4a.tpnotefotosharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listContacts(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        Long userId = user.getId();

        List<Contact> contacts = contactService.getContactsForUser(userId);
        long pendingRequestsCount = contactService.countPendingRequests(user.getId());

        model.addAttribute("contacts", contacts);
        model.addAttribute("pendingRequestsCount", pendingRequestsCount);
        return "contacts/contactsList";
    }

    @PostMapping("/request")
    public String requestContact(@RequestParam String contactUsername, Authentication authentication) {
        String currentUsername = authentication.getName();
        contactService.requestContact(currentUsername, contactUsername);
        return "redirect:/contacts";
    }

    @GetMapping("/requests/received")
    public String listReceivedRequests(Model model, Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        model.addAttribute("receivedRequests", contactService.getReceivedRequests(user.getId()));
        return "contacts/receivedRequests";
    }

    @PostMapping("/respond")
    public String respondToRequest(@RequestParam Long contactId, @RequestParam boolean accept) {
        contactService.respondToContactRequest(contactId, accept);
        return "redirect:/contacts";
    }

    @PostMapping("/add")
    public String addContact(@RequestParam String contactUsername, Authentication authentication) {
        String currentUsername = authentication.getName();
        contactService.addContactByUsername(currentUsername, contactUsername);
        return "redirect:/contacts";
    }

    @PostMapping("/remove")
    public String removeContact(@RequestParam Long contactId) {
        contactService.removeContact(contactId);
        return "redirect:/contacts";
    }

    @RestController
    @RequestMapping("/api/contacts")
    public class ContactRestController {

        @Autowired
        private ContactService contactService;

        @GetMapping("/pending-count")
        public long getPendingRequestsCount(Authentication authentication) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            return contactService.countPendingRequests(user.getId());
        }
    }

}
