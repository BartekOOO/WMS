package edu.uws.ii.springboot.commands.sectors;

import edu.uws.ii.springboot.models.Sector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditSectorCommand {

    private Long id;
    private String code;
    private String name;

    public EditSectorCommand() {

    }

    public EditSectorCommand configureSector(Sector sector) {
        this.id = sector.getId();
        this.code = sector.getCode();
        this.name = sector.getName();
        return this;
    }
}
