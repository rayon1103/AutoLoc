package tn.esprit.rayan_mahmoudi_4cce11.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.rayan_mahmoudi_4cce11.domain.Vehicule;
import tn.esprit.rayan_mahmoudi_4cce11.exception.ResourceNotFoundException;
import tn.esprit.rayan_mahmoudi_4cce11.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau vehicule ne doit pas avoir d'identifiant");
        }
        valider(vehicule);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    public List<Vehicule> findAll() {
        return (List<Vehicule>) vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existant = findById(id);
        valider(vehicule);
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        return vehiculeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    private static void valider(Vehicule e) {
        if (e.getImmatriculation() == null || e.getImmatriculation().isBlank()) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire");
        }

        if (e.getTarifJournalier() == null) {
            throw new IllegalArgumentException("Le tarif journalier est obligatoire");
        }

        if (e.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne doit pas être négatif");
        }
    }
}
