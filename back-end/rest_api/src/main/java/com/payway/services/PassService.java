package com.payway.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.payway.repository.PassRepository;
import com.payway.repository.TagRepository;

@Service
public class PassService {

    private final PassRepository passRepository;
    private final TagRepository tagRepository; // Εάν οι tags είναι συνδεδεμένες με τις διελεύσεις

    @Autowired
    public PassService(PassRepository passRepository, TagRepository tagRepository) {
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
    }

    public void resetPasses() {
        try {
            // Διαγραφή των διελεύσεων (passes)
            passRepository.deleteAll();

            // Διαγραφή των εξαρτώμενων δεδομένων (πχ: tags, αν υπάρχουν)
            tagRepository.deleteAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to reset passes and dependent data: " + e.getMessage());
        }
    }
}
