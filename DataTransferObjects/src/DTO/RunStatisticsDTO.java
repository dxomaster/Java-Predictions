package DTO;

import java.util.List;

public class RunStatisticsDTO implements java.io.Serializable{
    private final String formattedDate;
    private final String UUID;
    //private final Map<String, List<EntityDTO>> entities;
    private final List<EntityDTO> EntityDefinitionDTOList;

    public RunStatisticsDTO(String formattedDate, String UUID, List<EntityDTO> EntityDefinitionDTOList) {
        this.formattedDate = formattedDate;
        this.UUID = UUID;

        this.EntityDefinitionDTOList = EntityDefinitionDTOList;

    }

    public List<EntityDTO> getEntityDefinitionDTOList() {
        return EntityDefinitionDTOList;
    }




}
