package DTO;

import java.util.List;

public class RunStatisticsDTO implements java.io.Serializable {
    //private final Map<String, List<EntityDTO>> entities;
    private final List<EntityDTO> EntityDefinitionDTOList;

    public RunStatisticsDTO(List<EntityDTO> EntityDefinitionDTOList) {

        this.EntityDefinitionDTOList = EntityDefinitionDTOList;

    }

    public List<EntityDTO> getEntityDefinitionDTOList() {
        return EntityDefinitionDTOList;
    }


}
