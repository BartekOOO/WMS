package edu.uws.ii.springboot.commands.sectors;

import edu.uws.ii.springboot.models.Sector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteSectorCommand {

    private Long id;

    public DeleteSectorCommand() {

    }

    public DeleteSectorCommand configureSector(Long id) {
        this.id = id;
        return this;
    }

    public DeleteSectorCommand configureSector(Sector sector) {
        this.id = sector.getId();
        return this;
    }

}
