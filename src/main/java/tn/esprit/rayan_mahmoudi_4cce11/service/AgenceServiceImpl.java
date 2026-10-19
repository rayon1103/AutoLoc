package tn.esprit.rayan_mahmoudi_4cce11.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.rayan_mahmoudi_4cce11.domain.Agence;
import tn.esprit.rayan_mahmoudi_4cce11.exception.ResourceNotFoundException;
import tn.esprit.rayan_mahmoudi_4cce11.repository.IAgenceRepository;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence create(Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau agence ne doit pas avoir d'identifiant");
        }
        valider(agence);
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existant = findById(id);
        valider(agence);
        existant.setNom(agence.getNom());
        existant.setVille(agence.getVille());
        existant.setAdresse(agence.getAdresse());
        existant.setTelephone(agence.getTelephone());
        return agenceRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }

    private static void valider(Agence e) {
        if (e.getNom() == null || e.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }

        if (e.getVille() == null || e.getVille().isBlank()) {
            throw new IllegalArgumentException("La ville est obligatoire");
        }
    }
}
