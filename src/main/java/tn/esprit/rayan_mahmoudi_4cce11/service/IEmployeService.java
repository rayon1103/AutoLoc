package tn.esprit.rayan_mahmoudi_4cce11.service;

import java.util.List;
import tn.esprit.rayan_mahmoudi_4cce11.domain.Employe;

public interface IEmployeService {

    Employe create(Employe employe);

    Employe findById(Long id);

    List<Employe> findAll();

    Employe update(Long id, Employe employe);

    void deleteById(Long id);
}
