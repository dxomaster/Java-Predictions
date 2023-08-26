package DTO;

import java.util.List;

public class RunStatisticsDTO implements java.io.Serializable {
    //private final Map<String, List<EntityDTO>> entities;
    private final List<StatisticEntityDTO> EntityDefinitionDTOList;

    public RunStatisticsDTO(List<StatisticEntityDTO> EntityDefinitionDTOList) {

        this.EntityDefinitionDTOList = EntityDefinitionDTOList;

    }

    public List<StatisticEntityDTO> getEntityDefinitionDTOList() {
        return EntityDefinitionDTOList;
    }


}
