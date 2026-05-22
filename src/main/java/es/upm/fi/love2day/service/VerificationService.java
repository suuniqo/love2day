package es.upm.fi.love2day.service;

@Service
public class VerificationService {
    private final VerificationsRepository verificationsRepository;

    public VerificationService(VerificationsRepository repository) {
        this.verificationsRepository = repository;
    }

    // TODO:El último parámetro debería ser un documento
    // Debe devolver el verification, o el verificationid??
    public Verification createVerification(long userid, DocumentType documentType, String document) {
        Verification verification = new verification(userid, PENDING);

        long verificationid = VerificationRepository.save(verification);
        //submitDocumentod(verificationid,documentType,document);
        
        return verification;
    }

    public Optional<Verification> findById(Long id) {
        return verificationsRepository.findById(id);
    }

    public Optional<Verification> findByUserid(Long userid) {
        return verificationsRepository.findByUserid(userid);
    }

    public void setState(VerificationState verificationState) {
        Verification verification = verificationsRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Verification not found"));

        verification.setStatus(VERIFIED); // TODO: implementar verificación de verdad

        verificationsRepository.save(verification);
    }
}
