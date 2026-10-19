package tn.esprit.rayan_mahmoudi_4cce11.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.rayan_mahmoudi_4cce11.domain.Maintenance;
import tn.esprit.rayan_mahmoudi_4cce11.exception.ResourceNotFoundException;
import tn.esprit.rayan_mahmoudi_4cce11.repository.IMaintenanceRepository;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        if (maintenance.getIdMaintenance() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau maintenance ne doit pas avoir d'identifiant");
        }
        valider(maintenance);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        Maintenance existant = findById(id);
        valider(maintenance);
        existant.setDateDebut(maintenance.getDateDebut());
        existant.setDateFin(maintenance.getDateFin());
        existant.setDescription(maintenance.getDescription());
        return maintenanceRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }

    private static void valider(Maintenance e) {
        if (e.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }

        if (e.getDateFin() != null && e.getDateFin().isBefore(e.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }
    }
}
