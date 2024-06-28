package org.core.backend.ticketapp.passport.dao;

import java.util.Map;
import java.util.UUID;

public interface IReliefRequestDao {

    Map<String, Object> getReliefOfficerNotExistsRolesActionsAndGroups(UUID reliefOfficerId, UUID requestedById);

    void deleteAllExpiredActionsGroupsAndActions();
}
