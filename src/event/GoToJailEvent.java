package event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GoToJailEvent extends CardEvent{
    private boolean canCollectSalary;
}
