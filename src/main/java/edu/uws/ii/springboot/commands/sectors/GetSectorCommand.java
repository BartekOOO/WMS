package edu.uws.ii.springboot.commands.sectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSectorCommand {

    private String code;
    private Long id;

    public GetSectorCommand() {

    }

    public GetSectorCommand whereCodeEquals(String code) {
        this.code = code;
        return this;
    }

    public GetSectorCommand whereIdEquals(Long id) {
        this.id = id;
        return this;
    }
}
