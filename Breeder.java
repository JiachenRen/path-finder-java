import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by Jiachen on 7/16/17.
 * The Breeder class that manages the gene pool and breeds mice generations.
 */
public class Breeder {
    @SuppressWarnings("FieldCanBeLocal")
    private static int maxIterations = 20;
    private int geneLength;
    private int numOffspring;

    Breeder(int geneLength, int numOffspring) {
        this.geneLength = geneLength;
        this.numOffspring = numOffspring;
    }

    ArrayList<Mouse> getInitialGeneration() {
        ArrayList<Mouse> mice = new ArrayList<>();
        for (Gene gene : getInitialGenePool())
            mice.add(new Mouse(gene));
        return mice;
    }

    private ArrayList<Gene> getInitialGenePool() {
        ArrayList<Gene> genes = new ArrayList<>();
        for (int i = 0; i < numOffspring; i++) {
            genes.add(this.makeRandomizedGene());
        }
        return genes;
    }

    private Gene makeRandomizedGene() {
        Gene gene = new Gene();
        for (int i = 0; i < geneLength; i++) {
            int randIterations = (int) (Math.random() * maxIterations);
            float angle = (float) (Math.random() * Math.PI * 2);
            gene.getSteers().add(new Steer(randIterations, angle));
        }
        return gene;
    }

    /**
     * Takes the existing individuals and create the next generation!
     *
     * @param curGen the current generation.
     * @return the next generation
     */
    ArrayList<Mouse> nextGenerationOf(ArrayList<Mouse> curGen) {
        ArrayList<Mouse> viable = filterViableOffspring(curGen, Math.random() * 0.4);
        ArrayList<Mouse> offspring = crossBreed(viable);
        return mutateAndMultiply(offspring);
    }

    /**
     * @param prototypes the mice prototypes created through the process of meiosis
     * @return mutate the prototype mice and produce the next generation.
     */
    private ArrayList<Mouse> mutateAndMultiply(ArrayList<Mouse> prototypes) {
        ArrayList<Mouse> gen = new ArrayList<>(numOffspring);
        while (gen.size() < numOffspring) {
            for (Mouse proto : prototypes) {
                Gene unique = new Gene(proto.getGene());
                unique.mutate(proto.curIndex());
                gen.add(new Mouse(unique));
                if (gen.size() == numOffspring)
                    break;
            }
        }
        return gen;
    }

    /**
     * if an IllegalArgumentException error is thrown, it means that there's no viable offspring!
     *
     * @param gen        the generation to be filtered
     * @param percentage the percentage of offspring that is going to make it to the next generation
     * @return an ArrayList of chosen offspring
     */
    private ArrayList<Mouse> filterViableOffspring(ArrayList<Mouse> gen, @SuppressWarnings("SameParameterValue") double percentage) throws RuntimeException {
        gen.sort((m1, m2) -> (int) (m1.distTo(des()) - m2.distTo(des())));
        ArrayList<Mouse> viable = new ArrayList<>(numOffspring);
        while ((viable.size() / (float) numOffspring) <= percentage)
            viable.add(gen.remove(0));
        viable.forEach(System.out::println);
        return viable;
    }

    /**
     * In genetics, meiosis is the process of two chromosomes exchanging DNA with one another.
     * This method does exactly what its name suggests.
     *
     * @param viable the ArrayList of viable parents.
     * @return the offspring they produce.
     */
    private ArrayList<Mouse> crossBreed(ArrayList<Mouse> viable) {
        ArrayList<Mouse> offspring = new ArrayList<>(numOffspring);
        while (offspring.size() < numOffspring) {
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < viable.size(); i++) {
//                for (int q = i + 1; q < viable.size(); q++) {
//                    Mouse mom = viable.get(i), dad = viable.get(q);
//                    Mouse born = mom.breedWith(dad);
//                    born.setCurIndex((mom.curIndex() + dad.curIndex()) / 3);
//                    offspring.add(born);
//                }

                //alternative method
                Mouse born = new Mouse(viable.get(i).getGene());
                int index = (int)(viable.get(i).curIndex() - (i / (float) viable.size()) * viable.get(i).curIndex() / 2 + 3);
                born.setCurIndex(index < 0 ? 0 : index);
                offspring.add(born);
                if (offspring.size() == numOffspring) break;
            }
        }
        return offspring;
    }


    /**
     * @return the destination in the simulation
     */
    private PVector des() {
        return Simulation.destination;
    }

    public int getGeneLength() {
        return geneLength;
    }

    public void setGeneLength(int geneLength) {
        this.geneLength = geneLength;
    }

    public int getNumOffspring() {
        return numOffspring;
    }

    public void setNumOffspring(int numOffspring) {
        this.numOffspring = numOffspring;
    }
}
