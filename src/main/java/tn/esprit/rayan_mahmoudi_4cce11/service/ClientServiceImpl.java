package tn.esprit.rayan_mahmoudi_4cce11.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.rayan_mahmoudi_4cce11.domain.Client;
import tn.esprit.rayan_mahmoudi_4cce11.exception.ResourceNotFoundException;
import tn.esprit.rayan_mahmoudi_4cce11.repository.IClientRepository;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau client ne doit pas avoir d'identifiant");
        }
        valider(client);
        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        Client existant = findById(id);
        valider(client);
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());
        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private static void valider(Client e) {
        if (e.getNom() == null || e.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (e.getEmail() == null || e.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'e-mail est obligatoire");
        }
    }
}
