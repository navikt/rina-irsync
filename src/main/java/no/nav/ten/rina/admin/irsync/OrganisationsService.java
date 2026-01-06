package no.nav.ten.rina.admin.irsync;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.ten.rina.admin.irsync.util.Auth;
import no.nav.ten.rina.resources.Resource;
import org.semver4j.Semver;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.net.SocketTimeoutException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class OrganisationsService {
  private final Auth auth;

  public Resource getMaxAvailableOrganisations() {
    return getMaxOrganisations(ResourceLocation.DISK);
  }

  public Resource getMaxInstalledOrganisations() {
    return getMaxOrganisations(ResourceLocation.SERVER);
  }

  public void installOrganisations(Semver version) {
    auth.client().put()
            .uri(uriBuilder -> uriBuilder
                    .path("Resources/dataorganisations")
                    .queryParam("resourceType", "organisation")
                    .queryParam("resourceVersion", version.getVersion())
                    .build())
            .retrieve()
            .toBodilessEntity();
  }

  private enum ResourceLocation {
    DISK("DISK"),    // Available organizations
    SERVER("SERVER"); // Installed organizations

    private final String value;

    ResourceLocation(String value) {
      this.value = value;
    }
  }

  private Resource getMaxOrganisations(ResourceLocation location) {
    return getOrganisations(location).stream()
            .max(Comparator.comparing(Resource::getVersion))
            .orElse(null);
  }

  @Retryable( includes = {HttpClientErrorException.class, HttpServerErrorException.class, SocketTimeoutException.class},
          maxRetries = 8, delay = 2345, multiplier = 5 )
  private List<Resource> getOrganisations(ResourceLocation location) {
    return List.of(Objects.requireNonNull(auth.client().get()
            .uri(uriBuilder -> uriBuilder
                    .path("Resources")
                    .queryParam("resourceLocation", location.value)
                    .queryParam("hardRefresh", "true")
                    .queryParam("resourceIds", "organisation")
                    .build())
            .retrieve()
            .body(Resource[].class)));
  }
}