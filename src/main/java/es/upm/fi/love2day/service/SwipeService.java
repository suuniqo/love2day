package es.upm.fi.love2day.service;

import java.util.Optional;

import es.upm.fi.love2day.repository.ProfilesRepository;

@Service
public class DiscoveryService {
    private final DiscoveryRepository discoveryRepository;

    public DiscoveryService(DiscoveryRepository repository) {
        this.discoveryRepository = repository;
    }
/* No creo que sea necesario, no hay model
    public Optional<Profile> findById(Long id) {
        return discoveryRepository.findById(id);
    }
*/
    //TODO: Tiene que enviar una lista de preferencias?
    //El id es es el del perfil, pues un perfil tiene solo unas preferencias?
    //Como hacemos que comunique con el profileRESTController?
    public List<Preferences> findPreferences(Long sourceId) {
        return discoveryRepository.findPreferences(sourceId);
    }

    //TODO:Deberíamos poner setters? O un método updateProfile?

    public void deleteSwipe(Long id) {
        swipesRepository.deleteById(id);
    }
}

