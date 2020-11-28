package event;

import board.Space;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdvanceEvent extends CardEvent{
    private Space targetSpace;
    private boolean canCollectSalary;
}
