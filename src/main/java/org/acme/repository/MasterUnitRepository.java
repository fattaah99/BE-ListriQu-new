package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.MasterUnit;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;



@ApplicationScoped
public class MasterUnitRepository implements PanacheRepositoryBase<MasterUnit, Integer> {
}
