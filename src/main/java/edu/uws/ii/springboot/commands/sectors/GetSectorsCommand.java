package edu.uws.ii.springboot.commands.sectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSectorsCommand {
    private String code;
    private Long id;

    public GetSectorsCommand() {

    }

    public GetSectorsCommand whereCodeEquals(String code) {
        this.code = code;
        return this;
    }

    public GetSectorsCommand whereIdEquals(Long id) {
        this.id = id;
        return this;
    }
}
