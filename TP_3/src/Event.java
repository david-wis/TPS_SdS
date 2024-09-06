import java.util.List;

public interface Event {
    List<Particle> execute(float dt);
    boolean particleInEvent(Particle p);
    List<Particle> getParticles();
}
