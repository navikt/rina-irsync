package no.nav.ten.rina.admin.irsync;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.semver4j.Semver;
import org.springframework.shell.command.annotation.Command;

@Slf4j
@AllArgsConstructor
@Command(group = "IR commands", description = "IR commands description")
public class IrCommands {
  private final OrganisationsService organisationsService;
  private final SynchronisationService synchronisationService;

  @Command(command = "order", description = "Orders an IR update based on current versions")
  public void order() {
    var initialDocumentWrapper = synchronisationService.getInitialDocument();
    synchronisationService.requestIRSync(initialDocumentWrapper.getInitialDocument().getSyn002());
    log.info("ORDERED IR update for version: {}", initialDocumentWrapper.getInitialDocument().getSyn002().getRequestForIRSync().getCurrentVersion());
  }

  @Command(command = "install", description = "Installs an IR update based on current versions")
  public void install() {
    var availableVersion = new Semver(organisationsService.getMaxAvailableOrganisations().getVersion());
    var installedVersion = new Semver(organisationsService.getMaxInstalledOrganisations().getVersion());

    if (installedVersion.withClearedPreReleaseAndBuild().isLowerThan(availableVersion.withClearedPreReleaseAndBuild())) {
      log.info("OUT OF DATE : available " + availableVersion + " is > installed " + installedVersion + " - installing");
      organisationsService.installOrganisations(availableVersion);
    } else {
      log.info("UP TO DATE :  installed " + installedVersion + " is >= available " + availableVersion + " - NOT installing");
    }
  }
}
