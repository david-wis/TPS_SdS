import java.util.List;

public interface Event {
    List<Particle> execute(float dt);
}
