/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.basic;

import com.simsilica.es.ComponentFilter;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.Name;
import com.simsilica.es.base.DefaultEntityData;
import com.simsilica.es.filter.FieldFilter;
import java.sql.SQLException;

/**
 *
 * @author Kyle
 */
public class SimpleFilterTest {

    public static void main(String[] args) throws SQLException {
        // The main entry point to my ES is EntityData… I think it better describes what we’re
// doing and avoids the “system” confusion
        EntityData ed = new DefaultEntityData();//new SqlEntityData("src/sqlDatabase", 100);  // 100 ms write-delay

// Create an entity and set some stuff using raw access
        EntityId anakin = ed.createEntity();
        ed.setComponent(anakin, new Name("Anakin Skywalker"));

        EntityId c3po = ed.createEntity();
        ed.setComponent(c3po, new Name("C-3PO"));
        ed.setComponent(c3po, new CreatedBy(anakin));


        ComponentFilter filter = FieldFilter.create(CreatedBy.class, "creatorId", anakin);
        EntitySet es = ed.getEntities(filter, CreatedBy.class, Name.class);
        es.applyChanges(); // not strictly necessary but not a bad idea
        System.out.println("Anakin’s creations:" + es);


        ComponentFilter filter2 = FieldFilter.create(Name.class, "name", "C-3PO");
        EntitySet es2 = ed.getEntities(filter, Name.class, CreatedBy.class);
        es2.applyChanges(); // not strictly necessary but not a bad idea
        System.out.println("c3po’s name:" + es2);
//        //…later in an update loop or something…
//        if (es.applyChanges()) {
//            addStuff(es.getAddedEntities());
//            removeStuff(es.getRemovedEntities());
//            updateStuff(es.getChangedEntities());
//        }
    }
}
