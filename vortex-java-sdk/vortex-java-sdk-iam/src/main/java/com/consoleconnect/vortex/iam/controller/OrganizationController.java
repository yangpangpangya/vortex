package com.consoleconnect.vortex.iam.controller;

import com.auth0.json.mgmt.organizations.Invitation;
import com.auth0.json.mgmt.organizations.Member;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.roles.Role;
import com.consoleconnect.vortex.core.model.HttpResponse;
import com.consoleconnect.vortex.core.toolkit.Paging;
import com.consoleconnect.vortex.core.toolkit.PagingHelper;
import com.consoleconnect.vortex.iam.dto.CreateConnectionDto;
import com.consoleconnect.vortex.iam.dto.CreateInivitationDto;
import com.consoleconnect.vortex.iam.dto.OrganizationConnection;
import com.consoleconnect.vortex.iam.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController()
@RequestMapping(value = "/organizations/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Organization", description = "Organization APIs")
@Slf4j
public class OrganizationController {

  private final OrganizationService service;

  @PreAuthorize("hasPermission(#orgId, 'org', 'read') ")
  @Operation(summary = "Retrieve an organization by id")
  @GetMapping()
  public Mono<HttpResponse<Organization>> findOne(@PathVariable String orgId) {
    return Mono.just(HttpResponse.ok(service.findOne(orgId)));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'read')")
  @Operation(summary = "List all existing connections")
  @GetMapping("/connections")
  public Mono<HttpResponse<Paging<OrganizationConnection>>> listConnections(
      @PathVariable String orgId,
      @RequestParam(value = "page", required = false, defaultValue = PagingHelper.DEFAULT_PAGE_STR)
          int page,
      @RequestParam(value = "size", required = false, defaultValue = PagingHelper.DEFAULT_SIZE_STR)
          int size) {
    return Mono.just(HttpResponse.ok(service.listConnections(orgId, page, size)));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'update')")
  @Operation(summary = "Setup a connection")
  @PostMapping("/connections")
  public Mono<HttpResponse<OrganizationConnection>> createConnection(
      @PathVariable String orgId,
      @RequestBody CreateConnectionDto request,
      JwtAuthenticationToken authenticationToken) {
    return Mono.just(
        HttpResponse.ok(service.createConnection(orgId, request, authenticationToken.getName())));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'read')")
  @Operation(summary = "List all invitations")
  @GetMapping("/invitations")
  public Mono<HttpResponse<Paging<Invitation>>> listInvitations(
      @PathVariable String orgId,
      @RequestParam(value = "page", required = false, defaultValue = PagingHelper.DEFAULT_PAGE_STR)
          int page,
      @RequestParam(value = "size", required = false, defaultValue = PagingHelper.DEFAULT_SIZE_STR)
          int size) {
    return Mono.just(HttpResponse.ok(service.listInvitations(orgId, page, size)));
  }

  @PreAuthorize("hasPermission(#org,'org', 'update')")
  @Operation(summary = "Create a new invitation")
  @PostMapping("/invitations")
  public Mono<HttpResponse<Invitation>> create(
      @PathVariable String orgId,
      @RequestBody CreateInivitationDto request,
      JwtAuthenticationToken jwtAuthenticationToken) {
    return Mono.just(
        HttpResponse.ok(
            service.createInvitation(orgId, request, jwtAuthenticationToken.getName())));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'read')")
  @Operation(summary = "Retrieve an invitation by id")
  @GetMapping("/invitations/{invitationId}")
  public Mono<HttpResponse<Invitation>> findOne(
      @PathVariable String orgId, @PathVariable String invitationId) {
    return Mono.just(HttpResponse.ok(service.getInvitationById(orgId, invitationId)));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'update')")
  @Operation(summary = "Delete an invitation by id")
  @DeleteMapping("/invitations/{invitationId}")
  public Mono<HttpResponse<Void>> delete(
      @PathVariable String orgId,
      @PathVariable String invitationId,
      JwtAuthenticationToken jwtAuthenticationToken) {
    service.deleteInvitation(orgId, invitationId, jwtAuthenticationToken.getName());
    return Mono.just(HttpResponse.ok(null));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'read')")
  @Operation(summary = "List all members")
  @GetMapping("/members")
  public Mono<HttpResponse<Paging<Member>>> listMembers(
      @PathVariable String orgId,
      @RequestParam(value = "page", required = false, defaultValue = PagingHelper.DEFAULT_PAGE_STR)
          int page,
      @RequestParam(value = "size", required = false, defaultValue = PagingHelper.DEFAULT_SIZE_STR)
          int size) {
    return Mono.just(HttpResponse.ok(service.listMembers(orgId, page, size)));
  }

  @PreAuthorize("hasPermission(#orgId,'org', 'read')")
  @Operation(summary = "List all roles")
  @GetMapping("/roles")
  public Mono<HttpResponse<Paging<Role>>> listRoles(
      @PathVariable String orgId,
      @RequestParam(value = "page", required = false, defaultValue = PagingHelper.DEFAULT_PAGE_STR)
          int page,
      @RequestParam(value = "size", required = false, defaultValue = PagingHelper.DEFAULT_SIZE_STR)
          int size) {
    return Mono.just(HttpResponse.ok(service.listRoles(orgId, page, size)));
  }
}
