package base.application.gen2;

import base.gen.SourceBuilder;
import base.model.MetaObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyManager<PARENT extends MetaObject, CHILD extends MetaObject> {

    transient private final Map<String, PARENT> addablePool = new HashMap<>();
    transient final private Map<String, Map<String, CHILD>> dependents
            = new ConcurrentHashMap<>();

    public Map<String, PARENT> getAddablePool() {
        return Collections.unmodifiableMap(this.addablePool);
    }

    public void addToPool(final PARENT parent) {
        addablePool.put(parent.uuid(), parent);
    }

    public void removeFromPool(final PARENT parent) {
        if (dependents.containsKey(parent.uuid())) {
            throw new IllegalStateException("Cannot remove " + parent + ".  Depended on by " + getDependents(parent).size() + " objects.");
        }
        addablePool.put(parent.uuid(), parent);
    }

    public synchronized DependencyManager addDepedency(
            final String referencedUUID,
            final CHILD referencingObject) {
        if (!addablePool.containsKey(referencedUUID)) {
            throw new RuntimeException("Object is not in pool " + referencedUUID);
        }
        dependents.putIfAbsent(referencedUUID, new ConcurrentHashMap<>());
        dependents.get(referencedUUID).put(
                referencingObject.uuid(),
                referencingObject);
        return this;
    }

    public synchronized DependencyManager addDepedency(
            final PARENT referencedObject,
            final CHILD referencingObject) {
        final String referencedUUID = referencedObject.uuid();
        return this.addDepedency(referencedUUID, referencingObject);
    }

    public synchronized DependencyManager removeDepedency(
            final String referencedUUID,
            final CHILD referencingObject) {
        final String reffedUUID = referencingObject.uuid();
        dependents.get(referencedUUID).remove(reffedUUID);
        if (dependents.get(referencedUUID).isEmpty()) {
            dependents.remove(referencedUUID);
        }
        return this;
    }

    public synchronized DependencyManager removeDepedency(
            final PARENT referencedObject,
            final CHILD referencingObject) {
        final String referencedUUID = referencedObject.uuid();
        return removeDepedency(referencedUUID, referencingObject);
    }

    public synchronized List<CHILD> getDependents(final PARENT referencedObject) {
        final List<CHILD> dependentsCopy = new LinkedList<>();
        if (dependents.containsKey(referencedObject.uuid())) {
            dependentsCopy.addAll(dependents.get(referencedObject.uuid()).values());
        }
        return dependentsCopy;
    }

    public final boolean isDependedOn(final PARENT referencedObject) {
        return !getDependents(referencedObject).isEmpty();
    }

//    public void clear() {
//        this.dependents.clear();
//    }
    public String getPoolReport() {
        final SourceBuilder bldr = new SourceBuilder("Addable set").appendln();
        for (final Map.Entry<String, PARENT> entry : addablePool.entrySet()) {
            bldr.append(1, entry.getKey()).append(" -> ").append(entry.getValue());
        }
        bldr.appendln("Dep Set");
        for (final Map.Entry<String, Map<String, CHILD>> depSets
                : dependents.entrySet()) {
            final String parentKey = depSets.getKey();
            bldr.append(1, "Listing dependents of ").appendln(parentKey);
            if (!addablePool.containsKey(parentKey)) {
                bldr.appendln(1, "!!!! Could not find referenced ").appendln(parentKey);
            }
            for (final Map.Entry<String, CHILD> deps : depSets.getValue().entrySet()) {
                if (!deps.getValue().uuid().equals(deps.getKey())) {
                    bldr.appendln(1, "!!!! Could not match uuids ").appendln(deps.getValue().uuid()).append(" != ").append(deps.getKey());
                }
                bldr.appendln(1, deps.getValue());
            }
        }
        return bldr.toString();
    }

}
