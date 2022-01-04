package de.tudresden.inf.rn.xapi.datatools.lrs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/ui/manage/lrs")
@RequiredArgsConstructor
public class LrsMavController {
    private final LrsService lrsService;

    @GetMapping("/")
    public ModelAndView showLrsConnections(@RequestParam(name = "active_only") Optional<Boolean> activeOnly) {
        ModelAndView mav = new ModelAndView("bootstrap/lrs/show");
        mav.addObject("connections", this.lrsService.getConnections(activeOnly.orElse(true)).stream().map(LrsConnectionTO::of).toList());
        return mav;
    }

    @PostMapping("/delete")
    public ModelAndView deleteLrsConnection(@RequestParam(name = "lrs_uuid") UUID lrsUuid) {
        // TODO: This can throw an IAE. See this in #16
        this.lrsService.deactivateConnection(lrsUuid);
        return new ModelAndView("redirect:/ui/manage/lrs/");
    }

    @PostMapping("/reactivate")
    public ModelAndView reactivateLrsConnection(@RequestParam(name = "lrs_uuid") UUID lrsUuid) {
        // TODO: This can throw an IAE. See this in #16
        this.lrsService.activateConnection(lrsUuid);
        return new ModelAndView("redirect:/ui/manage/lrs/");
    }

    @GetMapping("/edit")
    public ModelAndView showEditLrsConnection(@RequestParam(name = "lrs_uuid") UUID lrsUuid) {
        // TODO: This can throw an IAE. See this in #16
        LrsConnection found = this.lrsService.getConnection(lrsUuid);
        ModelAndView mav = new ModelAndView("bootstrap/lrs/detail");
        mav.addObject("connection", LrsConnectionTO.of(found));
        mav.addObject("method", "edit");
        return mav;
    }

    @PostMapping("/edit")
    public ModelAndView editLrsConnection(@Validated LrsConnectionTO data) {
        // TODO: This can throw an IAE. See this in #16
        this.lrsService.updateConnection(data);
        return new ModelAndView("redirect:/ui/manage/lrs/");
    }

    @GetMapping("/add")
    public ModelAndView showAddLrsConnection() {
        ModelAndView mav = new ModelAndView("bootstrap/lrs/detail");
        mav.addObject("method", "add");
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addLrsConnection(@Validated LrsConnectionTO data) {
        this.lrsService.createConnection(data);
        return new ModelAndView("redirect:/ui/manage/lrs/");
    }
}
