package de.tudresden.inf.verdatas.xapitools.dave.controllers;

import de.tudresden.inf.verdatas.xapitools.dave.DaveAnalysisService;
import de.tudresden.inf.verdatas.xapitools.dave.persistence.DaveDashboard;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@Order(1)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class IdentifierSettingFlowController implements AnalysisStep{
    private final DaveAnalysisService daveAnalysisService;

    static final String BASE_URL = DaveAnalysisMavController.BASE_URL + "/dashboards";

    /**
     * Get the Human readable name of this Step
     *
     * @return Name of Step
     */
    @Override
    public String getName() {
        return "Set identification";
    }

    /**
     * Get the Paths which belong to this step.
     * When this pattern is matched, the step will be highlighted in the UI.
     * Be sure to match **any** subpath of your step.
     *
     * @return Regex-Pattern matching all Paths that belong to this step
     */
    @Override
    public Pattern getPathRegex() {
        return Pattern.compile(BASE_URL + "/(new|edit)$");
    }

    @GetMapping(BASE_URL + "/new")
    public ModelAndView showSetIdentifier(@RequestParam(name = "flow") Optional<UUID> dashboardId) {
        ModelAndView mav = new ModelAndView("bootstrap/dave/dashboard/identifier");
        mav.addObject("dashboardIdentifier",
                dashboardId
                        .map(this.daveAnalysisService::getDashboard)
                        .map(DaveDashboard::getIdentifier)
                        .orElse("")
        );
        dashboardId.ifPresent((id) -> mav.addObject("flow", id.toString()));
        mav.addObject("mode", DaveAnalysisMavController.Mode.CREATING);
        return mav;
    }

    @GetMapping(BASE_URL + "/edit")
    public ModelAndView showEditIdentifier(@RequestParam(name = "flow") UUID dashboardId) {
        ModelAndView mav = this.showSetIdentifier(Optional.of(dashboardId));
        mav.addObject("mode", DaveAnalysisMavController.Mode.EDITING);
        return mav;
    }

    @PostMapping(BASE_URL + "/new")
    public RedirectView setIdentifierAndCreate(@RequestParam(name = "flow") Optional<UUID> dashboardId, String identifier,
                                               DaveAnalysisMavController.Mode mode, RedirectAttributes attributes) {
        DaveDashboard dashboard = dashboardId
                .map(this.daveAnalysisService::getDashboard)
                .orElseGet(this.daveAnalysisService::createEmptyDashboard);
        this.daveAnalysisService.setDashboardIdentifier(dashboard, identifier);
        attributes.addAttribute("flow", dashboard.getId());
        return new RedirectView(DaveAnalysisMavController.Mode.CREATING.equals(mode) ? "./new/source" : "./show");
    }
}
