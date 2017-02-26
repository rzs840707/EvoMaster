package org.evomaster.core.search.gene

import org.evomaster.core.search.service.Randomness

/**
 * A gene that has a major, disruptive impact on the whole chromosome.
 * As such, it should be mutated only with low probability
 */
class DisruptiveGene<out T>(name: String, val gene: T, val probability: Double) : Gene(name)
    where T: Gene{

    init {
        if (probability < 0 || probability > 1){
            throw IllegalArgumentException("Invalid probability value: $probability")
        }
    }

    override fun copy(): Gene {
        return DisruptiveGene(name, gene, probability)
    }

    override fun randomize(randomness: Randomness, forceNewValue: Boolean) {
        gene.randomize(randomness, forceNewValue)
    }

    override fun getValueAsString(): String {
        return gene.getValueAsString()
    }

    override fun isMutable() = probability > 0
}