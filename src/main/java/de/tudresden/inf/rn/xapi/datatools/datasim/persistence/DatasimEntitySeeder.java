package de.tudresden.inf.rn.xapi.datatools.datasim.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

@Component
@Profile("dev")
public class DatasimEntitySeeder {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final DatasimProfileRepository profileRepository;
    private final DatasimSimulationRepository simulationRepository;

    public DatasimEntitySeeder(DatasimSimulationRepository simulationRepository, DatasimProfileRepository profileRepository) {
        this.simulationRepository = simulationRepository;
        this.profileRepository = profileRepository;
        this.seed();
    }

    private void seed() {
        DatasimProfile sampleProfile = new DatasimProfile("ya-cmi5", "ya-cmi5.json");
        this.profileRepository.save(sampleProfile);
        DatasimPersonaGroup sampleGroup = new DatasimPersonaGroup("Default Group", this.createSamplePersonae());
        Random random = new Random();
        DatasimSimulationParams sampleParams = new DatasimSimulationParams(1000L, random.nextLong(5000L), LocalDateTime.now().atZone(ZoneId.systemDefault()), LocalDateTime.now().plusWeeks(1).atZone(ZoneId.systemDefault()));
        DatasimSimulation simulation = this.createSampleSimulation(sampleGroup, sampleProfile, sampleParams);
        this.simulationRepository.save(simulation);
        this.logger.info("Sample simulation: http://localhost:8080/ui/datasim/new?flow=" + simulation.getId());
    }

    private Set<DatasimPersona> createSamplePersonae() {
        return Set.of(
                new DatasimPersona("Sample Persona 1", "mail1@example.org"),
                new DatasimPersona("Sample Persona 2", "mail2@example.org"),
                new DatasimPersona("Sample Persona 3", "mail3@example.org"),
                new DatasimPersona("Sample Persona 4", "mail4@example.org")
        );
    }

    private DatasimSimulation createSampleSimulation(DatasimPersonaGroup group, DatasimProfile profile, DatasimSimulationParams params) {
        return new DatasimSimulation(
                "TestSim",
                new HashSet<>(Set.of(group)),
                new HashMap<>(),
                params,
                profile
        );
    }
}
