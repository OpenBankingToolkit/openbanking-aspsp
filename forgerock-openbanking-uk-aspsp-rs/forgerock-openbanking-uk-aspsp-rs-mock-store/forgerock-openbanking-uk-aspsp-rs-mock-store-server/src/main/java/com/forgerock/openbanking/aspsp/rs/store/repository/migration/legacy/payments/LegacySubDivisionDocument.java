package com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments;

/**
 * Represents the ID and country subdivision fields within each legacy document type. This allows us to treat each
 * document in a type safe generic way.
 */
@Deprecated
public interface LegacySubDivisionDocument {

    String getId();

    String getCountrySubDivision();
}
