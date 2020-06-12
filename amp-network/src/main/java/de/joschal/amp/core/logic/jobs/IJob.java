package de.joschal.amp.core.logic.jobs;

public interface IJob {

    void init();

    void tick();

    void tearDown();

}
