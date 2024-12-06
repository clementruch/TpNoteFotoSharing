package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Utilisateur;
import local.epul4a.tpnotefotosharing.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public void save(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }
}
