
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jiachen on 7/16/17.
 * This class represents the gene constructed from DNAs.
 */
class Gene {
    private ArrayList<Steer> steers;

    /**
     * Controls the allowed mutation for the life span of the steers.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static float V1 = 0;

    /**
     * Controls the allowed mutation for the direction.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static float V2 = 3f;

    Gene() {
        steers = new ArrayList<>();
    }

    Gene(Gene clone) {
        steers = new ArrayList<>();
        for (Steer steer : clone.getSteers()) {
            steers.add(new Steer(steer));
        }
    }

    ArrayList<Steer> getSteers() {
        return steers;
    }

    /**
     * Performs meiosis where sections of the genes are exchanged with one another.
     *
     * @param other the other mouse
     * @return the gene constructed from the parents (the child's gene)
     */
    Gene performMeiosisWith(Gene other) {
        Gene mixed = new Gene();
        for (int i = 0; i < other.getSteers().size(); i++) {
            Steer section = (i % 2 == 0 ? this : other).getSteers().get(i);
            mixed.getSteers().add(section);
        }
        return mixed;
    }

    /**
     * introduce genetic mutation.
     */
    void mutate(int from) {
        for (int i = from; i < steers.size(); i++) {
            Steer steer = steers.get(i);
            int temp = (int) Math.round(steer.getLifeSpan() - V1 / 2 + Math.random() * V1);
            temp = temp < 1 ? 1 : temp;
            int newLifeSpan = temp;
            float newAngle = steer.getAngle() - V2 / 2 + (float) Math.random() * V2;
            steer.setLifeSpan(newLifeSpan);
            steer.setAngle(newAngle);
        }
    }
}
