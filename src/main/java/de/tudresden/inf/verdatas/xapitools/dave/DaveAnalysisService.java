package de.tudresden.inf.verdatas.xapitools.dave;

import de.tudresden.inf.verdatas.xapitools.dave.connector.DaveConnector;
import de.tudresden.inf.verdatas.xapitools.dave.connector.DaveConnectorLifecycleManager;
import de.tudresden.inf.verdatas.xapitools.dave.persistence.*;
import de.tudresden.inf.verdatas.xapitools.lrs.LrsConnection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
// TODO Zugriff auf Dokumente und deren Inhalt nur mit Überprüfung, ob vorhanden!! Auch in Controllern prüfen!!
public class DaveAnalysisService {
    private final DaveDashboardRepository dashboardRepository;
    private final DaveVisRepository visRepository;
    private final DaveQueryRepository queryRepository;
    private final DaveGraphDescriptionRepository graphDescriptionRepository;
    private final DaveConnectorLifecycleManager daveConnectorLifecycleManager;

    public DaveConnector getDaveConnector(LrsConnection lrsConnection) {
        return this.daveConnectorLifecycleManager.getConnector(lrsConnection);
    }

    public Stream<DaveDashboard> getAllDashboards() {
        return this.dashboardRepository.findAll().stream();
    }

    public DaveDashboard getDashboard(UUID dashboardId) {
        return this.dashboardRepository.findById(dashboardId).orElseThrow(() -> new NoSuchElementException("No such dashboard."));
    }

    public Stream<DaveVis> getAllAnalysis() {
        return this.visRepository.findAll().stream();
    }

    public DaveVis getAnalysisByIdentificator(String identificator) {
        return this.visRepository.findByIdentifier(identificator).orElseThrow(() -> new NoSuchElementException("No such analysis."));
    }

    //public List<String> getPathsForAnalysisDescription(DaveVis vis, DaveConnector connector) {}


    @Transactional
    public DaveDashboard createEmptyDashboard() {
        DaveDashboard emptyDashboard = new DaveDashboard(null, new LinkedList<>());
        this.dashboardRepository.save(emptyDashboard);
        return emptyDashboard;
    }

    @Transactional
    public void setDashboardIdentifier(DaveDashboard dashboard, String identifier) {
        dashboard.setIdentifier(identifier);
        this.dashboardRepository.save(dashboard);
    }

    @Transactional
    public void setDashboardSource(DaveDashboard dashboard, LrsConnection lrsConnection) {
        dashboard.setLrsConnection(lrsConnection);
        this.dashboardRepository.save(dashboard);
    }

    // TODO use path from DaveVis Object
    public List<String> getActivitiesOfLrs(LrsConnection connection) {
        List<String> paths = List.of("/home/ylvion/Downloads/query (5).json", "/home/ylvion/Downloads/query (6).json");
        List<String> activities = this.daveConnectorLifecycleManager.getConnector(connection).getAnalysisResult(paths);
        return activities.stream().map((s) -> s.substring(1, s.length() - 1)).map((s) -> s.split(" ")[1]).map((s) -> s.replace("\"", "")).toList();
    }

    public void addVisualisationToDashboard(DaveDashboard dashboard, String activity, String analysis) {
        List<Pair<String, DaveVis>> visualisations = dashboard.getVisualisations();
        visualisations.add(Pair.of(activity, this.getAnalysisByIdentificator(analysis)));
        dashboard.setVisualisations(visualisations);
        this.dashboardRepository.save(dashboard);
    }

    public List<Pair<String, DaveVis>> getVisualisationsOfDashboard(DaveDashboard dashboard) {
        return dashboard.getVisualisations();
    }
}
