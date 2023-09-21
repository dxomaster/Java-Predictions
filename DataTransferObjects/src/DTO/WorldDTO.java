package DTO;

import java.util.List;

public class WorldDTO implements java.io.Serializable {
    private final List<PropertyDTO> environmentProperties;
    private final List<EntityDTO> entities;
    private final List<RuleDTO> rules;
    private final TerminationDTO termination;


    public WorldDTO(List<PropertyDTO> environmentProperties, List<EntityDTO> entities, List<RuleDTO> rules, TerminationDTO termination) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.termination = termination;
    }

    public String toString() {

        return "";
    }

    public List<PropertyDTO> getEnvironmentProperties() {
        return environmentProperties;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public TerminationDTO getTermination() {
        return termination;
    }
}
