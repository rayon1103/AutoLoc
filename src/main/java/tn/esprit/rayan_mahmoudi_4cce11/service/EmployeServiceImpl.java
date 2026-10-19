package tn.esprit.rayan_mahmoudi_4cce11.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.rayan_mahmoudi_4cce11.domain.Employe;
import tn.esprit.rayan_mahmoudi_4cce11.exception.ResourceNotFoundException;
import tn.esprit.rayan_mahmoudi_4cce11.repository.IEmployeRepository;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau employe ne doit pas avoir d'identifiant");
        }
        valider(employe);
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existant = findById(id);
        valider(employe);
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    private static void valider(Employe e) {
        if (e.getNom() == null || e.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (e.getPrenom() == null || e.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }

        if (e.getRole() == null) {
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }
    }
}
