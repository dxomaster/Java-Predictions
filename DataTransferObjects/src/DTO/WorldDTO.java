package DTO;

import java.util.List;

public class WorldDTO implements java.io.Serializable {
    private List<PropertyDTO> environmentProperties;
    private List<EntityDTO> entities;
    private List<RuleDTO> rules;
    private terminationDTO termination;


    public WorldDTO(List<PropertyDTO> environmentProperties, List<EntityDTO> entities, List<RuleDTO> rules, terminationDTO termination) {
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

    public terminationDTO getTermination() {
        return termination;
    }
}
